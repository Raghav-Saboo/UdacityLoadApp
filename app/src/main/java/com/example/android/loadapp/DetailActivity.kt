package com.example.android.loadapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    setSupportActionBar(findViewById(R.id.toolbar))
    findViewById<TextView>(R.id.file_name).text = intent.getStringExtra(FILE_NAME)
    findViewById<TextView>(R.id.status).text = intent.getStringExtra(DOWNLOAD_STATUS)
    findViewById<Button>(R.id.ok_button)
      .setOnClickListener {
        startActivity(Intent(this, MainActivity::class.java))
      }
  }

}
