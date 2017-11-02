package com.pet

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.buildSequence

fun main(args: Array<String>) {

	// coroutine builder
	val mySequence = buildSequence {
		// yields execution back to the caller
		yield(1)
		println("First went")
		yield(2)
		println("Second went")
	}
	for (i in mySequence) {
		println(i)
	}

	// "launch": has no result, returns a Job. Kinda like fire-and-forget
	// Job can be cancelled, waited by join()
	val job = launch { }
	//job.cancel()
	runBlocking(block = { job.join() })

	// Deferred inherits from job
	val deferreds = (1..1_000_000).map {
		// async: returning a value from a coroutine. As opposed to "launch", "async" has result that can be awaited (or joined)
		async {
			// this gets returned from the lambda
			it
		}
	}

	// deferreds can only be suspended in coroutine contexts
	// runBlocking: starts a coroutine context and blocks the current thread until the lambda keeps executing - can call suspending functions in it
	runBlocking {
		// await has result: the Int
		deferreds.sumBy { it.await() }
	}
	// at this point the deferreds are complete...
}

fun play2() {
	val numBackUpPairsByRegion = mapOf("Africa" to 4, "Antarctica" to 7, "Asia" to 3, "Australia" to 2, "Europe" to 2, "North America" to 3, "South America" to 4)
	val region = "Antarctica"
	val area = 14000000
	val numPantsPerPanda = 1 + numBackUpPairsByRegion[region] as Int

	// variable to function
	val howManyPants = fun(numPandas: Int) = numPandas + numPantsPerPanda
	// variable to function
	val howManyPants2 = fun(numPandas: Int) { numPandas + numPantsPerPanda }
	// variable to lambda
	val howManyPantsLambda = { numPandas: Int -> numPandas + numPantsPerPanda }
	val howManyPantsLambda2: (Int) -> Int = { it + numPantsPerPanda }

	// local function
	fun countPandas(area: Int): Int = if (area > 3000) area / 25 else area / 18
	// anonymous object
	val pantsCounter = object {
		fun count(numPandas: Int, convertPandasToPants: (Int) -> Int): Int = convertPandasToPants(numPandas)
	}
	println(pantsCounter.count(countPandas(area), howManyPants))

	println(pantsCounter::class.java.simpleName)

	val res = run {
		"return this"
		//return@run 22
	}
	// prints "return this" or 22
	println(res)
}

fun play(args: Array<String>) {
	val favNums = arrayOf(11, 38, 74, 15)
	println("${favNums.size}")

	for (i in 3 downTo 0) {
		println(i)
	}

	val list = mutableListOf(3, 4, 7, 2, 4, 9, 0, 1).subList(3, 4)

	// true
	println("aa" == "aa")
	// false
	println("aa" === "aa")

	val p1 = Person("Peter", 28)
	val p2 = Person("Peter", 28)
	// false
	println(p1 == p2)
	// false
	println(p1 === p2)

	val dp1 = DataPerson("Peter", 28)
	val dp2 = DataPerson("Peter", 28)
	// true - equals method is overridden by Kotlin for data classes and checks whether all properties are the same
	println(dp1 == dp2)
	// false - checks reference equality
	println(dp1 === dp2)
}

class Person(val name: String, val age: Int)
data class DataPerson(val name: String, val age: Int) {

	override fun toString(): String = if (this.age > 0) "$name-$age" else "${name.toString()}"
}

fun countPandas(area: Int): Int = if (area > 3000) area / 25 else area / 18