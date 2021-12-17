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
import java.sql.PreparedStatement

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
        productPriceQuantityTextView.text = adjustData(currentProduct)
        quantityTextView.setText(currentProduct.quantity.toString())

        val decrementButton = rowView.findViewById<Button>(R.id.bDecrement)
        val incrementButton = rowView.findViewById<Button>(R.id.bIncrement)
        val deleteButton = rowView.findViewById<Button>(R.id.bTrash)
        decrementButton.setOnClickListener {
            decrementQuantity(quantityTextView, productPriceQuantityTextView, currentProduct)
        }

        incrementButton.setOnClickListener {
            incrementQuantity(quantityTextView, productPriceQuantityTextView, currentProduct)
        }

        deleteButton.setOnClickListener {
            deleteItem(currentProduct)
        }
        adjustData(currentProduct)
        return rowView
    }

    private fun decrementQuantity(quantityEditText: TextView,
                                  productPriceQuantityTextView: TextView,
                                  product: Product) {
        if (product.quantity > 1) {
            product.quantity--
            quantityEditText.text = product.quantity.toString()
            productPriceQuantityTextView.text = adjustData(product)
        }
    }

    private fun incrementQuantity(quantityEditText: TextView,
                                  productPriceQuantityTextView: TextView,
                                  product: Product) {
        if (product.quantity < 100) {
            product.quantity++
            quantityEditText.text = product.quantity.toString()
            productPriceQuantityTextView.text = adjustData(product)
        }
    }

    private fun deleteItem(product : Product) {
        productList.remove(product)
        notifyDataSetChanged()
        deleteFromDB(product)
    }

    private fun adjustData(product: Product) : String {
        updateDatabase(product)
        return (product.price * product.quantity).toString() + " $"
    }

    private fun updateDatabase(product : Product) {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("UPDATE cart_items SET quantity = ? WHERE id_products = ? AND id_user = ?")
        statement.setInt(1, product.quantity)
        statement.setInt(2, product.id)
        statement.setInt(3, User.id)
        Database.update(statement)
    }

    private fun deleteFromDB(product : Product) {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("DELETE FROM cart_items WHERE id_products = ? AND id_user = ?")
        statement.setInt(1, product.id)
        statement.setInt(2, User.id)
        Database.update(statement)
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