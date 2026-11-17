package fck.and.recurring

import android.content.Context
import fck.and.recurring.impl.alarm.AlarmImpl
import fck.and.recurring.impl.workmanager.WorkManagerImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FckRecurring private constructor() {
    fun enqueue(name: String, interval: Int, worker: FckWorker) {
        MainScope().launch {
            val workerClass = worker::class.java
            val current = Repo.instance.query(name)
            val work = when {
                current == null -> Work(
                    name = name,
                    worker = workerClass.name,
                    interval = interval,
                    round = 0,
                    state = Work.PENDING,
                ).also { Repo.instance.insert(it) }
                current.interval != interval -> current.copy(interval = interval).also {
                    Repo.instance.updateInterval(current.id, interval)
                }
                else -> current
            }
            AlarmImpl().enqueue(work)
            WorkManagerImpl().enqueue(work)
        }
    }

    companion object {
        lateinit var app: Context
            private set
        val instance by lazy {
            WorkManagerImpl.initialize()
            Repo.instance
            FckRecurring()
        }
        fun initialize(context: Context) {
            app = context.applicationContext
            instance
        }
    }
}
