package hu.pet.VNextSpringModule

import hu.pet.utils.BookService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("hu.pet.utils")
class VNextSpringModuleApplication {

	@Bean
	fun runner(bookService: BookService) = CommandLineRunner {
		bookService.printMagic()
	}
}

fun main(args: Array<String>) {
	runApplication<VNextSpringModuleApplication>(*args)
}
