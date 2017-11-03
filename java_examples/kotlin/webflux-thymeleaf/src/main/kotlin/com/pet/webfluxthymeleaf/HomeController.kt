package com.pet.webfluxthymeleaf

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.webfluxthymeleaf.infrastructure.readToEnd
import com.pet.webfluxthymeleaf.movie.Movie
import com.pet.webfluxthymeleaf.movie.MovieService
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.reactor.mono
import kotlinx.coroutines.experimental.time.delay
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.reactive.function.client.WebClient
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Paths
import java.time.Duration


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

		model.addAttribute("movies", ReactiveDataDriverContextVariable(movieService.getMoviesFromDb(10)))

		val movies = movieService.getMoviesFromDb().collectList().awaitFirst()
		model.addAttribute("superMovies", movies);

		"index";
	}

	@GetMapping("/long")
	fun longRunningSimulation(model: Model) = mono {

		val view = Mono
				.delay(Duration.ofSeconds(1))
				.map { "index" }
				.awaitFirst()

		model.addAttribute("movies", movieService.getMoviesFix())

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

		val moviesFromExternalSystem = webClient
				.get()
				.uri("/movies")
				.retrieve()
				.bodyToFlux(Movie::class.java)
				.collectList()
				.awaitFirst()

		model.addAttribute("movies", moviesFromExternalSystem)

		var channel: AsynchronousFileChannel? = null;
		try {
			channel = AsynchronousFileChannel.open(Paths.get(ClassLoader.getSystemResource("movies.json").toURI()))
			val moviesFromFile = channel.readToEnd<Array<Movie>>(mapper)

			model.addAttribute("superMovies", moviesFromFile);
		} finally {
			channel?.close()
		}

		"index"
	}
}