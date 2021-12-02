package com.example.projet_mobile.modals

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
}