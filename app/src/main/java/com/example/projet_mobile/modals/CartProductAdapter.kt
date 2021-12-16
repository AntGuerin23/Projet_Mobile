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
    private val productList: ArrayList<ProductItem>
    ) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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

        val currentProduct = getItem(position) as ProductItem

        val productImageImageView = rowView.findViewById<ImageView>(R.id.ivProductImage)
        val productNameTextView = rowView.findViewById<TextView>(R.id.tvProductName)
        val productPriceTextView = rowView.findViewById<TextView>(R.id.tvProductPrice)
        val quantityEditText = rowView.findViewById<EditText>(R.id.etQuantity)
        val productPriceQuantityTextView = rowView.findViewById<TextView>(R.id.tvProductPriceQuantity)

        productImageImageView.setImageResource(currentProduct.imageResource)
        productNameTextView.text = currentProduct.name
        productPriceTextView.text = currentProduct.price
        productPriceQuantityTextView.text = adjustPrice(quantityEditText, currentProduct)

        val decrementButton = rowView.findViewById<Button>(R.id.bDecrement)
        val incrementButton = rowView.findViewById<Button>(R.id.bIncrement)
        decrementButton.setOnClickListener {
            decrementQuantity(quantityEditText,
                productPriceQuantityTextView, currentProduct)
        }

        incrementButton.setOnClickListener {
            incrementQuantity(quantityEditText,
                productPriceQuantityTextView, currentProduct)
        }

        return rowView
    }

    private fun decrementQuantity(quantityEditText: EditText,
                                  productPriceQuantityTextView: TextView,
                                  product: ProductItem) {
        var currentQuantity = quantityEditText.text.toString().toInt()
        if (currentQuantity > 1) {
            currentQuantity--
        }
        quantityEditText.setText(currentQuantity.toString())
        productPriceQuantityTextView.text = adjustPrice(quantityEditText, product)
    }

    private fun incrementQuantity(quantityEditText: EditText,
                                  productPriceQuantityTextView: TextView,
                                  product: ProductItem) {
        var currentQuantity = quantityEditText.text.toString().toInt()
        currentQuantity++
        quantityEditText.setText(currentQuantity.toString())
        productPriceQuantityTextView.text = adjustPrice(quantityEditText, product)
    }

    private fun adjustPrice(quantityEditText: EditText,
                            product: ProductItem) : String {
        val priceInt = product.price.dropLast(2).toInt()
        val quantityInt = quantityEditText.text.toString().toInt()
        val priceQuantity = priceInt * quantityInt
        return "$priceQuantity $"
    }
}