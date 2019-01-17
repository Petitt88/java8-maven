package com.pet.webfluxthymeleaf.java;

import com.pet.webfluxthymeleaf.app.movie.MovieService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class JavaJokeController {

	private final MovieService movieService;

	public JavaJokeController(MovieService movieService) {
		this.movieService = movieService;
	}

	public Mono<ServerResponse> veryBadJoke(ServerRequest request) {
		Map<String, Object> model = new HashMap<>();

		model.put("firstSourceTitle", "Movies from Java - only the 1st 10. Blocking!");
		model.put("secondSourceTitle", "Movies from Java - all. Blocking!");

		return this.movieService.getMoviesFromDb(10L)
			.collectList()
			.flatMap(movies -> {
				model.put("movies", movies);
				return this.movieService.getMoviesFromDb(null).collectList();
			})
			.flatMap(movies -> {
				model.put("superMovies", movies);
				return ServerResponse.ok().render("index", model);
			});
	}

	public Mono<ServerResponse> joke(ServerRequest request) {
		Map<String, Object> model = new HashMap<>();

		model.put("firstSourceTitle", "Movies from Java - only the 1st 10. Non blocking.");
		model.put("secondSourceTitle", "Movies from Java - all. Non blocking.");

		Mono<ServerResponse> viewMono = this.movieService.getMoviesFromDb(10L)
			.collectList()
			.flatMap(movies -> {
				model.put("movies", movies);
				return this.movieService.getMoviesFromDb(null).collectList()
					.flatMap(movies2 -> {
						model.put("superMovies", movies2);
						return ServerResponse.ok().render("index");
					});
			});
		return viewMono;
	}
}
