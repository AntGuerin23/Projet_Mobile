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
            User.province = getProvince(Integer.valueOf(userInfo["province_id"].toString()))!!
            User.id = getIdFromDatabase(User.email)
            User.picture = TableConverter.getUserImage(User.id)!!
        }

        fun createUser(email : String, firstname : String, lastname : String, password : String, picture : Drawable) {
            User.email = email
            User.firstname = firstname
            User.lastname = lastname
            User.password = password
            User.picture = PictureConverter.convertDrawableToByteArray(picture)
            User.province = Province.QUEBEC
            User.id = getIdFromDatabase(email)
        }

        private fun getIdFromDatabase(email : String) : Int {
            val statement: PreparedStatement = Database.connectDB()!!
                .prepareStatement("SELECT * FROM users WHERE email = ?")
            statement.setString(1, email)
            val result = TableConverter.getRows(Database.preparedQuery(statement))
            return Integer.valueOf(result.get(0)["user_id"])
        }

        private fun getProvince(provinceId : Int) : Province? {
            val provinces = Province.values()
            provinces.forEach { province ->
                if (province.id == provinceId) {
                    return province
                }
            }
            return null
        }
    }
}