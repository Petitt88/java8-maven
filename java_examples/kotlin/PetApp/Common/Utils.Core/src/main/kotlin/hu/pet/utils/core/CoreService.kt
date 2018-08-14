package hu.pet.utils.core

import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class CoreService(private val logger: Logger) {
	fun doFantasticThing(): Unit = logger.info("Magic is happening right now...")
}