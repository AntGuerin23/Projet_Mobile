package com.example.projet_mobile.views.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.PictureConverter
import com.example.projet_mobile.modals.User
import java.sql.PreparedStatement

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @SuppressLint("WrongThread", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeImageView()
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedImage : Uri? = data?.data;
                val imageView = view.findViewById<ImageView>(R.id.ivProfilePic)
                imageView.setImageURI(selectedImage);
                val byteArray = PictureConverter.convertDrawableToByteArray(imageView.drawable)
                val statement : PreparedStatement = Database.connectDB()!!.prepareStatement("UPDATE users SET picture = ? WHERE email = ?")
                statement.setBytes(1, byteArray)
                statement.setString(2, User.email)
                Database.update(statement)
                User.picture = byteArray
            }
        }

        val changePictureButton  = view.findViewById<Button>(R.id.bChangeImage)
        changePictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResult.launch(intent)
        }
    }

    private fun initializeImageView() {
        val decodedBitmap = BitmapFactory.decodeByteArray(User.picture, 0, User.picture!!.size)
        val drawable = BitmapDrawable(resources, decodedBitmap)
        val imageView = requireView().findViewById<ImageView>(R.id.ivProfilePic)
        imageView.setImageDrawable(drawable)
    }
}
