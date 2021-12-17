package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.Product
import com.example.projet_mobile.modals.TableConverter
import com.example.projet_mobile.modals.User
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.PreparedStatement

class ProductDetailFragment(private val product: Product) : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var imageImageView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var quantity: TextView
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
        quantity = view.findViewById(R.id.tvQuantity)
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
            quantity.text = currentQuantity.toString()
        }
    }

    private fun incrementQuantity() {
        if (currentQuantity < 100) {
            currentQuantity++
            quantity.text = currentQuantity.toString()
        }
    }

    private fun addToCart() {
        Toast.makeText(activity, "'${product.name}' added to cart!", Toast.LENGTH_SHORT).show()
        val duplicates = getDuplicates()
        if (!isItemInDB(duplicates)) {
            writeCartItem()
        } else {
            updateCartItem(duplicates)
        }
    }

    private fun getDuplicates() :  ArrayList<HashMap<String, String>> {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("SELECT * FROM cart_items WHERE id_user = ? AND id_products = ?")
        statement.setInt(1, User.id)
        statement.setInt(2, product.id)
        return TableConverter.getRows(Database.preparedQuery(statement))
    }

    private fun writeCartItem() {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("INSERT INTO cart_items (id_products, id_user, quantity, price) VALUES (?, ?, ?, ?)")
        statement.setInt(1, product.id)
        statement.setInt(2, User.id)
        statement.setInt(3, currentQuantity)
        statement.setInt(4, currentQuantity * product.price)
        Database.update(statement)
    }

    private fun updateCartItem(duplicates: ArrayList<HashMap<String, String>>) {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("UPDATE cart_items SET quantity = ? WHERE id_products = ? AND id_user = ?")
        statement.setInt(1, getCartQuantity(duplicates) + currentQuantity)
        statement.setInt(2, product.id)
        statement.setInt(3, User.id)
        Database.update(statement)
    }

    private fun isItemInDB(duplicates : ArrayList<HashMap<String, String>>) : Boolean {
        return duplicates.size != 0
    }

    private fun getCartQuantity(duplicates : ArrayList<HashMap<String, String>>) : Int {
        return Integer.valueOf(duplicates.get(0)["quantity"].toString())
    }

    private fun back() {
        (activity as MainActivity).changeFragment(MainPageFragment())
    }
}