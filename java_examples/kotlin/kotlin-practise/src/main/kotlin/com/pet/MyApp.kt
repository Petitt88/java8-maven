@file:JvmName("Foo")

package com.pet

import kotlinx.coroutines.experimental.*
import java.lang.Integer.parseInt
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

fun main(args: Array<String>) {

	result(args)

	runBlocking {
		coroutinesAreCheap()
	}

	println("Hello Workld!")

	var res = arrayOf<String?>()
	var seq = if (true) res.asSequence() else sequenceOf(1, 2)
	var seq2 = res?.asSequence() ?: sequenceOf(1, 2)

	for (item in listOf("Peter", "Noni", null)) {
		item?.let {
			res = res.plus(it)
			// the same as above
			res += it
			// "return <value>" cannot be used in lambda, only "return". But without a label that would return from the whole "main" function
			// --> if this would be an anonymous class then this returned only from the "let" block

			// to return only from the let block
			//return@let

			// last statement --> implicit return, or just use the "return" keyword
			it
		}
				?.run {
					if (this is String) {

					}
					res += this
					this
				}
				?.also { println(it) }
	}

	// anonymous class
	val th = object : Thread() {
		override fun run() {
			super.run()
		}

		fun alma() = "alma"
	}
	th.isDaemon = true
	th.alma()

	val cons0: Consumer<Int> = Consumer(::println)
	val con1 = Consumer<Int> { num -> println(num) }
	val cons2 = Consumer<Int> { println(it) }
	val cons3 = Consumer<Int>(::println)
	val kotCons = { par: Int -> println(par) }
	val kotBiCons = { par1: Int, par2: Int -> println("$par1-$par2") }
	val kotBiCons2: (Int, Int) -> Unit = { par1: Int, par2: Int -> println("$par1-$par2") }

	cons0.accept(0);
	cons3.accept(3)
	kotCons(1)
	kotBiCons(1, 2)
	kotBiCons2(1, 2)

	SingletonObject.test()

	callFunc(::main)
	callFunc2(::main)

	val seqGen = generateSequence(1) { it * 10 }
	seqGen.take(10).forEach(::println)

	val myMap = mutableMapOf("Pet" to 1, "Noni" to 2, Pair("Flori", 3))
	// operator fun get(key: K)
	val value = myMap["Pet"] ?: -1;
	// operator fun <K, V> MutableMap<K, V>.set(key: K, value: V)
	myMap["Pet"] = value * 2

	for (entry in myMap.entries) {
		println("$entry.key-$entry.value")
	}
}

fun mySum(a: Int, b: Int): String {
	return "ysdysd $a $b ${a + b}"
}

fun printProduct(arg1: String?, arg2: String?) {

	val str = arg1?.substring(1) ?: "alma"
	arg2?.let { println(it) }
	arg2?.let { x -> println(x) }

	val x = parseInt(arg1)
	val y = parseInt(arg2)


	// Using `x * y` yields error because they may hold nulls.
	if (arg1 != null && arg2 != null) {


		// x and y are automatically cast to non-nullable after null check
		println(x * y)
		println(x or y)
	} else {
		println("either '$arg1' or '$arg2' is not a number")
	}
}

fun collectionsSample() {
	val items = listOf("1", "2");

	when {
		"orange" in items -> println("juicy")
		"apple" in items -> println("apple is fine too")
	}

	if ("1" in items) {
		println("it is in!")
	}
}

fun tryWithResources() {
	var p = if (true) 0 else 1

	val stream = Files.newInputStream(Paths.get("/some/file.txt"))
	//stream.buffered().use { }
	stream.buffered().reader().use { reader ->
		println(reader.readText())
	}
}

fun arraySample() {
	val arr = arrayOf("Peter", "Dóri")

	for (item in arr) {

	}
	//System::class.java
	for ((index, item) in arr.withIndex()) {

	}

	val list = mutableListOf<Int>(10, 20, 30)
	list.swap(1, 2);

	arrayOf(1, 2, 3).groupBy { }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
	val tmp = this[index1] // 'this' corresponds to the list
	this[index1] = this[index2]
	this[index2] = tmp
}

open class Foo {
	open fun f() {
		println("Foo.f()")

		println(lazyValue);

		//Customer::getEmailPlusName
		3.let {
			// here this points to "Foo"
			this.f()
			// return 3 from this lambda
			3
		}.let(::println)
		3.apply {
			// here this points to the variable the "apply" function is invoked on (3)
			this@Foo.f()

			// because this is a lambda, would return from the entire f() function
			// return   - however in case of anonymous function it only return from the anonymous function
		}
		with(3) {
			// here this points to the parameter (3)
		}

		synchronized(this) {

		}

		// Any is like Object in Java: Any is the superclass of anything
		val any: Any;

		listOf(1, 2, 3, 4, 5).map({ a -> println(a) })
		listOf(1, 2, 3, 4, 5).map({ println(it) })
		listOf(1, 2, 3, 4, 5).map { println(it) }
	}

	open val x: Int get() = 1

	lateinit var name: String;
}

fun coroutineExample(): Unit {
	runBlocking {

	}

	println("Start")

	// Start a coroutine
	launch(CommonPool) {
		delay(1000)
		println("Hello")
	}

	Thread.sleep(2000) // wait for 2 seconds
	println("Stop")
}

suspend fun coroutinesAreCheap() {
//	// slow as hell, starting 1.000.000 threads
//	val c = AtomicInteger()
//	for (i in 1..1_000_000)
//		thread(start = true) {
//			c.addAndGet(i)
//		}
//	println(c.get())

	// same as above, but with coroutines
	// This example completes in less than a second for me --> fast as hell
	val c = AtomicInteger()

	for (i in 1..1_000_000)
		launch(CommonPool) {
			c.addAndGet(i)
		}
	println(c.get())

	val deferred = (1..1_000_000).map { n ->
		async(CommonPool) {
			delay(1000) // make sure they run in parallel
			n
		}
	}
	val sum = deferred.sumBy { it.await() }

	// functional interface
	val runnable = Runnable {
		println("this runs in the Runable.")
	};
	runnable.run();
}

internal fun mic() {
	TestData(1).prop1 and TestData(1).prop1
	mapOf(1 to "Pet", 2 to "Flori")

	val nullableList: List<Int?> = listOf(1, 2, null, 4)
	val intList: List<Int> = nullableList.filterNotNull()

	val f: () -> Unit = { Thread.sleep(10) }
	val g: (x: Int, y: Int) -> Unit = { x, y -> Thread.sleep(10) }
	f()
	g(1, 0)

	sum(1, 2)
	1.sumReceiver(2)
}

private fun enumTest() {
	val dir = Direction.EAST
	val color = Color.BLUE
	println(color.rgb)
}

// equals gets overridden for data classes so it checks whether all properties are equal instead of reference equality
data class Customer(val name: String, val email: String) {
	val getEmailPlusName get() = "$name and $email"
}

public var bar: Int = 4;

data class TestData(val prop1: Int)

// primary constructor
class Person(age: Int) {

	// secondary constructor
	constructor(age: Int, name: String) : this(age) {
		this.name = name;
	}

	var name: String = ""
	var age: Int

	val sum: Int
		get() {
			return this.age
		}
	val sum2: Int get() = this.age

	// initializer for primary constructor
	init {
		this.age = age
	}
}

object SingletonObject {

	fun test() {
		var a = 1 and 0
		var b = 1.or(0)

		//setOf(1, 2, 3).all { }
		val test = TestJava();
		// Any! means Any or Any?
		test.tester(null)
		println(test.obj)
		test.testerNotNull("asdasd")
		test.testerNotNull(null)
		//3.javaClass
		val now = LocalDateTime.now()

		`Find all users on JSON REST endpoint`()

		User.create("Pet", 28)
	}
}

val lazyValue: String by lazy(LazyThreadSafetyMode.PUBLICATION, {
	println("computed!")
	"Hello"
})

class User(val map: Map<String, Any?>) {
	val name: String by map
	val age: Int     by map

	// delegated property
	val age2: Int by lazy { 5 };

	companion object {
		fun create(name: String, age: Int) = User(emptyMap())
	}
}

annotation class Special(val why: String, val number: Int)

// Nothing return type means the method will throw
fun throwError(message: String): Nothing = throw NotImplementedError(message)

fun `Find all users on JSON REST endpoint`() {

}

val sum = { x: Int, y: Int -> x + y }
val sum2: (Int, Int) -> Int = { x, y -> x + y }
val sumReceiver = fun Int.(other: Int): Int = this + other

fun callFunc(func: (Array<String>) -> Unit) = func(arrayOf("1"))
fun callFunc2(func: (args: Array<String>) -> Unit) = func(arrayOf("1"))

val represents: String.(Int) -> Boolean = { other -> this.toIntOrNull() == other }

enum class Direction {
	NORTH, SOUTH, WEST, EAST
}

enum class Color(val rgb: Int) {
	RED(0xFF0000),
	GREEN(0x00FF00),
	BLUE(0x0000FF)
}

fun <T : Comparable<T>> sort(list: List<T>) {
}

fun <T> cloneWhenGreater(list: List<T>, threshold: T): List<T>
		where T : Comparable<T>,
		      T : Cloneable {
	return list.filter { it > threshold }.map { it }
}

fun List<String>.filterValid(): List<String> = emptyList()
// kotlin has type erasure just like java as well --> methods with the same name but different generic types clash --> need to specify different name for the jvm bytecode
@JvmName("filterValidInt")
fun List<Int>.filterValid(): List<Int> = emptyList()
// From Kotlin they will be accessible by the same name filterValid, but from Java it will be filterValid and filterValidInt.

// The same trick applies when we need to have a property x alongside with a function getX():
val x: Int
	@JvmName("getX_prop")
	get() = 15

fun getX() = 10

open class Car {
	open fun getNumber() = 3
}

class MegaCar : Car()

fun findEmails(users: List<String>, predicate: (String) -> Boolean) = users.filter(predicate)


fun argsExInvoke() {
	argsEx("ws", "sd", "2323")

	val arr = arrayOf("23", "sads")
	// * - array spread operator
	argsEx(*arr)

	val arr2 = arrayOf("1", "2", *arr, "3")
}

fun argsEx(vararg strings: String) {
}