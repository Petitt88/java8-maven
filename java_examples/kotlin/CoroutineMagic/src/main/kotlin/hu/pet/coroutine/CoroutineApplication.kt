package hu.pet.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.flow
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

		// parallel execution
		val works = (1..10).map {
			async { computeNameAsync(3000) }
		}
		works.forEach { it.await() }

		// workAsync is not awaited here
		val def = workAsync(1000)
		// it is awaited here instead
		def.await()
//		val child = launch {
//			throw IOException("help")
//		}
		// name computation is awaited
		val name = computeNameAsync(2000)

		// hot stream: inside block starts immediately, running concurrently with the main code
		// Channels, just like futures, are synchronization primitives. You shall use a channel when you need to send data from one coroutine to
		// another coroutine in the same or in a different process, because different coroutines are concurrent and you need synchronization to
		// work with any data in presence of concurrency⁷.
		val produceInstance = produce<Int> {
			while (!isClosedForSend) {
				delay(1000)
				send(2)
			}
		}

//		if(true){
//			return@runBlocking
//			// Oops! Leaked values: Channel is still running!
//		}

		produceInstance.cancel()
		// cold stream: inside block does not get executed until somebody invokes .collect() on the Flow<Int> instance.
		// Unlike channels, flows do not inherently involve any concurrency. They are non-blocking, yet sequential.
		val flowInstance = flow<Int> { emit(3) }

		logger.info("This is the end of {runBlocking}, thread: ${Thread.currentThread().id}")
//		try {
//			child.join()
//		} catch (e: CancellationException) {
//			logger.info("Error: ${e.message}")
//		}

		// this is how we can create our custom scopes
		val scope = MainScope()
		val scope2 = CoroutineScope(Dispatchers.Default)
		scope.async { 2 }
		scope2.launch { }
		scope2.cancel()

		/*
		withContext(Dispatchers.XXX) {...} vs coroutineScope {...}
		- both creates new child scope and attaches it to the parent
			- If an exception is raised in the parent or parent gets just cancelled (job.cancel()) all the children are cancelled
			- If an exception is raised in one of the children - the parent and all other children gets cancelled
			- If a child gets cancelled it does not affect the other children nor the parent
		- coroutineScope: creates new CoroutineScope and calls the specified suspend block with this scope. The provided scope inherits its coroutineContext from the outer scope, but overrides context's Job.
		- withContext: Calls the specified suspending block with a given coroutine context (via Dispatchers), suspends until it completes, and returns the result.
		 */
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
fun CoroutineScope.workAsync(duration: Long): Deferred<Int> = async {
	delay(duration)
	5
}

/**
 * "coroutineScope" builder inherits the scope of the current coroutine and it becomes its "father".
 * The main difference between "runBlocking" and "coroutineScope" builder is that the latter:
 * - is suspending function
 *  --> does not block the current thread while awaiting for its execution to complete
 *      - its execution is complete when all children launched inside get complete
 *  --> can be invoked from another suspending function
 */
suspend fun computeNameAsync(duration: Long): String = coroutineScope {
	// because coroutineScope is suspending and uses structured concurrency, it will complete only when this launch block completes
	// however the execution goes forward immediately after invoking "launch" because it is NOT a suspending function - same goes for async
	launch {
		logger.info("Starting delay in launch of \"computeNameAsync\"")
		delay(duration * 4)
		logger.info("Delay finished in launch \"computeNameAsync\"")
	}

	val name = async {
		logger.info("Starting delay in async of \"computeNameAsync\"")
		delay(duration * 1)
		logger.info("Delay finished in async \"computeNameAsync\"")
		"Peter Kongyik"
	}.await()
	name
}

/**
 * Caller decides when to await this.
 */
 fun CoroutineScope.computeNameAsync2(duration: Long): Deferred<String> = async {
	launch {
		logger.info("Starting delay in launch of \"computeNameAsync\"")
		delay(duration * 4)
		logger.info("Delay finished in launch \"computeNameAsync\"")
	}

	val name = async {
		logger.info("Starting delay in async of \"computeNameAsync\"")
		delay(duration * 1)
		logger.info("Delay finished in async \"computeNameAsync\"")
		"Peter Kongyik"
	}.await()
	name
}

/**
 * Same as @see computeNameAsync regarding the await: caller awaits it right off the bat.
 * However this uses a different context with a different thread (executes on the default dispatcher).
 */
suspend fun computeNameAsync3(duration: Long): String = withContext(Dispatchers.Default){
	// because coroutineScope is suspending and uses structured concurrency, it will complete only when this launch block completes
	// however the execution goes forward immediately after invoking "launch" because it is NOT a suspending function - same goes for async
	launch {
		logger.info("Starting delay in launch of \"computeNameAsync\"")
		delay(duration * 4)
		logger.info("Delay finished in launch \"computeNameAsync\"")
	}

	val name = async {
		logger.info("Starting delay in async of \"computeNameAsync\"")
		delay(duration * 1)
		logger.info("Delay finished in async \"computeNameAsync\"")
		"Peter Kongyik"
	}.await()
	name
}
