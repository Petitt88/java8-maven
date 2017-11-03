package com.pet.webfluxthymeleaf.movie

data class Movie(val id: Int, val name: String) {
	constructor() : this(0, "")
}