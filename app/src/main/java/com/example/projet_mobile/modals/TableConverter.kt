package com.example.projet_mobile.modals

import android.util.Log
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.math.log

object TableConverter {

    fun getRows(queryResult: ResultSet?) : ArrayList<HashMap<String, String>> {
        var rows = ArrayList<HashMap<String, String>>()
        while (queryResult!!.next()) {
            var row = HashMap<String, String>()
            var data  = queryResult.metaData
            for (i in 1..data.columnCount) {
                row.put(data.getColumnName(i), queryResult.getString(i))
            }
            rows.add(row)
        }
        return rows
    }

    fun getUserImage(userId : Int) : ByteArray? {
        return getUserImageFromQuery(Database.querySelectImage(userId))
    }

    private fun getUserImageFromQuery(queryResult: ResultSet?) : ByteArray? {
        queryResult?.next();
        return queryResult?.getBytes(1)
    }
}