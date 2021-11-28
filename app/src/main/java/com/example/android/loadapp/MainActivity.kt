package com.example.android.loadapp

import android.app.DownloadManager
import android.app.DownloadManager.COLUMN_STATUS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.android.loadapp.ButtonState.Completed
import com.example.android.loadapp.Status.SUCCESS

class MainActivity : AppCompatActivity() {

  private var downloadID: Long = 0
  private var downloadURL = ""
  private var downloadedFileName = ""
  private var downloadedFileNotification = ""

  private lateinit var notificationManager: NotificationManager
  private lateinit var pendingIntent: PendingIntent
  private lateinit var action: NotificationCompat.Action
  private lateinit var loadingButton: LoadingButton

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(findViewById(R.id.toolbar))
    registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    notificationManager = getSystemService(NotificationManager::class.java)
    createChannel(getString(R.string.channel_id), getString(R.string.channel_name))

    findViewById<RadioGroup>(R.id.downloadRadioGroup).setOnCheckedChangeListener { _, checkedId ->
      when (checkedId) {
        R.id.glide_button -> {
          downloadURL = getString(R.string.glide_url)
          downloadedFileName = getString(R.string.glide_text)
          downloadedFileNotification = getString(R.string.glide_notification_text)
        }
        R.id.load_app_button -> {
          downloadURL = getString(R.string.load_app_url)
          downloadedFileName = getString(R.string.load_app_text)
          downloadedFileNotification = getString(R.string.load_app_notification_text)
        }
        R.id.retrofit_button -> {
          downloadURL = getString(R.string.retrofit_url)
          downloadedFileName = getString(R.string.retrofit_text)
          downloadedFileNotification = getString(R.string.retrofit_notification_text)
        }
      }
    }
    loadingButton = findViewById(R.id.loadingButton)
    loadingButton.setOnClickListener {
      download()
    }
  }

  private val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      val action = intent?.action
      if (downloadID == id) {
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
          loadingButton.setLoadingButtonState(Completed)
          val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
          val cursor: Cursor =
            downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
          if (cursor.moveToFirst() && cursor.count > 0) {
            val colId = cursor.getColumnIndex(COLUMN_STATUS)
            val status = cursor.getInt(colId)
            sendNotification(status)
          }
        }
      }
    }

    private fun sendNotification(status: Int) {
      if (status == DownloadManager.STATUS_SUCCESSFUL) {
        notificationManager.sendNotification(downloadedFileNotification,
                                             SUCCESS,
                                             applicationContext)
      } else {
        notificationManager.sendNotification(downloadedFileNotification,
                                             Status.FAILED,
                                             applicationContext)
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.N)
  private fun download() {
    loadingButton.setLoadingButtonState(ButtonState.Clicked)
    if (downloadURL.isEmpty()) {
      loadingButton.setLoadingButtonState(Completed)
      Toast.makeText(this, getString(R.string.no_button_selected_text), Toast.LENGTH_SHORT).show()
      return
    }
    loadingButton.setLoadingButtonState(ButtonState.Loading)
    val request =
      DownloadManager.Request(Uri.parse(downloadURL))
        .setTitle(getString(R.string.app_name))
        .setDescription(getString(R.string.app_description))
        .setRequiresCharging(false)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    downloadID =
      downloadManager.enqueue(request)// enqueue puts the download request in the queue.
  }

  private fun createChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationChannel = NotificationChannel(
        channelId,
        channelName,
        IMPORTANCE_HIGH
      ).apply {
        setShowBadge(false)
      }
      notificationChannel.enableLights(true)
      notificationChannel.lightColor = Color.RED
      notificationChannel.enableVibration(true)
      notificationChannel.description = getString(R.string.channel_description)
      val notificationManager = getSystemService(NotificationManager::class.java)
      notificationManager.createNotificationChannel(notificationChannel)
    }
  }

}