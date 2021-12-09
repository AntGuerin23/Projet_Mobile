package com.example.projet_mobile.views.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.TableConverter
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @SuppressLint("WrongThread", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedImage : Uri? = data?.data;
                val imageView = view.findViewById<ImageView>(R.id.ivProfilePic)
                imageView.setImageURI(selectedImage);

                val picture = imageView.drawable
                val bitmap = picture!!.toBitmap()
                val buffer = ByteArrayOutputStream(bitmap.width * bitmap.height)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
                val byteArray =  buffer.toByteArray()
                Database.updateImage(byteArray, 3)
                //TODO : Update image var inside user
            }
        }

        val changePictureButton  = view.findViewById<Button>(R.id.bChangeImage)
        changePictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResult.launch(intent)
        }

        //Encode drawable
//        val picture = ResourcesCompat.getDrawable(resources, R.drawable.chinese_man, null)
//        val bitmap = picture!!.toBitmap()
//        val buffer = ByteArrayOutputStream(bitmap.width * bitmap.height)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
//        val byteArray =  buffer.toByteArray()

        //Get image from DB
        val dbByteArray = TableConverter.getUserImage(3); //TODO : Image should already be instanciated inside a User object, should not have a query here.
        val decodedBitmap = BitmapFactory.decodeByteArray(dbByteArray, 0, dbByteArray!!.size)
        val drawable = BitmapDrawable(resources, decodedBitmap)
        val imageView = view.findViewById<ImageView>(R.id.ivProfilePic)
        imageView.setImageDrawable(drawable)
    }
}
