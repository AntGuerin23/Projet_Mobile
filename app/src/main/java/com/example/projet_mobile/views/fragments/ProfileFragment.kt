package com.example.projet_mobile.views.fragments

import androidx.fragment.app.Fragment
import com.example.projet_mobile.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {

}

//Encode
//var picture = requireActivity().resources.getDrawable(R.drawable.chinese_man);
//var bitmap = picture!!.toBitmap()
//val buffer = ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight())
//bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
//val byteArray =  buffer.toByteArray()
//
//val DBByteArray = TableConverter.getImage(Database.query("SELECT * FROM users WHERE user_id = 3"));
//
////Decode
//var decodedBitmap = BitmapFactory.decodeByteArray(DBByteArray, 0, byteArray.size)
//
//var drawable = BitmapDrawable(getResources(), decodedBitmap)
//
//var imageView = view.findViewById<ImageView>(R.id.imageView)
//Log.d("TAG", "onCreate: " + imageView)
//imageView.setImageDrawable(drawable)