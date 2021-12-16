package com.example.projet_mobile.modals

import java.sql.PreparedStatement
import java.sql.ResultSet

object TableConverter {

    fun getRows(queryResult: ResultSet?) : ArrayList<HashMap<String, String>> {
        val rows = ArrayList<HashMap<String, String>>()
        while (queryResult!!.next()) {
            val row = HashMap<String, String>()
            val data  = queryResult.metaData
            for (i in 1..data.columnCount) {
                row[data.getColumnName(i)] = queryResult.getString(i)
            }
            rows.add(row)
        }
        return rows
    }

    fun getUserImage(userId : Int) : ByteArray? {
        val statement : PreparedStatement = Database.connectDB()!!.prepareStatement("SELECT picture FROM users WHERE user_id = ?")
        statement.setInt(1, userId)
        return getUserImageFromQuery(Database.preparedQuery(statement))
    }

    private fun getUserImageFromQuery(queryResult: ResultSet?) : ByteArray? {
        queryResult?.next();
        return queryResult?.getBytes(1)
    }
}