package com.pet.webfluxthymeleaf

import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.reactor.mono
import kotlinx.coroutines.experimental.time.delay
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Mono
import java.time.Duration


@Controller
@RequestMapping("/")
class HomeController(private val movieService: MovieService) {

//	@GetMapping("/")
//	fun index(): Mono<String> {
//		val res = Mono.delay(Duration.ofSeconds(3))
//				.map { "index" }
//
//		return res;
//	}

	@GetMapping("/")
	fun index(model: Model): Mono<String> = mono {
		val view = Mono.delay(Duration.ofSeconds(2))
				.map { "index" }
				.awaitFirst()

		model.addAttribute("movies", ReactiveDataDriverContextVariable(movieService.getMovies()))

		// getMoviesWithGenerate is an infinite stream
		val movies = movieService.getMoviesWithGenerate("Super")
				.take(3)
				// transform the Flux<T> into a Mono<List<T>>
				.collectList()
				.awaitFirst()
		model.addAttribute("superMovies", movies);

		delay(Duration.ofSeconds(1))

		view;
	}
}