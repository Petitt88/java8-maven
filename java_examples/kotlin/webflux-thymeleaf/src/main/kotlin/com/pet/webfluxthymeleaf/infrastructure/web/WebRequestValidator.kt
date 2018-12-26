package com.pet.webfluxthymeleaf.infrastructure.web

import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import javax.validation.Validator

class WebRequestValidator(val validator: Validator) {

	inline fun <reified T> bodyToMono(request: ServerRequest): Mono<T> {
		return request.bodyToMono(T::class.java)
			.doOnSuccess {
				val errors = validator.validate(it)
				if (errors.any()) {
					val errorsMessage = errors.map { "${it.propertyPath}: ${it.message}" }.joinToString()
					failBadRequest(errorsMessage)
				}
			}
	}
}