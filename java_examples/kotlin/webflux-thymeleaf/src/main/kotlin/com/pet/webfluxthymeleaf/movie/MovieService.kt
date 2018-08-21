package com.pet.webfluxthymeleaf.movie

import kotlinx.coroutines.experimental.reactive.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.*

@Service
class MovieService(private val mr: MovieRepository, private val webClient: WebClient) {

	fun getMoviesFromDb(count: Long? = null) = if (count == null) mr.findAll() else mr.findAll().take(count)

	fun getMoviesFix(namePrefix: String = ""): Flux<Movie> = Flux.just(*(1..10).map { this.createMovie(it, "${namePrefix}Movie$it") }.toTypedArray())

	/**
	 * Generates movies in an endless stream.
	 */
	fun getMoviesWithGenerate(namePrefix: String = ""): Flux<Movie> = Flux
		.generate { sink: SynchronousSink<Movie> -> sink.next(this.createMovie(Date().time.toInt(), "${namePrefix}Movie")) }
		.delayElements(Duration.ofSeconds(1L))

	fun getMoviesWithGenerate2(namePrefix: String = ""): Flux<Movie> = Flux
		.generate<Movie> { it.next(this.createMovie(Date().time.toInt(), "${namePrefix}Movie")) }
		// delay each new element by 1 second
		.delayElements(Duration.ofSeconds(1L))

	fun createMovie(movie: Movie) = mr.save(movie)

	suspend fun getMovie(id: Int): Movie {
		val movie = webClient
			.get()
			.uri("/movies/${id}")
			.retrieve()
			.bodyToMono(Movie::class.java)
			.awaitSingle()

		return movie
	}

	fun getMovie2(id: Int): Mono<Movie> = webClient
		.get()
		.uri("/movies/${id}")
		.retrieve()
		.bodyToMono(Movie::class.java)

	private fun createMovie(id: Int, name: String) = Movie(id, name)
}