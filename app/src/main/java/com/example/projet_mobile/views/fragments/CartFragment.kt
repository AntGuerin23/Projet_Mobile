package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.CartProductAdapter
import com.example.projet_mobile.modals.ProductAdapter
import com.example.projet_mobile.modals.ProductItem

class CartFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var cartProductList: ArrayList<ProductItem>

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
    }

    /*
     * Do query SELECT * FROM cart WHERE user_id = {current_user_id};
     *
     * Then put all items in the cart in {list}
     */
    private fun getProducts(): ArrayList<ProductItem> {
        val list = ArrayList<ProductItem>()
        val imageName = "p_black_hoodie"
        val imageId = resources.getIdentifier(
            imageName, "drawable", "com.example.projet_mobile")
        list += ProductItem(
            imageId,
            "Test product 2",
            "This is a test product",
            "20 $"
        )
        list += ProductItem(
            imageId,
            "Test product 5",
            "This is a test product",
            "20 $"
        )
        return list
    }
}