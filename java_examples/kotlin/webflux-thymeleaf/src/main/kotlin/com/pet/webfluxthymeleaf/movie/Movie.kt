package com.pet.webfluxthymeleaf.movie

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Document
data class Movie(
	@Id @get:NotNull val id: Int?,
	@get:NotNull @get:Size(min = 3) val name: String?
) {
	constructor() : this(null, null)
}