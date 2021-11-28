package com.example.android.loadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Notification ID.
private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
  fileName: String,
  status: Status,
  applicationContext: Context,
) {
  // Create the content intent for the notification, which launches
  // this activity
  val contentIntent = Intent(applicationContext, DetailActivity::class.java)
  contentIntent.putExtra(FILE_NAME, fileName)
  contentIntent.putExtra(DOWNLOAD_STATUS, status.name)
  val contentPendingIntent = PendingIntent.getActivity(
    applicationContext,
    NOTIFICATION_ID,
    contentIntent,
    FLAG_UPDATE_CURRENT
  )

  // Build the notification
  val builder = NotificationCompat.Builder(
    applicationContext,
    applicationContext.getString(R.string.channel_id)
  )
    .setSmallIcon(R.drawable.ic_assistant_black_24dp)
    .setContentTitle(applicationContext.getString(R.string.notification_title))
    .setContentText(fileName)
    .setContentIntent(contentPendingIntent)
    .setAutoCancel(true)
    .addAction(0, applicationContext.getString(R.string.notification_button), contentPendingIntent)
    .setPriority(NotificationCompat.PRIORITY_HIGH)
  notify(NOTIFICATION_ID, builder.build())
}
