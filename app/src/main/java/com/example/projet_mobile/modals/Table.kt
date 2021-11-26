package com.example.projet_mobile.modals

import java.sql.ResultSet

class Table(queryResult: ResultSet?) {


    init {
        var rows = ArrayList<String>()
        while (queryResult!!.next()) {
            //Loop through columns ??
        }

    }
}