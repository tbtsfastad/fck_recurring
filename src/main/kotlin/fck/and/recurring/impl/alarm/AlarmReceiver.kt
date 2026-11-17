package fck.and.recurring.impl.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("name")
        val round = intent.getIntExtra("round", -1)
        if (name == null || round < 0) return
        AlarmImpl().did(name, round)
    }
}
