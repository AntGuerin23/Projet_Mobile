package com.example.projet_mobile.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.projet_mobile.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.mainActivityButton).setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java)
            startActivity(intent)
        }
    }
}