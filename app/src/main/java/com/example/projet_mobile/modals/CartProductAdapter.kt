package com.example.projet_mobile.modals

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
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

        val productImageImageView = rowView.findViewById(R.id.ivProductImage) as ImageView
        val productNameTextView = rowView.findViewById(R.id.tvProductName) as TextView
        val productPriceTextView = rowView.findViewById(R.id.tvProductPrice) as TextView

        val currentProduct = getItem(position) as ProductItem
        productImageImageView.setImageResource(currentProduct.imageResource)
        productNameTextView.text = currentProduct.name
        productPriceTextView.text = currentProduct.price

        return rowView
    }
}