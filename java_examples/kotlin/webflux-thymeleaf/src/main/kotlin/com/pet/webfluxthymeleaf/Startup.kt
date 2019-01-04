package com.pet.webfluxthymeleaf

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.pet.webfluxthymeleaf.app.HomeController
import com.pet.webfluxthymeleaf.app.movie.Movie
import com.pet.webfluxthymeleaf.app.movie.MovieRepository
import com.pet.webfluxthymeleaf.app.movie.MovieService
import com.pet.webfluxthymeleaf.infrastructure.web.WebErrorHandler
import com.pet.webfluxthymeleaf.infrastructure.web.WebRequestValidator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.context.support.beans
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.router
import java.time.LocalDateTime

class Startup {
	companion object {
		val initializer: BeanDefinitionDsl by lazy {
			beans {
				bean {
					router(Router.homeRoutes(ref()))
						//.and(Router.starRoutes(ref())) - this is how to chain routes for multiple handlers
				}
				// integrate Exposed with Spring's transaction manager. Thus Database.connect(dataSource) is no more necessary and
				// transaction {} blocks control commit explicitly.
//				bean<PlatformTransactionManager> {
//					// a necessary bean to make Exposed work with Spring's transaction management.
//					// instead of @Transactional one can use Exposed's transaction { } block with explicit commit.
//					// Without the commit statement the transaction gets rolled back automatically at the end of the block.
//					SpringTransactionManager(ref())
//				}
				bean {
					// security config
					Security.securityWebFilterChain(ref())
				}
				// global error handler for those exceptions that occur in the request handler (controller)
				bean<WebErrorHandler>()
				// WebRequestValidator is to be used when a json request body needs to be mapped to a class and perform a bean validation. BadRequestException is thrown in case of validation error.
				bean<WebRequestValidator>()
				bean<MovieService>()
				bean<HomeController>()
				bean("messageSource") {
					ReloadableResourceBundleMessageSource().apply {
						setBasename("messages")
						setDefaultEncoding("UTF-8")
					}
				}
				bean {
					DatabaseHelper.seed(ref())
				}
				bean {
					WebClient.builder()
						.baseUrl("http://localhost:8081")
						.build()
				}
				bean(scope = BeanDefinitionDsl.Scope.PROTOTYPE) {
					Jackson2ObjectMapperBuilder()
						//.indentOutput(false)
						//.serializationInclusion(JsonInclude.Include.NON_NULL)
						.modules(JavaTimeModule())
						.featuresToDisable(
							SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
							SerializationFeature.FAIL_ON_EMPTY_BEANS
						)
						.featuresToEnable(
							SerializationFeature.WRITE_DATES_WITH_ZONE_ID,
							JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
							MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES
						)
						.build<ObjectMapper>()
				}
				profile("dev") {
					//bean<BeanForDemoProfile>()
				}
			}
		}
	}
}

private class Security {
	companion object {
		fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
			.authorizeExchange()
			//.pathMatchers(HttpMethod.GET,"/movies/**").permitAll()
			.anyExchange().permitAll()
			.and()
			.build()
	}
}

private class DatabaseHelper {
	companion object {
		fun seed(mr: MovieRepository): ApplicationRunner = ApplicationRunner {
			GlobalScope.launch {

				val count = mr.count().awaitFirst()

				logger.info("Deleting $count elements...")
				mr.deleteAll().awaitFirstOrDefault(null)

				val elementsToInsert = 1000

				val movies = (1..elementsToInsert).map { Movie(it, "${it}__Movie__${LocalDateTime.now()}") }
				logger.info("Inserting $elementsToInsert elements...")

				mr.saveAll(movies)
					// without this awaitFirst will fire as soon as the 1st item is inserted. We need all data in the database at the next statement
					.collectList()
					.doOnSuccess {
						logger.info("Done inserting $elementsToInsert elements")
					}
					.awaitFirst()

				mr.count().doOnSuccess { logger.info("Current number of movies in the database: $it") }.subscribe()
			}
		}
	}
}

private class Router {
	companion object {
		fun homeRoutes(handler: HomeController): (RouterFunctionDsl.() -> Unit) = {
			"/".nest {
				GET("", handler::index)
				GET("/push", handler::push)
				GET("/long", handler::longRunningSimulation)
				GET("/file", handler::files)
				POST("/", handler::createMovie)
				GET("/movies", handler::getMovies)
				GET("/movies/{id}", handler::getMovie)
			}
		}
	}
}

private val logger: Logger = LoggerFactory.getLogger(Startup::class.java)