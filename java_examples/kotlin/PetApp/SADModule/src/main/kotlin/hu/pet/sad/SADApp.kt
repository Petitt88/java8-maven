package hu.pet.sad

import hu.pet.utils.BookService
import hu.pet.utils.core.CoreService
import org.slf4j.LoggerFactory

fun main() {
	println("SAD module starting...")

	val coreService = CoreService(LoggerFactory.getLogger(CoreService::class.java))
	BookService(LoggerFactory.getLogger(BookService::class.java), coreService).also {
		it.printBook()
		it.printMagic()
	}

	coreService.doFantasticThing()

	println("SAD module finished!")
}