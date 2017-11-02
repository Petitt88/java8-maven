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
			// launch has no result, returns a job that can be waited in a coroutine context by ".join()" which is a suspend function
			jobs += launch {
				val res = myAsyncFunc()
			}

			// coroutine builder
			// async has result, ".await()" can be used to retrieve it (it is suspending just like join)
			jobs += async {
				val res = myAsyncFunc()
				// will be the result of the async builder
				res
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
			val seq = buildSequence {
				yield(1)

				yield(2)
			}
			for (i in seq.take(10)) {

			}

			// coroutine builder
			// returns a CompletableFuture
			val fut = future {
				val c1 = CompletableFuture<Int>()
				val res = c1.await()
				// will be the result of the future builder
				res
			}

			// coroutine builder
			// returns a mono
			val mon = mono {

			}

			// coroutine builder
			// returns a flux
			val flu = flux<Int>(CommonPool) {

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