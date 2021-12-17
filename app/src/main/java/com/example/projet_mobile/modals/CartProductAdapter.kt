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
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import java.sql.PreparedStatement

class CartProductAdapter(
    private val context: Context,
    private val productList: ArrayList<Product>,
    private val fragment: Fragment
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
        val productPriceQuantityTextView =
            rowView.findViewById<TextView>(R.id.tvProductPriceQuantity)

        productImageImageView.setImageResource(currentProduct.imageResource)
        productNameTextView.text = currentProduct.name
        productPriceTextView.text = currentProduct.price.toString() + " $"
        productPriceQuantityTextView.text = adjustData(currentProduct)
        quantityTextView.text = currentProduct.quantity.toString()

        initializeEventListeners(
            rowView,
            quantityTextView,
            productPriceQuantityTextView,
            currentProduct
        )
        adjustData(currentProduct)
        return rowView
    }

    private fun decrementQuantity(
        quantityEditText: TextView,
        productPriceQuantityTextView: TextView,
        product: Product
    ) {
        if (product.quantity > 1) {
            product.quantity--
            quantityEditText.text = product.quantity.toString()
            productPriceQuantityTextView.text = adjustData(product)
        }
    }

    private fun incrementQuantity(
        quantityEditText: TextView,
        productPriceQuantityTextView: TextView,
        product: Product
    ) {
        if (product.quantity < 100) {
            product.quantity++
            quantityEditText.text = product.quantity.toString()
            productPriceQuantityTextView.text = adjustData(product)
        }
    }

    private fun deleteItem(product: Product) {
        productList.remove(product)
        notifyDataSetChanged()
        deleteFromDB(product)
    }

    private fun adjustData(product: Product): String {
        updateDatabase(product)
        updateBill()
        return (product.price * product.quantity).toString() + " $"
    }

    private fun initializeEventListeners(
        rowView: View,
        quantityTextView: TextView,
        productPriceQuantityTextView: TextView,
        currentProduct: Product
    ) {
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
    }

    private fun updateDatabase(product: Product) {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("UPDATE cart_items SET quantity = ? WHERE id_products = ? AND id_user = ?")
        statement.setInt(1, product.quantity)
        statement.setInt(2, product.id)
        statement.setInt(3, User.id)
        Database.update(statement)
    }

    private fun deleteFromDB(product: Product) {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("DELETE FROM cart_items WHERE id_products = ? AND id_user = ?")
        statement.setInt(1, product.id)
        statement.setInt(2, User.id)
        Database.update(statement)
    }

    private fun updateBill() {
        val subTotal = calculateSubTotal()
        val tps = calculateTPS(subTotal)
        val provinceTax = calculateProvinceTax(subTotal)
        val total = calculateTotal(subTotal, tps, provinceTax)
        updateFields(subTotal, tps, provinceTax, total)
    }

    private fun calculateSubTotal(): Double {
        var subTotal = 0
        for (product in productList) {
            subTotal += product.price * product.quantity
        }
        return subTotal.toDouble()
    }

    private fun calculateTPS(subTotal: Double): Double {
        return subTotal * 0.0975
    }

    private fun calculateProvinceTax(subTotal: Double): Double {
        return subTotal * User.province.taxPercentage
    }

    private fun calculateTotal(subTotal: Double, tps: Double, provinceTax: Double): Double {
        val total = subTotal + tps + provinceTax
        User.total = total
        return total
    }

    @SuppressLint("SetTextI18n")
    private fun updateFields(subTotal: Double, tps: Double, provinceTax: Double, total: Double) {
        fragment.requireView().findViewById<TextView>(R.id.tvSubTotalPrice).text =
            ("%.2f".format(subTotal) + " $")
        fragment.requireView().findViewById<TextView>(R.id.tvTPSPrice).text =
            "%.2f".format(tps) + " $"
        fragment.requireView().findViewById<TextView>(R.id.tvTVQPrice).text =
            "%.2f".format(provinceTax) + " $"
        fragment.requireView().findViewById<TextView>(R.id.tvTotalPrice).text =
            "%.2f".format(total) + " $"
    }

}