package com.pet.webfluxthymeleaf.app.movie

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface MovieRepository : ReactiveMongoRepository<Movie, Int>