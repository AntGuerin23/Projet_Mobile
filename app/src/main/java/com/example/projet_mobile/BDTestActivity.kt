package com.example.projet_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import Database




class BDTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bdtest)
        val db = Database()
        db.testQuery()
    }
}