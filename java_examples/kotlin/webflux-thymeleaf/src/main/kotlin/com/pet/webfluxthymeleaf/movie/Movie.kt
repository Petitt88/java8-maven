package com.pet.webfluxthymeleaf.movie

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class Movie(@NotNull val id: Int?,
                 @NotNull @Size(min = 3) val name: String?) {
	constructor() : this(null, null)
}