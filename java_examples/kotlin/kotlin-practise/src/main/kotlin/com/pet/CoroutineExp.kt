package com.pet

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.reactor.flux
import kotlinx.coroutines.experimental.reactor.mono
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.buildSequence

fun classInFunction() {

	class Message(val id: UUID) {
		private val myId = Message(UUID.randomUUID())

		fun asyncMessageMagic() {
			// to hold builders which return Job instances
			val jobs = mutableListOf<Job>()

			// coroutine builder
			jobs += launch {
				val res = myAsyncFunc()
			}

			// coroutine builder
			jobs += async {
				val res = myAsyncFunc()
			}

			// coroutine builder
			runBlocking(Unconfined) {


				jobs += launch(this.coroutineContext) {

				}
				delay(1000)
				val ass = async(CommonPool) {

				}
				ass.await()

				jobs.last().cancel()
				jobs.forEach { it.join() }
			}

			// coroutine builder
			val fib = buildSequence {
				yield(1)

				yield(2)
			}
			for (i in fib.take(10)) {

			}

			// coroutine builder
			future {
				val c1 = CompletableFuture<Int>()
				val res = c1.await()
			}

			// coroutine builder
			mono {

			}

			// coroutine builder
			flux<Int>(CommonPool) {

			}
		}
	}

	val mess = Message(UUID.randomUUID())
}

suspend fun myAsyncFunc(): Int {
	TODO("Not implemented yet")
}

// The startCoroutine2 is defined in as an extension for suspending function type.
fun <T> (suspend () -> T).startCoroutine2(completion: Continuation<T>) {

}