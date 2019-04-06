package hu.pet.coroutine

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineApplication

val logger = LoggerFactory.getLogger(CoroutineApplication::class.java)

fun main(args: Array<String>) {
	runApplication<CoroutineApplication>(*args)

	runBlocking {
		val job = launch {
			isActive
		}
		// if not joined, Job will be executed right after the 1st suspending function's suspend
		// or at the latest when runBlocking is finishing
		//job.join()

		val deferred = async {
			5
		}
		// if not awaited, Deferred will be executed right after the 1st suspending function's suspend
		// or at the latest when runBlocking is finishing
		//val numResult = deferred.await()

		// because the work is not a suspending function will not be awaited here
		// however won't get started right away, because coroutine builders by default all use the same thread as their parent
		// so this will only start when the current execution is suspended by a suspending function
		this.doThis()

		logger.info("Starting background works, thread: ${Thread.currentThread().id}")
		// workNotAwaited is not suspended --> doesn't get awaited
		// and starts its work on a dedicated thread --> starts right after "launch" method in it
		// does not use GlobalScope --> runBlocking will wait for it
		workNotAwaited(5000)
		// workAwaited is suspended --> will be awaited, main execution suspends here
		workAwaited(1000)

		// workAsync is not awaited here
		val def = workAsync(1000)
		// it is awaited here instead
		def.await()
//		val child = launch {
//			throw IOException("help")
//		}

		// inherits the scope of its caller
		coroutineScope {
		}
		logger.info("This is the end of {runBlocking}, thread: ${Thread.currentThread().id}")
//		try {
//			child.join()
//		} catch (e: CancellationException) {
//			logger.info("Error: ${e.message}")
//		}
	}
	logger.info("Finished, thread: ${Thread.currentThread().id}")
}

fun CoroutineScope.doThis() {
	val job = launch {
		logger.info("Hello from not suspending function, thread: ${Thread.currentThread().id}")
	}
	//job.join()
}

fun CoroutineScope.workNotAwaited(duration: Long) {
	launch(Dispatchers.Default) {
		logger.info("Work -from not suspended- starting, thread: ${Thread.currentThread().id}")
		delay(duration)
		logger.info("Work -from not suspended- $duration done, thread: ${Thread.currentThread().id}")
	}
}

suspend fun workAwaited(duration: Long): Int = withContext(Dispatchers.Default) {
	logger.info("Work -suspended- starting, thread: ${Thread.currentThread().id}")
	delay(duration)
	// Thread.sleep(1000)
	logger.info("Work -suspended- $duration done, thread: ${Thread.currentThread().id}")
	100
}

/**
 * By convention functions returning Deferred<T> shall be named with "Async" postfix.
 */
fun CoroutineScope.workAsync(duration: Long): Deferred<Int> {
	return async {
		delay(duration)
		5
	}
}

