import java.util.concurrent.Executors

fun main() { // testing main()
    val thread = Thread () {
        println(100)
        Thread.sleep(1000)
        println(123)
    }
    thread.name = "123"
    val thread2 = Thread () {
        println(100)
        Thread.sleep(1000)
        println(123)
    }
    val thread3 = Thread () {
        println(156)
        Thread.sleep(100)
        println(789)
    }
    val threadPool = ThreadPool(5)
    threadPool.execute(thread2)
    threadPool.execute(thread2)
    threadPool.execute(thread2)
    threadPool.execute(thread2)
    threadPool.execute(thread2)
    threadPool.execute(thread2)
    threadPool.execute(thread3)
    Thread.sleep(1100)
    threadPool.shutdown(false)
}