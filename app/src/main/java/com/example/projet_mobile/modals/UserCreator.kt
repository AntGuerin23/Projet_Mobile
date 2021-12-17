package com.example.projet_mobile.modals

import android.graphics.drawable.Drawable
import java.sql.PreparedStatement

class UserCreator {

    companion object UserCreator {

        fun createUser(userInfo : HashMap<String, String>) {
            User.email = userInfo["email"].toString()
            User.firstname = userInfo["firstname"].toString()
            User.lastname = userInfo["lastname"].toString()
            User.password = userInfo["password"].toString()
            User.id = getIdFromDatabase(User.email)
            User.picture = TableConverter.getUserImage(User.id)!!
            User.province = getProvince(Integer.valueOf(userInfo["province_id"].toString()))
        }

        fun createUser(email : String, firstname : String, lastname : String, password : String, picture : Drawable) {
            User.email = email
            User.firstname = firstname
            User.lastname = lastname
            User.password = password
            User.picture = PictureConverter.convertDrawableToByteArray(picture)
            User.id = getIdFromDatabase(email)
            User.province = Province.QUEBEC
        }

        private fun getIdFromDatabase(email : String) : Int {
            val statement: PreparedStatement = Database.connectDB()!!
                .prepareStatement("SELECT * FROM users WHERE email = ?")
            statement.setString(1, email)
            val result = TableConverter.getRows(Database.preparedQuery(statement))
            return Integer.valueOf(result.get(0)["user_id"])
        }

        private fun getProvince(provinceId : Int) : Province {
            return when (provinceId) {
                0 -> Province.QUEBEC
                1 -> Province.ONTARIO
                2 -> Province.B_C
                3 -> Province.ALBERTA
                4 -> Province.SASKATCHEWAN
                5 -> Province.MANITOBA
                6 -> Province.YUKON
                7 -> Province.NF_L
                8 -> Province.N_B
                9 -> Province.N_S
                10 -> Province.P_E_I
                else -> Province.QUEBEC
            }
        }
    }
}