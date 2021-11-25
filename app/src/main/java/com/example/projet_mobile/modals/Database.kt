package com.example.projet_mobile.modals

import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy


class Database {
    private var connection: Connection? = null
    private val host = "206.167.241.243"
    private val database = "postgres"
    private val port = 5432
    private val user = "mathis"
    private val pass = "2071326"
    private var url = "jdbc:postgresql://%s:%d/%s"
    private var status = false
    private lateinit var thread : Thread


    fun testQuery() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        var statement = connection!!.createStatement()
        var result = statement.executeQuery("SELECT * FROM vendeur")

        while (result.next()) {
            Log.d("TAG", "testQuery: " + result.getString(4))
        }

    }

    private fun connect() {
        thread = Thread {
            try {
                Class.forName("org.postgresql.Driver")
                connection = DriverManager.getConnection(url, user, pass)
                status = true
                Log.d("Databaseeee", "connected: $status" )
            } catch (e: Exception) {
                status = false
                e.printStackTrace()
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }
    }

    init {
        url = String.format(url, host, port, database)
        connect()
        //this.disconnect();
        Log.d("Database", "connection status: $status" )
    }
}