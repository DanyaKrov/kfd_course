import java.util.LinkedList
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.locks.ReentrantLock

class ThreadPool(threadCount: Int) : Executor {

    var threadList: List<Thread> = (1..threadCount).map { Thread() }
    var queueList: LinkedList<Runnable> = LinkedList()
    private val exceptionHandler = Thread.UncaughtExceptionHandler { _: Thread, _ -> run {} }
    private var isShutDown: Boolean = true
    private var isActive: Boolean = true
    private var addThreadLock = ReentrantLock()

    init {
        val thread = Thread(ThreadExecutor())
        thread.start()
    }

    inner class ThreadExecutor: Runnable {
        override fun run() {
            while (isActive) {
                for(thread in threadList) {
                    if (thread.state == Thread.State.NEW) { // Thread wasn't launched yet
                        thread.start()
                    }
                    else if (addThreadLock.tryLock() && !queueList.isEmpty() && thread.state == Thread.State.TERMINATED) {
                        threadList = threadList.minus(thread) + Thread(queueList[0]) // adding new in the place of executed one
                        queueList.removeAt(0)
                        addThreadLock.unlock()
                    }
                }
            }
        }
    }


    override fun execute(command: Runnable) {
        if (isShutDown)
            queueList.add(command)
        else // new threads cant be added after shutdown
            throw RejectedExecutionException("ThreadPool was shutdown")
    }

    fun shutdown(wait: Boolean) {
        isShutDown = false
        if (wait)
            return
        for(thread in threadList) {
            thread.setUncaughtExceptionHandler(exceptionHandler) // for Exceptions from interrupting Thread.sleep()
            thread.interrupt()
        }
        isActive = false // killing thread running loop
    }
}