package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.Product
import com.example.projet_mobile.modals.User
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.PreparedStatement

class ProductDetailFragment(private val product: Product) : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var imageImageView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var quantity: EditText
    private lateinit var decrementButton: Button
    private lateinit var incrementButton: Button
    private lateinit var addButton: Button
    private lateinit var backButton: Button
    private var currentQuantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        initializeInformation()
        initializeButtons()
    }

    private fun initializeViews(view: View) {
        nameTextView = view.findViewById(R.id.tvProductName)
        priceTextView = view.findViewById(R.id.tvProductPrice)
        imageImageView = view.findViewById(R.id.ivProductImage)
        descriptionTextView = view.findViewById(R.id.tvProductDescription)
        quantity = view.findViewById(R.id.etQuantity)
        decrementButton = view.findViewById(R.id.bDecrement)
        incrementButton = view.findViewById(R.id.bIncrement)
        addButton = view.findViewById(R.id.bAddToCard)
        backButton = view.findViewById(R.id.bBack)
    }

    private fun initializeInformation() {
        nameTextView.text = product.name
        priceTextView.text = product.price.toString() + " $"
        imageImageView.setImageResource(product.imageResource)
        descriptionTextView.text = product.description
    }

    private fun initializeButtons() {
        decrementButton.setOnClickListener {
            decrementQuantity()
        }

        incrementButton.setOnClickListener {
            incrementQuantity()
        }

        addButton.setOnClickListener {
            addToCart()
        }

        backButton.setOnClickListener {
            back()
        }
    }

    private fun decrementQuantity() {
        if (currentQuantity > 1) {
            currentQuantity--
        }
        quantity.setText(currentQuantity.toString())
    }

    private fun incrementQuantity() {
        currentQuantity++
        quantity.setText(currentQuantity.toString())
    }

    private fun addToCart() {
        //TODO : If doesn't already exist
        Toast.makeText(activity, "'${product.name}' added to cart!", Toast.LENGTH_SHORT).show()
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("INSERT INTO cart_items (id_products, id_user, quantity, price) VALUES (?, ?, ?, ?)")
        statement.setInt(1, product.id)
        statement.setInt(2, User.id)
        statement.setInt(3, currentQuantity)
        statement.setInt(4, currentQuantity * product.price)
        Database.update(statement)
    }

    private fun back() {
        (activity as MainActivity).changeFragment(MainPageFragment())
    }
}