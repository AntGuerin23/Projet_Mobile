package com.example.projet_mobile.modals

import android.graphics.drawable.Drawable

class UserCreator {

    companion object UserCreator {

        fun createUser(userInfo : HashMap<String, String>) {
            User.email = userInfo["email"].toString()
            User.firstname = userInfo["firstname"].toString()
            User.lastname = userInfo["lastname"].toString()
            User.password = userInfo["password"].toString()
        }

        fun createUser(email : String, firstname : String, lastname : String, password : String, picture : Drawable) {
            User.email = email
            User.firstname = firstname
            User.lastname = lastname
            User.password = password
            User.picture = PictureConverter.convertDrawableToByteArray(picture)
        }
    }
}