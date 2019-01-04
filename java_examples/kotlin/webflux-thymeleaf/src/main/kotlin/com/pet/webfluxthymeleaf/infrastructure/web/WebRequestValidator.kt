package com.pet.webfluxthymeleaf.infrastructure.web

import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import javax.validation.Validator

class WebRequestValidator(val validator: Validator) {

	/**
	 * Maps the json body to the provided generic type and performs bean validation. Throws a BadRequestException in case of validation error.
	 * @exception BadRequestException when bean validation error occurs or when the content-type is not application/json.
	 */
	@Throws(BadRequestException::class)
	inline fun <reified T> bodyToMono(request: ServerRequest): Mono<T> {
		// TODO: check the Content-Type and if it is not "application/json" throw BadRequestException
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