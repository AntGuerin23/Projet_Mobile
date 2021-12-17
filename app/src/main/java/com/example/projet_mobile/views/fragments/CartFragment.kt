package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ArrayAdapter
import android.widget.Button

import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R

import com.example.projet_mobile.modals.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import com.example.projet_mobile.modals.CartProductAdapter
import com.example.projet_mobile.modals.ProductAdapter
import com.example.projet_mobile.modals.ProductItem
import com.example.projet_mobile.views.activities.LoginActivity
import com.example.projet_mobile.views.activities.MainActivity


class CartFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var cartProductList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartProductList = getProducts()
        listView = view.findViewById(R.id.lvCart)
        listView.adapter = CartProductAdapter(requireActivity(), cartProductList)
        view.findViewById<Button>(R.id.bProceedToPayment).setOnClickListener {
            (activity as MainActivity).changeFragment(FormFragment())
        }
    }

    private fun getProducts(): ArrayList<Product> {
        val list = ArrayList<Product>()
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("SELECT * FROM cart_items ci JOIN products p ON ci.id_products = p.product_id WHERE ci.id_user = ?")
        statement.setInt(1, User.id)
        val results: ResultSet? = Database.preparedQuery(statement)
        val resultTable = TableConverter.getRows(results)
        for (product in resultTable) {
            val imageName = product["image_url"].toString()
            val imageId =
                resources.getIdentifier(imageName, "drawable", "com.example.projet_mobile")
            val productInstance = Product(
                    imageId,
            Integer.valueOf(product["id_products"].toString()),
            product["name"].toString(),
            product["description"].toString(),
            Integer.valueOf(product["price"].toString().substringBefore("."))
            )
            productInstance.quantity = Integer.valueOf(product["quantity"].toString())
            list += productInstance
        }
        return list
    }
}