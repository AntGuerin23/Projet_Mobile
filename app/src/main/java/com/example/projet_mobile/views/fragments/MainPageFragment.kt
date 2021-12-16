package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.*
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.ResultSet

class MainPageFragment : Fragment(),
    ProductAdapter.OnItemClickListener {

    private lateinit var productRecyclerView : RecyclerView
    private lateinit var productList: List<ProductItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
        User.firstname = "Bob"
        User.lastname = "Dole"
        User.email = "bobdole@gmail.com"
        User.password = "qwerty"
        User.picture = ContextCompat.getDrawable(requireContext(), R.drawable.default_pic)?.let {
            PictureConverter.convertDrawableToByteArray(
                it
            )
        }!!

    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    private fun getProducts(): List<ProductItem> {
        val list = ArrayList<ProductItem>()
        val imageName = "p_black_hoodie"
        val imageId = resources.getIdentifier(
            imageName, "drawable", "com.example.projet_mobile")
        list += ProductItem(
                imageId,
                "Test product 1",
                "This is a test product",
                "20 $"
            )
        list += ProductItem(
            imageId,
            "Test product 2",
            "This is a test product",
            "20 $"
        )
        list += ProductItem(
            imageId,
            "Test product 3",
            "This is a test product",
            "20 $"
        )
        list += ProductItem(
            imageId,
            "Test product 4",
            "This is a test product",
            "20 $"
        )
        list += ProductItem(
            imageId,
            "Test product 5",
            "This is a test product",
            "20 $"
        )
//
//        val results: ResultSet? = Database.query("SELECT * FROM products")
//        val resultTable = TableConverter.getRows(results)
//
//        for (product in resultTable) {
//            val imageName = product["image_url"].toString()
//            val imageId =
//                resources.getIdentifier(imageName, "drawable", "com.example.projet_mobile")
//
//            list += ProductItem(
//                imageId,
//                product["name"].toString(),
//                product["description"].toString(),
//                product["price"].toString().substringBefore(".") + " $"
//            )
//        }

        return list
    }
}