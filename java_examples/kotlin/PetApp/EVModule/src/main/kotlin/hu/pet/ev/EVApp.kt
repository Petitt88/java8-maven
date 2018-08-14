package hu.pet.ev

import hu.pet.utils.core.CoreService
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
	println("EV module starting...")

	CoreService(LoggerFactory.getLogger(CoreService::class.java)).doFantasticThing()

	println("EV module finished!")
}