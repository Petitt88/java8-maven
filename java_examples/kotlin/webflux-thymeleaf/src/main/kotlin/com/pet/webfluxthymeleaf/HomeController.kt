package com.pet.webfluxthymeleaf

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.webfluxthymeleaf.infrastructure.parseJson
import com.pet.webfluxthymeleaf.movie.Movie
import com.pet.webfluxthymeleaf.movie.MovieService
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.reactor.mono
import kotlinx.coroutines.experimental.time.delay
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.body
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
	fun index(model: Model): Mono<String> = mono(Unconfined) {

		model.addAttribute("firstSourceTitle", "Movies from ReactiveDataDriverContextVariable - Flux must be used")
				.addAttribute("secondSourceTitle", "Movies from Mongo database after with coroutine suspending on a Flux")

		model.addAttribute("movies", ReactiveDataDriverContextVariable(movieService.getMoviesFromDb(10)))

		var movies = movieService.getMoviesFromDb().collectList().awaitFirst()
		movies = movies.sortedBy { it.id }
		model.addAttribute("superMovies", movies);

		"index";
	}

	@GetMapping("/long")
	fun longRunningSimulation(model: Model) = mono {

		model.addAttribute("firstSourceTitle", "Fixed movies from already resolved Flux via ReactiveDataDriverContextVariable")
				.addAttribute("secondSourceTitle", "Movies from an infinite Flux stream. Queried only the first 5, each one took 1 second to get.")

		val view = Mono
				.delay(Duration.ofSeconds(1))
				.map { "index" }
				.awaitFirst()

		model.addAttribute("movies", ReactiveDataDriverContextVariable(movieService.getMoviesFix("Super")))

		// getMoviesWithGenerate is an infinite stream
		val movies = movieService.getMoviesWithGenerate("Super")
				.take(5)
				// transform the Flux<T> into a Mono<List<T>>
				// without this "awaitFirst" sends the 1st Movie back as soon as it is read from the db
				.collectList()
				.awaitFirst()
		model.addAttribute("superMovies", movies);

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
			model.addAttribute("movies", moviesFromExternalSystem)
		} catch (e: Exception) {
			println(e.message)
		}


		var channel: AsynchronousFileChannel? = null;
		try {
			channel = AsynchronousFileChannel.open(Paths.get(ClassLoader.getSystemResource("movies.json").toURI()))
			val moviesFromFile = channel.parseJson<Array<Movie>>(mapper)

			model.addAttribute("superMovies", moviesFromFile);
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
		val result = ResponseEntity.created(URI.create("${entity.id}")).body("Done!")

		//val result = ServerResponse.created(URI.create("${entity.id}")).build().awaitFirst()
		result
	}

	@GetMapping("/movies")
	@ResponseBody
	fun getMovies(): Mono<ServerResponse> {
		return ServerResponse.ok().body(movieService.getMoviesFromDb())
	}
}