package com.example.projet_mobile.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.R

class BDTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bdtest)
        val db = Database()
        db.testQuery()
    }
}