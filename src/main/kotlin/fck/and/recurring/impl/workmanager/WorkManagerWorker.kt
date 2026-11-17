package fck.and.recurring.impl.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

internal class WorkManagerWorker(
    context: Context,
    parameters: WorkerParameters,
) : Worker(context, parameters) {
    override fun doWork(): Result {
        val name = inputData.getString("name")
        val round = inputData.getInt("round", -1)
        if (name == null || round < 0) return Result.failure()
        WorkManagerImpl().did(name, round)
        return Result.success()
    }
}
