package com.example.projet_mobile.modals

data class Product(val imageResource: Int,
                   val id : Int,
                   var name: String,
                   val description: String,
                   val price: Int) {
    var quantity = 0
}
