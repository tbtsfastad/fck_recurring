package fck.and.recurring.impl.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import fck.and.recurring.FckRecurring
import fck.and.recurring.Work
import fck.and.recurring.impl.FckImpl

internal class AlarmImpl : FckImpl() {
    override fun enqueue(work: Work) {
        val intent = Intent(FckRecurring.app, AlarmReceiver::class.java)
            .setAction(work.name)
            .putExtra("name", work.name)
            .putExtra("round", work.round)
        val pendingIntent = PendingIntent.getBroadcast(
            FckRecurring.app,
            work.round,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val triggerAt = SystemClock.elapsedRealtime() + work.interval * 60_000L + 5_000L
        FckRecurring.app.getSystemService(AlarmManager::class.java).setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAt,
            pendingIntent,
        )
    }
}
