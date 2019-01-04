package com.pet.webfluxthymeleaf.infrastructure.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.EncoderHttpMessageWriter
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.validation.FieldError
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * By implementing {@see ErrorWebExceptionHandler} webflux invokes this in case of any unhandled request handler (controller) exception,
 * just like @RestControllerAdvice or @ControllerAdvice.
 */
class WebErrorHandler(private val logger: Logger, mapper: ObjectMapper, viewResolvers: List<ViewResolver>) : ErrorWebExceptionHandler {

	private val messageWriters: List<HttpMessageWriter<*>>

	init {
		// messageWriter is empty. Use the EncoderHttpMessageWriter to delegate the task to an Encoder.
		messageWriters = listOf(EncoderHttpMessageWriter(Jackson2JsonEncoder(mapper)))
	}

	private val context: ServerResponse.Context by lazy {
		ServerResponseContext(messageWriters, viewResolvers)
	}

	override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {

		val status: HttpStatus = when (ex) {
			is BadRequestException -> HttpStatus.BAD_REQUEST
			is WebExchangeBindException -> HttpStatus.BAD_REQUEST
			is ForbiddenException -> HttpStatus.FORBIDDEN
			is NotFoundException -> HttpStatus.NOT_FOUND
			else -> {
				this.logger.error("Unhandled exception", ex)
				HttpStatus.INTERNAL_SERVER_ERROR
			}
		}

		val errorMessage: String? = when (ex) {
			is WebExchangeBindException -> ex.bindingResult.allErrors.joinToString {
				"On the '${it.objectName}' parameter the '${if (it is FieldError) it.field else ""}' field ${it.defaultMessage}."
			}
			else -> ex.message
		}

		val errorObject = object {
			val message = errorMessage
		}

		return ServerResponse.status(status)
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.syncBody(errorObject)
			.flatMap {
				it.writeTo(exchange, context)
			}
	}

	private class ServerResponseContext(
		private val messageWriters: List<HttpMessageWriter<*>>,
		private val viewResolvers: List<ViewResolver>
	) : ServerResponse.Context {
		override fun messageWriters(): List<HttpMessageWriter<*>> = messageWriters

		override fun viewResolvers(): List<ViewResolver> = viewResolvers
	}
}