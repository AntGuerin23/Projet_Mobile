package com.example.projet_mobile.modals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.projet_mobile.R


class ProvinceSpinnerAdapter(
    val context: Context,
    private var provinces: ArrayList<ProvinceSpinnerItem>
) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_dropdown_layout, parent, false)
            vh = ItemHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }

        vh.label.text = provinces[position].provinceName
        vh.img.setBackgroundResource(R.drawable.ic_province)

        return view
    }

    override fun getItem(position: Int): Any {
        return provinces[position]
    }

    override fun getCount(): Int {
        return provinces.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val img: ImageView

        init {
            label = row?.findViewById(R.id.tvSpinnerText) as TextView
            img = row.findViewById(R.id.ivSpinnerIcon) as ImageView
        }
    }

}