package com.example.projet_mobile.modals

import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.sql.ResultSet

object Database {

    private var connection: Connection? = null
    private val host = "206.167.241.245"
    private val database = "postgres"
    private val port = 5432
    private val user = "etudiant"
    private val pass = "Etudiant1"
    private var url = "jdbc:postgresql://%s:%d/%s"
    private var status = false
    private lateinit var thread : Thread

    fun query(query: String): ResultSet? {
        connectDB()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val statement = connection!!.createStatement()
        val result = statement.executeQuery(query)

        disconnectDB()
        return result
    }

    private fun connectDB() {
        thread = Thread {
            try {
                Class.forName("org.postgresql.Driver")
                connection = DriverManager.getConnection(url, user, pass)
                status = true
                Log.d("Database", "connected: $status" )
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

    private fun disconnectDB() {
        connection!!.close()
    }

    init {
        url = String.format(url, host, port, database)
    }
}