package com.pet.webfluxthymeleaf

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.webfluxthymeleaf.infrastructure.parseJson
import com.pet.webfluxthymeleaf.movie.Movie
import com.pet.webfluxthymeleaf.movie.MovieService
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.reactor.mono
import kotlinx.coroutines.experimental.time.delay
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.result.view.Rendering
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono
import java.lang.Exception
import java.net.URI
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Paths
import java.time.Duration
import javax.validation.Valid


@Controller
@RequestMapping("/")
class HomeController(private val movieService: MovieService,
                     private val webClient: WebClient,
                     private val mapper: ObjectMapper) {

//	@GetMapping("/")
//	fun index(): Mono<String> {
//		val res = Mono
//              .delay(Duration.ofSeconds(3))
//				.map { "index" }
//
//		return res;
//	}

	@GetMapping("/")
	fun index(): Mono<Rendering> = mono(Unconfined) {

		val movies = movieService.getMoviesFromDb()
				.collectList()
				.awaitFirst()
				.sortedBy { it.id }

		Rendering.view("index")
				// this is a Flux. It just works, WebFlux handles it: subscribes to it and renders the view only if the Publisher is done.
				// this will take ~3 seconds to complete --> WebFlux renders the view after 3 seconds (right after the Flux gets completed)
				.modelAttribute("movies", movieService.getMoviesWithGenerate2("WebFlux subscribes to it").take(3))
				.modelAttribute("superMovies", movies)
				.modelAttribute("firstSourceTitle", "Movies from Mongo database after with coroutine suspending on a Flux")
				.modelAttribute("secondSourceTitle", "Movies from Mongo. The Flux itself is passed to the model --> WebFlux subscribes to it and starts rendering the view only after the Flux gets completed.")
				.build()
	}

	@GetMapping("/push", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
	fun push(model: Model): Mono<String> = mono(Unconfined) {

		model["firstSourceTitle"] = "Movies from ReactiveDataDriverContextVariable - Flux must be used. This enables a server pushing model. In this case Thymeleaf itself subscribes to the Flux and renders a section whenever a new data arrives until the Flux gets completed."
		model["secondSourceTitle"] = "Movies from Mongo database after with coroutine suspending on a Flux"

		val movies = movieService.getMoviesFromDb()
				.collectList()
				.awaitFirst()
				.sortedBy { it.id }
		model["superMovies"] = movies

		// dataStreamBufferSizeElements 1: this means to flush every item  (default is 10 - flush data after the Flux has fired 10 times)
		// this is a push model because of "ReactiveDataDriverContextVariable" --> Thymeleaf itself subscribes to the wrapped Flux - so WebFlux doesn't do anything in case of a ReactiveDataDriverContextVariable
		model["movies"] = ReactiveDataDriverContextVariable(movieService.getMoviesWithGenerate().take(5), 1)

		"index"
	}

	@GetMapping("/long")
	fun longRunningSimulation(model: Model) = mono {

		model.addAttribute("firstSourceTitle", "Fixed movies from a fix Flux.")
				.addAttribute("secondSourceTitle", "Movies from an infinite Flux stream. Queried only the first 5, each one took 1 second to get.")

		val view = Mono
				.delay(Duration.ofSeconds(1))
				.map { "index" }
				.awaitFirst()

		model["movies"] = movieService.getMoviesFix("Super")

		// getMoviesWithGenerate is an infinite stream
		val movies = movieService.getMoviesWithGenerate("Super")
				.take(5)
				// transform the Flux<T> into a Mono<List<T>>
				// without this "awaitFirst" sends the 1st Movie back as soon as it is read from the db
				.collectList()
				.awaitFirst()
		model["superMovies"] = movies

		delay(Duration.ofSeconds(1))

		view
	}

	@GetMapping("/file")
	fun files(model: Model) = mono {

		model.addAttribute("firstSourceTitle", "Movies read asynchronously from external system")
				.addAttribute("secondSourceTitle", "Movies read asynchronously from the filesystem")

		// the server may be down for some reason
		try {
			val moviesFromExternalSystem = webClient
					.get()
					.uri("/movies")
					.retrieve()
					.bodyToFlux(Movie::class.java)
					.collectList()
					.awaitFirst()
			model["movies"] = moviesFromExternalSystem
		} catch (e: Exception) {
			println(e.message)
		}


		var channel: AsynchronousFileChannel? = null
		try {
			channel = AsynchronousFileChannel.open(Paths.get(ClassLoader.getSystemResource("movies.json").toURI()))
			val moviesFromFile = channel.parseJson<Array<Movie>>(mapper)

			model["superMovies"] = moviesFromFile
		} finally {
			channel?.close()
		}

		"index"
	}

	@PostMapping("/")
	@ResponseBody
	fun createMovie(@RequestBody @Valid movie: Movie): Mono<ResponseEntity<*>> = mono {

		// do NOT put bindingResult: BindingResult in the url because WebFlux cannot provide it and exception will be thrown!
		// the request is refused (400 becomes the response's status automatically) if the movie is invalid automatically, no need to manually handle it
//		if (!bindingResult.hasErrors()) {
//			println(bindingResult)
//			return@mono ServerResponse.badRequest().syncBody(bindingResult.allErrors).awaitFirst()
//		}

		val entity = movieService.createMovie(movie).awaitFirst()
		// ServerResponse does not work! response's Content-Type will be text/event-stream and statuscode becomes 200
		// use the good old ResponseEntity instead
		//val result = ServerResponse.created(URI.create("${entity.id}")).build().awaitFirst()

		ResponseEntity.created(URI.create("${entity.id}")).body("Done!")
	}

	@GetMapping("/movies")
	@ResponseBody
	fun getMovies(): Mono<ResponseEntity<List<Movie>>> = mono {
		val movies = movieService.getMoviesFromDb().collectList().awaitFirst()
		ResponseEntity.ok().body(movies)
	}
}