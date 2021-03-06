package com.example.projet_mobile.views.fragments

import android.location.LocationManager
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.Product
import com.example.projet_mobile.modals.ProductAdapter
import com.example.projet_mobile.modals.TableConverter
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.ResultSet

class MainPageFragment : Fragment(),
    ProductAdapter.OnItemClickListener {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productList: List<Product>
    private lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productList = getProducts()
        productRecyclerView = view.findViewById(R.id.rvProduct)
        productRecyclerView.adapter = ProductAdapter(productList, this)
        productRecyclerView.layoutManager = LinearLayoutManager(activity)
        productRecyclerView.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int) {
        val clickedProductItem = productList[position]
        (activity as MainActivity).changeFragment(ProductDetailFragment(clickedProductItem))
    }

    private fun getProducts(): List<Product> {
        val list = ArrayList<Product>()
        val results: ResultSet? = Database.query("SELECT * FROM products")
        val resultTable = TableConverter.getRows(results)
        for (product in resultTable) {
            val imageName = product["image_url"].toString()
            val imageId =
                resources.getIdentifier(imageName, "drawable", "com.example.projet_mobile")
            list += Product(
                imageId,
                Integer.valueOf(product["product_id"].toString()),
                product["name"].toString(),
                product["description"].toString(),
                Integer.valueOf(product["price"].toString().substringBefore("."))
            )
        }
        return list
    }
}