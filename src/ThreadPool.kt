import java.util.LinkedList
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class ThreadPool(
    threadCount: Int,
): Executor {
    private val isActive = AtomicBoolean(true)
    private val threadList = mutableListOf<Thread>()
    private val threadQueue = LinkedList<Runnable>()
    private val locker = ReentrantLock()
    private val condition: Condition = locker.newCondition()

    init {
        repeat(threadCount) {
            threadList.add(
                Thread {
                    while (threadQueue.isNotEmpty() || isActive.get()) {
                        val task =
                            try {
                                locker.lock()
                                while (threadQueue.isEmpty() && isActive.get())
                                    condition.await()
                                if (threadQueue.isEmpty())
                                    continue
                                threadQueue.poll()
                            }
                            finally {
                                locker.unlock()
                            }
                        try {
                            task.run()
                        }
                        catch (_: Exception) {
                            return@Thread
                        }
                    }
                }.apply {start()})
        }
    }
    fun shutdown(wait: Boolean = true) {
        isActive.set(false)
        if (!wait)
            threadList.forEach { it.interrupt() }
        try {
            locker.lock()
            condition.signalAll()
        }
        finally {
            locker.unlock()
        }
        threadList.forEach {
            it.join()
        }

    }


    override fun execute(command: Runnable) {
        if (!isActive.get())
            throw IllegalStateException("ThreadPool is shutdown")
        locker.lock()
        try {
            threadQueue.add(command)
            condition.signal()
        }
        finally {
            locker.unlock()
        }
    }

}