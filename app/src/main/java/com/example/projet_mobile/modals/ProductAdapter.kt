package com.example.projet_mobile.modals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_mobile.R

class ProductAdapter(private val productList: List<ProductItem>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_item, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]

        holder.productImage.setImageResource(currentItem.imageResource)
        holder.productName.text = currentItem.name
        holder.productDescription.text = currentItem.description
        holder.productPrice.text = currentItem.price
    }

    override fun getItemCount() = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val productDescription: TextView = itemView.findViewById(R.id.tvProductDescription)
        val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
    }
}