package hu.pet.utils

import hu.pet.utils.core.CoreService
import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class BookService(private val logger: Logger, private val coreService: CoreService) {

	fun printBook(): Unit = logger.info("This is my name: ${this.javaClass.name}")

	fun printMagic(): Unit = coreService.doFantasticThing()
}