package cz.cvut.fel.zan.macham29_zan.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import cz.cvut.fel.zan.macham29_zan.MainActivity
import cz.cvut.fel.zan.macham29_zan.R

class MatchAlarmReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context, intent: Intent) {
        val matchTitle = intent.getStringExtra("EXTRA_MATCH_TITLE") ?: "Favorited Match"
        val matchId = intent.getIntExtra("EXTRA_MATCH_ID", -1)

        val isDemo = intent.getBooleanExtra("IS_DEMO", false)
        val matchDate = intent.getStringExtra("EXTRA_MATCH_DATE") ?: ""

        val channelId = "match_reminders_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Upcoming Match Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Notification before favorited match start"
        notificationManager.createNotificationChannel(channel)

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            matchId,
            tapIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val titleText = if (isDemo) "You favorited $matchTitle!" else "Match starting soon!"
        val bodyText = if (isDemo) "Kickoff is due $matchDate" else "$matchTitle starts in an hour!"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_football)
            .setContentTitle(titleText)
            .setContentText(bodyText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val finalNotificationId = if (isDemo) matchId + 100000 else matchId
        notificationManager.notify(finalNotificationId, notification)
    }
}