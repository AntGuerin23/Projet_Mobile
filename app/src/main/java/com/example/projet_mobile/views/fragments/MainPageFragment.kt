package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.ProductAdapter
import com.example.projet_mobile.modals.ProductItem
import com.example.projet_mobile.modals.TableConverter
import java.sql.ResultSet

class MainPageFragment : Fragment(R.layout.fragment_main_page) {

    private lateinit var productRecyclerView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productRecyclerView = view.findViewById(R.id.rvProduct)
        val productList = getProducts()
        productRecyclerView.adapter = ProductAdapter(productList)
        productRecyclerView.layoutManager = LinearLayoutManager(activity)
        productRecyclerView.setHasFixedSize(true)
    }

    private fun getProducts(): List<ProductItem> {
        val list = ArrayList<ProductItem>()

        val results: ResultSet? = Database.query("SELECT * FROM products")
        val resultTable = TableConverter.getRows(results)

        for (i in resultTable) {
            val imageId = i["image_url"].toString().toInt()
            list += ProductItem(imageId,
                i["name"].toString(),
                i["description"].toString(),
                i["price"].toString().substringBefore(".") + " $")
        }
        return list
    }
}