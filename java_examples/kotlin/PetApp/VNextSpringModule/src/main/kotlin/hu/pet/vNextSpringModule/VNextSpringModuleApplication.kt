package hu.pet.vNextSpringModule

import hu.pet.utils.BookService
import org.slf4j.Logger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("hu.pet.utils")
class VNextSpringModuleApplication {

	@Bean
	fun runner(buildProperties: BuildProperties?, logger: Logger, bookService: BookService) = CommandLineRunner {
		logger.info("Application version is: ${buildProperties?.let { "${buildProperties.group}-${buildProperties.artifact}: ${buildProperties.version}" } ?: "Development"}")
		bookService.printMagic()
	}
}

fun main(args: Array<String>) {
	runApplication<VNextSpringModuleApplication>(*args)
}
