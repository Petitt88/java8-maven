package com.pet.webfluxthymeleaf.infrastructure.web

class BadRequestException : Exception {
	constructor()
	constructor(message: String) : super(message)
}

class ForbiddenException : Exception()

class NotFoundException : Exception() {
}

fun failBadRequest(message: String? = null): Nothing =
		throw  if (message != null) BadRequestException(message) else BadRequestException()

fun failForbidden(): Nothing = throw ForbiddenException()

fun failNotFound(): Nothing = throw NotFoundException()