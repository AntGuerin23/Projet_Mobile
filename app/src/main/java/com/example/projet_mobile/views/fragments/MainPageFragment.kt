package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.ProductAdapter
import com.example.projet_mobile.modals.ProductItem

class MainPageFragment : Fragment(R.layout.fragment_main_page) {

    private lateinit var productRecyclerView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productRecyclerView = view.findViewById(R.id.rvProduct)
        val productList = generateDummyProducts(500)
        productRecyclerView.adapter = ProductAdapter(productList)
        productRecyclerView.layoutManager = LinearLayoutManager(activity)
        productRecyclerView.setHasFixedSize(true)
    }

    private fun generateDummyProducts(size: Int): List<ProductItem> {

        val list = ArrayList<ProductItem>()

        for (i in 0 until size) {
            val drawable = when (i % 2) {
                0 -> R.drawable.ic_email
                else -> R.drawable.ic_lock
            }

            val item = ProductItem(drawable, "Item $i", "Line 2", "X$")
            list += item
        }

        return list
    }
}