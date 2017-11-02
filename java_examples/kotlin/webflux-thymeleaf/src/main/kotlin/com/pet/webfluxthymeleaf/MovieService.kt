package com.pet.webfluxthymeleaf

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import java.util.*

@Service
class MovieService {

	fun getMovies(namePrefix: String = ""): Flux<Movie> = Flux.just(*(1..10).map { this.creaetMovie(it, "${namePrefix}Movie$it") }.toTypedArray())

	fun getMoviesWithGenerate(namePrefix: String = ""): Flux<Movie> = Flux
			.generate{ sink: SynchronousSink<Movie> -> sink.next(this.creaetMovie(Date().time.toInt(),"${namePrefix}Movie" ))}
			.delayElements(Duration.ofSeconds(1L))

	private fun creaetMovie(id: Int, name: String) = Movie(id, name)
}