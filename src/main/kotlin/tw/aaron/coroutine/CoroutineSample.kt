package tw.aaron.coroutine

import kotlinx.coroutines.*
import org.jetbrains.annotations.BlockingExecutor
import java.util.concurrent.CompletionService
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors
import kotlin.time.measureTime


val Dispatchers.LOOM: @BlockingExecutor CoroutineDispatcher
    get() = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()


fun blockingHttpCall(){
    Thread.sleep(100)
}

suspend fun nonBlockingIO(){
    delay(100)
}

suspend fun sample1()  {
    measureTime {
        supervisorScope {
            repeat(100_000) {
                launch(Dispatchers.IO) {
                    blockingHttpCall()
                }
            }
        }
    }.also { println("BlockingIO Dispatcher.io completed, time: $it") }
}


fun sample2()  {

    val numberOfTasks: Int = 100000

    val executorService = Executors.newFixedThreadPool(64)

    val startTime = System.currentTimeMillis()

    val completionService: CompletionService<Void?> = ExecutorCompletionService(executorService)

    for (i in 0 until numberOfTasks) {
        completionService.submit {
            blockingHttpCall()
            null
        }
    }

    for (i in 0 until numberOfTasks) {
        completionService.take().get()
    }

    val endTime = System.currentTimeMillis()
    val elapsedTime = endTime - startTime

    val minutes = (elapsedTime / 1000 / 60).toString().padStart(2, '0')
    val seconds = ((elapsedTime / 1000) % 60).toString().padStart(2, '0')

    val formattedTime = "$minutes m $seconds s"


    println("BlockingIO Java threads completed, time: $formattedTime")

    executorService.shutdown()

}

suspend fun sample3()  {

    measureTime {
        supervisorScope {
            repeat(100_000) {
                launch(Dispatchers.IO) {
                    nonBlockingIO()
                }
            }
        }
    }.also { println("nonBlockingIO Dispatcher.IO completed, time: $it") }
}

fun sample4()  {

    val numberOfTasks: Int = 100000

    val executorService = Executors.newFixedThreadPool(64)

    val startTime = System.currentTimeMillis()

    val completionService: CompletionService<Void?> = ExecutorCompletionService(executorService)

    for (i in 0 until numberOfTasks) {
        completionService.submit {
            blockingHttpCall()
            null
        }
    }

    for (i in 0 until numberOfTasks) {
        completionService.take().get()
    }

    val endTime = System.currentTimeMillis()
    val elapsedTime = endTime - startTime

    val minutes = (elapsedTime / 1000 / 60).toString().padStart(2, '0')
    val seconds = ((elapsedTime / 1000) % 60).toString().padStart(2, '0')

    val formattedTime = "$minutes m $seconds s"


    println("nonBlockingIO Java threads completed, time: $formattedTime")

    executorService.shutdown()

}

suspend fun sample5()  {

    measureTime {
        supervisorScope {
            repeat(100_000) {
                launch(Dispatchers.LOOM) {
                    nonBlockingIO()
                }
            }
        }
    }.also { println("nonBlockingIO Dispatcher.loom completed, time: $it") }

}

suspend fun sample6()  {

    measureTime {
        supervisorScope {
            val jobs = List(100_000) {
                async(Dispatchers.IO) {
                    nonBlockingIO()
                }
            }
            jobs.awaitAll()
        }
    }.also { println("nonBlockingIO async Dispatcher.IO completed, time: $it") }

}

suspend fun sample7()  {

    measureTime {
        supervisorScope {
            val jobs = List(100_000)  {
                async(Dispatchers.LOOM) {
                    nonBlockingIO()
                }
            }
            jobs.awaitAll()
        }
    }.also { println("nonBlockingIO async Dispatcher.loom completed, time: $it") }

}

suspend fun main() {
    sample1()
    sample2()
    sample3()
    sample4()
    sample5()
    sample6()
    sample7()
}
