package com.pet.webfluxthymeleaf.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("movie")
class MovieConfig {
	lateinit var name: String
	var length: Int = 0
}