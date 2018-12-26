package com.pet.webfluxthymeleaf.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.webfluxthymeleaf.app.movie.Movie
import com.pet.webfluxthymeleaf.app.movie.MovieService
import com.pet.webfluxthymeleaf.infrastructure.parseJson
import com.pet.webfluxthymeleaf.infrastructure.web.WebRequestValidator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.time.delay
import org.slf4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono
import java.net.URI
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Paths
import java.time.Duration
import kotlin.collections.set

class HomeController(
	private val movieService: MovieService,
	private val webClient: WebClient,
	private val mapper: ObjectMapper,
	private val logger: Logger,
	private val validator: WebRequestValidator
) {

//	@GetMapping("/")
//	fun index(): Mono<String> {
//		val res = Mono
//              .delay(Duration.ofSeconds(3))
//				.map { "index" }
//
//		return res;
//	}

	//@GetMapping("/")
	fun index(request: ServerRequest): Mono<ServerResponse> = GlobalScope.mono {

		val movies = movieService.getMoviesFromDb()
			.collectList()
			.awaitFirst()
			.sortedBy { it.id }

		val model = mapOf<String, Any>(
			// this is a Flux. It just works, WebFlux handles it: subscribes to it and renders the view only if the Publisher is done.
			// this will take ~3 seconds to complete --> WebFlux renders the view after 3 seconds (right after the Flux gets completed)
			"movies" to movieService.getMoviesWithGenerate2("WebFlux subscribes to it").take(3),
			"superMovies" to movies,
			"firstSourceTitle" to "Movies from Mongo database after with coroutine suspending on a Flux",
			"secondSourceTitle" to "Movies from Mongo. The Flux itself is passed to the model --> WebFlux subscribes to it and starts rendering the view only after the Flux gets completed."
		)

		ServerResponse
			.ok()
			.render("index", model)
			.block()

//		Rendering.view("index")
//			.modelAttribute("movies", movieService.getMoviesWithGenerate2("WebFlux subscribes to it").take(3))
//			.modelAttribute("superMovies", movies)
//			.modelAttribute("firstSourceTitle", "Movies from Mongo database after with coroutine suspending on a Flux")
//			.modelAttribute(
//				"secondSourceTitle",
//				"Movies from Mongo. The Flux itself is passed to the model --> WebFlux subscribes to it and starts rendering the view only after the Flux gets completed."
//			)
//			.build()
	}

	//@GetMapping("/push", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
	fun push(request: ServerRequest): Mono<ServerResponse> = GlobalScope.mono {

		val movies = movieService.getMoviesFromDb()
			.collectList()
			.awaitFirst()
			.sortedBy { it.id }

		// dataStreamBufferSizeElements 1: this means to flush every item  (default is 10 - flush data after the Flux has fired 10 times)
		// this is a push model because of "ReactiveDataDriverContextVariable" --> Thymeleaf itself subscribes to the wrapped Flux - so WebFlux doesn't do anything in case of a ReactiveDataDriverContextVariable
		val reactiveMovies = ReactiveDataDriverContextVariable(movieService.getMoviesWithGenerate().take(5), 1)

		val model = mapOf<String, Any>(
			"firstSourceTitle" to "Movies from ReactiveDataDriverContextVariable - Flux must be used. This enables a server pushing model. In this case Thymeleaf itself subscribes to the Flux and renders a section whenever a new data arrives until the Flux gets completed.",
			"secondSourceTitle" to "Movies from Mongo database after with coroutine suspending on a Flux",
			"superMovies" to movies,
			"movies" to reactiveMovies
		)

		ServerResponse.ok()
			.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
			.render("index", model)
			.block()
	}

	//@GetMapping("/long")
	fun longRunningSimulation(request: ServerRequest): Mono<ServerResponse> = GlobalScope.mono {
		val view = Mono
			.delay(Duration.ofSeconds(1))
			.map { "index" }
			.awaitFirst()

		// getMoviesWithGenerate is an infinite stream
		val movies = movieService.getMoviesWithGenerate("Super")
			.take(5)
			// transform the Flux<T> into a Mono<List<T>>
			// without this "awaitFirst" sends the 1st Movie back as soon as it is read from the db
			.collectList()
			.awaitFirst()

		delay(Duration.ofSeconds(1))

		val model = mapOf<String, Any>(
			"firstSourceTitle" to "Fixed movies from a fix Flux.",
			"secondSourceTitle" to "Movies from an infinite Flux stream. Queried only the first 5, each one took 1 second to get.",
			"movies" to movieService.getMoviesFix("Super"),
			"superMovies" to movies
		)

		ServerResponse
			.ok()
			.render(view, model)
			.block()
	}

	//@GetMapping("/file")
	fun files(request: ServerRequest): Mono<ServerResponse> = GlobalScope.mono {

		val model = mutableMapOf<String, Any>()
		model["firstSourceTitle"] = "Movies read asynchronously from external system"
		model["secondSourceTitle"] = "Movies read asynchronously from the filesystem"

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
			logger.error("Error while trying to get the movies from mongodb", e)
		}


		var channel: AsynchronousFileChannel? = null
		try {
			channel = AsynchronousFileChannel.open(Paths.get(ClassLoader.getSystemResource("movies.json").toURI()))
			val moviesFromFile = channel.parseJson<Array<Movie>>(mapper)

			model["superMovies"] = moviesFromFile
		} finally {
			channel?.close()
		}

		ServerResponse.ok()
			.render("index", model)
			.block()
	}

	//@PostMapping("/")
	//@ResponseBody
	fun createMovie(request: ServerRequest): Mono<ServerResponse> = GlobalScope.mono {
		val movie = validator.bodyToMono<Movie>(request).awaitFirst()

		// do NOT put bindingResult: BindingResult in the url because WebFlux cannot provide it and exception will be thrown!
		// the request is refused (400 becomes the response's status automatically) if the movie is invalid automatically, no need to manually handle it
//		if (!bindingResult.hasErrors()) {
//			logger.info(bindingResult)
//			return@mono ServerResponse.badRequest().syncBody(bindingResult.allErrors).awaitFirst()
//		}

		val entity = movieService.createMovie(movie).awaitFirst()
		// ServerResponse does not work! response's Content-Type will be text/event-stream and statuscode becomes 200
		// use the good old ResponseEntity instead
		//val result = ServerResponse.created(URI.create("${entity.id}")).build().awaitFirst()

		ServerResponse.created(URI.create("${entity.id}"))
			.syncBody("!Done")
			.block()
	}

	//@GetMapping("/movies")
	//@ResponseBody
	fun getMovies(request: ServerRequest): Mono<ServerResponse> {
		val movies = movieService.getMoviesFromDb()

		return ServerResponse.ok()
			.body(movies, Movie::class.java)
	}

	//@GetMapping("/movies/{id}")
	//@ResponseBody
	fun getMovie(request: ServerRequest): Mono<ServerResponse> {
		val id = request.pathVariable("id").toInt()

		return ServerResponse
			.ok()
			.body(movieService.getMovie2(id), Movie::class.java)
	}
}