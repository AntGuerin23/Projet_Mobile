package com.example.projet_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.rogerButton).setOnClickListener {
            var intent = Intent(this, RogerActivity::class.java).apply {
                //extrafdawdrDJKNOQduo
            }
            startActivity(intent)
        }
    }
}