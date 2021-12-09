package com.example.projet_mobile.modals

class User (val id : Int, val email : String, val password : String, val firstname : String, val lastname : String, var picture : ByteArray) {

    fun setNewImage(byteArray: ByteArray) {
        picture = byteArray;
        Database.updateImage(byteArray, id)
    }
}