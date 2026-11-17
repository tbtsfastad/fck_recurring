package fck.and.recurring.impl

import fck.and.recurring.FckRecurring
import fck.and.recurring.FckWorker
import fck.and.recurring.Repo
import fck.and.recurring.Work
import fck.and.recurring.impl.alarm.AlarmImpl
import fck.and.recurring.impl.workmanager.WorkManagerImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal abstract class FckImpl {
    abstract fun enqueue(work: Work)
    fun did(name: String, round: Int) {
        MainScope().launch {
            val work = Repo.instance.query(name, round) ?: return@launch
            if (work.state != Work.PENDING) return@launch
            runCatching {
                Class.forName(work.worker)
                    .asSubclass(FckWorker::class.java)
                    .getConstructor()
                    .newInstance()
                    .did(FckRecurring.app)
            }
            val next = Work(
                name = work.name,
                worker = work.worker,
                interval = work.interval,
                round = work.round + 1,
                state = Work.PENDING,
            )
            Repo.instance.insert(next)
            AlarmImpl().enqueue(next)
            WorkManagerImpl().enqueue(next)
            Repo.instance.updateState(work.id, Work.DID)
        }
    }
}
