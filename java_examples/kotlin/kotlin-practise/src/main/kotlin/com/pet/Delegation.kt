package com.pet

import kotlinx.coroutines.experimental.*
import java.util.*
import kotlin.coroutines.experimental.Continuation
import kotlin.reflect.KProperty

class Example {
	var p: String by Delegate()
	val p2: Int by Delegate2 { 3 }


	fun classInFunction() {
		class Message(val id: UUID) {
			private val myId = Message(UUID.randomUUID())

			fun asyncMessageMagic() {
				runBlocking(Unconfined) {
					val jobs = mutableListOf<Job>()

					jobs += launch(this.coroutineContext) {

					}
					delay(1000)
					jobs += async(CommonPool) {

					}
					val res = myAsyncFunc()

					jobs.forEach { it.join() }
				}
			}
		}

		val mess = Message(UUID.randomUUID())
	}

}

class Delegate {
	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return "$thisRef, thank you for delegating '${property.name}' to me!"
	}

	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
		println("$value has been assigned to '${property.name} in $thisRef.'")
	}
}

class Delegate2<out T : Int>(val initializer: () -> T) {
	val value: T
		get() = this.initializer()
	//set(value) = { field = value }

	//operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
}

private inline operator fun <T : Int> Delegate2<T>.getValue(thisRef: Any?, property: KProperty<*>): Int = value

suspend fun myAsyncFunc(): Int {
	TODO("Not implemented yet")
}

// The startCoroutine2 is defined in as an extension for suspending function type.
fun <T> (suspend () -> T).startCoroutine2(completion: Continuation<T>) {

}