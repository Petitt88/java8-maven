package com.pet.webfluxthymeleaf

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.webfluxthymeleaf.movie.Movie
import com.pet.webfluxthymeleaf.movie.MovieRepository
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.reactive.awaitFirstOrDefault
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@SpringBootApplication
class WebfluxThymeleafApplication {

	@Bean
	fun runner(mr: MovieRepository) = ApplicationRunner {
		GlobalScope.launch(Dispatchers.Unconfined) {

			val count = mr.count().awaitFirst()

			println("Deleting $count elements...")
			mr.deleteAll().awaitFirstOrDefault(null)

			val movies = (1..1000).map { Movie(it, "${it}__Movie__${LocalDateTime.now()}") }
			println("Inserting 1000 elements...")

			mr.saveAll(movies)
				// without this awaitFirst will fire as soon as the 1st item is inserted. We need all data in the database at the next statement
				.collectList()
				.awaitFirst()

			mr.findAll().subscribe(::println)
		}

//		mr.count().subscribe {
//			println(it)
//			mr.deleteAll().subscribe {
//				mr.saveAll((1..1000).map { Movie(it, "${it}__Movie__${LocalDateTime.now()}") }).subscribe {
//					mr.findAll().subscribe(::println)
//				}
//			}
//		}
	}

	@Bean
	fun webClient() = WebClient.create("http://localhost:8081")

	@Bean
	fun mapper() = ObjectMapper()
}

fun main(args: Array<String>) {
	runApplication<WebfluxThymeleafApplication>(*args)

//	SpringApplicationBuilder()
//		.initializers(beans {
//			bean {
//				SpringTransactionManager(ref())
//			}
//		})
//		.sources(WebfluxThymeleafApplication::class.java)
//		.build(*args)
}

//val myBeans = beans {
//	bean<UserHandler>()
//	bean<Routes>()
//	bean<WebHandler>("webHandler") {
//		RouterFunctions.toWebHandler(
//			ref<Routes>().router(),
//			HandlerStrategies.builder().viewResolver(ref()).build()
//		)
//	}
//	bean("messageSource") {
//		ReloadableResourceBundleMessageSource().apply {
//			setBasename("messages")
//			setDefaultEncoding("UTF-8")
//		}
//	}
//	bean {
//		val prefix = "classpath:/templates/"
//		val suffix = ".mustache"
//		val loader = MustacheResourceTemplateLoader(prefix, suffix)
//		MustacheViewResolver(Mustache.compiler().withLoader(loader)).apply {
//			setPrefix(prefix)
//			setSuffix(suffix)
//		}
//	}
//	profile("foo") {
//		bean<Foo>()
//	}
//}
