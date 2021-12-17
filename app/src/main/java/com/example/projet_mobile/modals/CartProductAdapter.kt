package com.example.projet_mobile.modals

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.projet_mobile.R

class CartProductAdapter(
    private val context: Context,
    private val productList: ArrayList<Product>
) : BaseAdapter() {
    lateinit var rowView: View
    lateinit var currentProduct: Product
    lateinit var productImageImageView: ImageView
    lateinit var productNameTextView: TextView
    lateinit var productPriceTextView: TextView
    lateinit var quantityEditText: EditText
    lateinit var productPriceQuantityTextView: TextView

    private val inflater: LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE
    ) as LayoutInflater

    override fun getCount() = productList.size

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        rowView = inflater.inflate(R.layout.product_item_cart, parent, false)
        currentProduct = getItem(position) as Product
        productImageImageView = rowView.findViewById(R.id.ivProductImage)
        productNameTextView = rowView.findViewById(R.id.tvProductName)
        productPriceTextView = rowView.findViewById(R.id.tvProductPrice)
        quantityEditText = rowView.findViewById(R.id.etQuantity)
        productPriceQuantityTextView = rowView.findViewById(R.id.tvProductPriceQuantity)

        productImageImageView.setImageResource(currentProduct.imageResource)
        productNameTextView.text = currentProduct.name
        productPriceTextView.text = currentProduct.price.toString() + " $"
        productPriceQuantityTextView.text = adjustPrice()
        quantityEditText.setText(currentProduct.quantity.toString())

        val decrementButton = rowView.findViewById<Button>(R.id.bDecrement)
        val incrementButton = rowView.findViewById<Button>(R.id.bIncrement)
        decrementButton.setOnClickListener {
            decrementQuantity()
        }

        incrementButton.setOnClickListener {
            incrementQuantity()
        }
        adjustPrice()
        return rowView
    }

    private fun decrementQuantity() {
        if (currentProduct.quantity > 1) {
            currentProduct.quantity--
        }
        quantityEditText.setText(currentProduct.quantity.toString())
        productPriceQuantityTextView.text = adjustPrice()
    }

    private fun incrementQuantity() {
        currentProduct.quantity++
        quantityEditText.setText(currentProduct.quantity.toString())
        productPriceQuantityTextView.text = adjustPrice()
    }

    private fun adjustPrice(): String {
        val priceQuantity = currentProduct.price * currentProduct.quantity
        return "$priceQuantity $"
    }
}