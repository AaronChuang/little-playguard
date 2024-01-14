package tw.aaron.coroutine

import kotlinx.coroutines.*


val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.message?.let { println("CoroutineExceptionHandler got $throwable") }
}

suspend fun exceptionError1()  {
    coroutineScope {
        launch(exceptionHandler) {
            println("exceptionError1-1")
            throw IllegalArgumentException("error")
        }

        launch(exceptionHandler) {
            println("exceptionError1-2")
            nonBlockingIO()
        }

        launch(exceptionHandler) {
            println("exceptionError1-3")
            nonBlockingIO()
        }
    }
}

suspend fun exceptionError2()  {
    supervisorScope {
        launch(exceptionHandler) {
            println("exceptionError2-1")
            throw IllegalArgumentException("error")
        }
        launch(exceptionHandler) {
            println("exceptionError2-2")
            nonBlockingIO()
        }
        launch(exceptionHandler) {
            println("exceptionError2-3")
            nonBlockingIO()
        }
    }
}


suspend fun exceptionError3()  {
    coroutineScope {
        listOf(
            async(exceptionHandler) {
                println("exceptionError3-1")
                throw IllegalArgumentException("error")
            },

            async(exceptionHandler) {
                println("exceptionError3-2")
                nonBlockingIO()
            },

            async(exceptionHandler) {
                println("exceptionError3-3")
                nonBlockingIO()
            }
        )
    }.awaitAll()
}

suspend fun exceptionError4()  {
    supervisorScope {
        listOf(
            async(exceptionHandler) {
                println("exceptionError4-1")
                throw IllegalArgumentException("error")
            },
            async(exceptionHandler) {
                println("exceptionError4-2")
                nonBlockingIO()
            },
            async(exceptionHandler) {
                println("exceptionError4-3")
                nonBlockingIO()
            }
        )
    }.awaitAll()
}


suspend fun main() {
    println("start")
    try {
        exceptionError1()
    }catch (e:Exception){
        println("exceptionError1 error")
    }
    try {
        exceptionError2()
    }catch (e:Exception){
        println("exceptionError2 error")
    }
    try {
        exceptionError3()
    }catch (e:Exception){
        println("exceptionError3 error")
    }
    try {
        exceptionError4()
    } catch (e:Exception){
        println("exceptionError4 error")
    }
    println("end")
}
