package com.example.projet_mobile.modals

import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.sql.PreparedStatement
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

    fun update(update: String) {
        connectDB()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val statement = connection!!.createStatement()
        statement.executeUpdate(update)
        disconnectDB()
    }

    fun querySelectImage(userId : Int) : ResultSet {
        connectDB()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val statement : PreparedStatement = connection!!.prepareStatement("SELECT picture FROM users WHERE user_id = ?")
        statement.setInt(1, userId)
        val result = statement.executeQuery()
        disconnectDB()
        return result;
    }

    fun insertUser(byteArray: ByteArray) {
        //TODO : Modify this function to get a User parameter and insert a user
        connectDB()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val statement : PreparedStatement = connection!!.prepareStatement("INSERT INTO users (firstname, lastname, email, picture) VALUES ('joe', 'louis', 'bruh2@gmail.com', ?)")
        statement.setBytes(1, byteArray)
        statement.executeUpdate()
        disconnectDB()
    }

    fun updateImage(byteArray: ByteArray, userId: Int) {
        connectDB()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val statement : PreparedStatement = connection!!.prepareStatement("UPDATE users SET picture = ? WHERE user_id = ?")
        statement.setBytes(1, byteArray)
        statement.setInt(2, userId)
        statement.executeUpdate()
        disconnectDB()
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