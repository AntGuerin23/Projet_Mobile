package com.example.projet_mobile.modals

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.projet_mobile.R

class CartProductAdapter(
    private val context: Context,
    private val productList: ArrayList<Product>
) : BaseAdapter() {

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
         val rowView = inflater.inflate(R.layout.product_item_cart, parent, false)
         val currentProduct = getItem(position) as Product
         val productImageImageView = rowView.findViewById<ImageView>(R.id.ivProductImage)
         val productNameTextView = rowView.findViewById<TextView>(R.id.tvProductName)
         val productPriceTextView = rowView.findViewById<TextView>(R.id.tvProductPrice)
         val quantityTextView = rowView.findViewById<TextView>(R.id.tvQuantity)
         val productPriceQuantityTextView = rowView.findViewById<TextView>(R.id.tvProductPriceQuantity)

        // productImageImageView.setImageResource(currentProduct.imageResource)
        // productNameTextView.text = currentProduct.name
        // productPriceTextView.text = currentProduct.price
        // productPriceQuantityTextView.text = adjustPrice(quantityTextView, currentProduct)

        productImageImageView.setImageResource(currentProduct.imageResource)
        productNameTextView.text = currentProduct.name
        productPriceTextView.text = currentProduct.price.toString() + " $"
        productPriceQuantityTextView.text = adjustPrice(currentProduct)
        quantityTextView.setText(currentProduct.quantity.toString())

        val decrementButton = rowView.findViewById<Button>(R.id.bDecrement)
        val incrementButton = rowView.findViewById<Button>(R.id.bIncrement)
        decrementButton.setOnClickListener {
            decrementQuantity(quantityTextView, productPriceQuantityTextView, currentProduct)
        }

        incrementButton.setOnClickListener {
            incrementQuantity(quantityTextView, productPriceQuantityTextView, currentProduct)
        }
        adjustPrice(currentProduct)
        return rowView
    }

    private fun decrementQuantity(quantityEditText: TextView,
                                  productPriceQuantityTextView: TextView,
                                  product: Product) {
        if (product.quantity > 1) {
            product.quantity--
        }
        quantityEditText.text = product.quantity.toString()
        productPriceQuantityTextView.text = adjustPrice(product)
    }

    private fun incrementQuantity(quantityEditText: TextView,
                                  productPriceQuantityTextView: TextView,
                                  product: Product) {
        if (product.quantity < 100) {
            product.quantity++
            quantityEditText.text = product.quantity.toString()
            productPriceQuantityTextView.text = adjustPrice(product)
        }
    }

    private fun adjustPrice(product: Product) : String {
        return (product.price * product.quantity).toString() + " $"
    }

    // private fun decrementQuantity() {
    //     if (currentProduct.quantity > 1) {
    //         currentProduct.quantity--
    //     }
    //     quantityEditText.setText(currentProduct.quantity.toString())
    //     productPriceQuantityTextView.text = adjustPrice()
    // }

    // private fun incrementQuantity() {
    //     currentProduct.quantity++
    //     quantityEditText.setText(currentProduct.quantity.toString())
    //     productPriceQuantityTextView.text = adjustPrice()
    // }

    // private fun adjustPrice(): String {
    //     val priceQuantity = currentProduct.price * currentProduct.quantity
    //     return "$priceQuantity $"
    // }

}