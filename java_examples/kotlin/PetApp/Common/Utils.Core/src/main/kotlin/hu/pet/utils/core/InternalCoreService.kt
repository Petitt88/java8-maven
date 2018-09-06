package hu.pet.utils.core

import org.slf4j.Logger

internal class InternalCoreService(private val logger: Logger) {
	fun doFantasticThing(): Unit = logger.info("Magic is happening right now...")
}