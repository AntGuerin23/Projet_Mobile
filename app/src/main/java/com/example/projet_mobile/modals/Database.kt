package com.example.projet_mobile.modals

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

object Database {

    var connection: Connection? = null
//    private val host = "206.167.241.245"
//    private val database = "postgres"
//    private val port = 5432
//    private val user = "etudiant"
//    private val pass = "Etudiant1"
    private val host = "192.168.56.1"
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

    fun preparedQuery(statement : PreparedStatement) : ResultSet? {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val result = statement.executeQuery();
        disconnectDB()
        return result
    }

    fun update(statement: PreparedStatement) {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        statement.executeUpdate()
        disconnectDB()
    }

//    fun querySelectImage(userId : Int) : ResultSet {
//        connectDB()
//        val policy = ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
//        val statement : PreparedStatement = connection!!.prepareStatement("SELECT picture FROM users WHERE user_id = ?")
//        statement.setInt(1, userId)
//        val result = statement.executeQuery()
//        disconnectDB()
//        return result;
//    }

    fun connectDB() : Connection? {
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
        return connection;
    }

    private fun disconnectDB() {
        connection!!.close()
    }

    init {
        url = String.format(url, host, port, database)
    }
}