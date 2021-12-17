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
        // val rowView = inflater.inflate(R.layout.product_item_cart, parent, false)

        // val currentProduct = getItem(position) as ProductItem

        // val productImageImageView = rowView.findViewById<ImageView>(R.id.ivProductImage)
        // val productNameTextView = rowView.findViewById<TextView>(R.id.tvProductName)
        // val productPriceTextView = rowView.findViewById<TextView>(R.id.tvProductPrice)
        // val quantityTextView = rowView.findViewById<TextView>(R.id.tvQuantity)
        // val productPriceQuantityTextView = rowView.findViewById<TextView>(R.id.tvProductPriceQuantity)

        // productImageImageView.setImageResource(currentProduct.imageResource)
        // productNameTextView.text = currentProduct.name
        // productPriceTextView.text = currentProduct.price
        // productPriceQuantityTextView.text = adjustPrice(quantityTextView, currentProduct)

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

    private fun decrementQuantity(quantityEditText: TextView,
                                  productPriceQuantityTextView: TextView,
                                  product: ProductItem) {
        var currentQuantity = quantityEditText.text.toString().toInt()
        if (currentQuantity > 1) {
            currentQuantity--
        }
        quantityEditText.text = currentQuantity.toString()
        productPriceQuantityTextView.text = adjustPrice(quantityEditText, product)
    }

    private fun incrementQuantity(quantityEditText: TextView,
                                  productPriceQuantityTextView: TextView,
                                  product: ProductItem) {
        var currentQuantity = quantityEditText.text.toString().toInt()
        currentQuantity++
        quantityEditText.text = currentQuantity.toString()
        productPriceQuantityTextView.text = adjustPrice(quantityEditText, product)
    }

    private fun adjustPrice(quantityEditText: TextView,
                            product: ProductItem) : String {
        val priceInt = product.price.dropLast(2).toInt()
        val quantityInt = quantityEditText.text.toString().toInt()
        val priceQuantity = priceInt * quantityInt

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