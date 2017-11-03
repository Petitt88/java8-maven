package com.pet.moviesapp.movie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/")
@RestController
public class MovieController {


	@GetMapping("/movies")
	public Collection<Movie> movies() {
		List<Movie> movies = Stream
				.iterate(0, i -> i + 1)
				.map(a -> new Movie(a, UUID.randomUUID().toString()))
				.limit(10)
				.collect(Collectors.toList());

		return movies;
	}
}
