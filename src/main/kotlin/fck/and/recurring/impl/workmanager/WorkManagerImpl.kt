package fck.and.recurring.impl.workmanager

import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import fck.and.recurring.FckRecurring
import fck.and.recurring.Repo
import fck.and.recurring.Work
import fck.and.recurring.impl.FckImpl
import java.util.concurrent.TimeUnit

internal class WorkManagerImpl : FckImpl() {
    override fun enqueue(work: Work) {
        if (work.interval < 15) return
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false)
            .build()
        val request = OneTimeWorkRequestBuilder<WorkManagerWorker>()
            .setInitialDelay(work.interval.toLong(), TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(workDataOf("name" to work.name, "round" to work.round))
            .build()
        WorkManager.getInstance(FckRecurring.app).enqueueUniqueWork(
            work.name,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }
    companion object {
        fun initialize() {
            WorkManager.initialize(FckRecurring.app, Configuration.Builder().build())
        }
    }
}
