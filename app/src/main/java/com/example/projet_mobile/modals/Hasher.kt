package com.example.projet_mobile.modals

import java.math.BigInteger
import java.security.MessageDigest

class Hasher {

    companion object Hasher {
        fun hashString(input : String) : String {
            //https://stackoverflow.com/questions/46510338/sha-512-hashing-with-android
            val md: MessageDigest = MessageDigest.getInstance("SHA-512")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext: String = no.toString(16)
            while (hashtext.length < 128) {
                hashtext = "0$hashtext"
            }
            return hashtext
        }
    }
}