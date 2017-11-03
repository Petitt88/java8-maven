package com.pet.webfluxthymeleaf.movie

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.*

@Service
class MovieService(private val mr: MovieRepository) {

	fun getMoviesFromDb(count: Long? = null) = if (count == null) mr.findAll() else mr.findAll().take(count)

	fun getMoviesFix(namePrefix: String = ""): Flux<Movie>
			= Flux.just(*(1..10).map { this.createMovie(it, "${namePrefix}Movie$it") }.toTypedArray())

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

	private fun createMovie(id: Int, name: String) = Movie(id, name)
}