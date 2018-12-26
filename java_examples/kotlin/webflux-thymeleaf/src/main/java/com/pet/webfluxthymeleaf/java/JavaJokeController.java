package com.pet.webfluxthymeleaf.java;

import com.pet.webfluxthymeleaf.app.movie.Movie;
import com.pet.webfluxthymeleaf.app.movie.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/java")
@Controller
public class JavaJokeController {

	private final MovieService movieService;

	public JavaJokeController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping("/verybadjoke")
	public String veryBadJoke(Model model) {

		model.addAttribute("firstSourceTitle", "Movies from Java - only the 1st 10. Blocking!")
			.addAttribute("secondSourceTitle", "Movies from Java - all. Blocking!");

		List<Movie> movies = this.movieService.getMoviesFromDb(10L)
			.collectList()
			.block();
		model.addAttribute("movies", movies);

		movies = this.movieService.getMoviesFromDb(null).collectList().block();
		model.addAttribute("superMovies", movies);

		return "index";
	}

	@GetMapping("/joke")
	public Mono<String> joke(Model model) {

		model.addAttribute("firstSourceTitle", "Movies from Java - only the 1st 10. Non blocking.")
			.addAttribute("secondSourceTitle", "Movies from Java - all. Non blocking.");

		Mono<String> viewMono = this.movieService.getMoviesFromDb(10L)
			.collectList()
			.flatMap(movies -> {
				model.addAttribute("movies", movies);
				return this.movieService.getMoviesFromDb(null)
					.collectList()
					.flatMap(movies2 -> {
						model.addAttribute("superMovies", movies2);
						return Mono.just("index");
					});
			});

		return viewMono;
	}
}
