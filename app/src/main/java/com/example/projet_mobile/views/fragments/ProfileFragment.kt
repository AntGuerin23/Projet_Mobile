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
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.*
import java.sql.PreparedStatement
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile), AdapterView.OnItemSelectedListener {

    @SuppressLint("WrongThread", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        initializeImageView()
        changePicture(view)

        val spinner = view.findViewById<Spinner>(R.id.sProvince)
        val customSpinnerAdapter = ProvinceSpinnerAdapter(requireActivity(), getProvince())
        spinner.adapter = customSpinnerAdapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        User.province = getProvinceFromPosition(pos)
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("UPDATE users SET province_id = ? WHERE user_id = ?")
        statement.setInt(1, pos)
        statement.setInt(2, User.id)
        Database.update(statement)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    private fun initializeImageView() {
        val decodedBitmap = BitmapFactory.decodeByteArray(User.picture, 0, User.picture.size)
        val drawable = BitmapDrawable(resources, decodedBitmap)
        val imageView = requireView().findViewById<ImageView>(R.id.ivProfilePic)
        imageView.setImageDrawable(drawable)
    }

    private fun getProvinceFromPosition(pos : Int) : Province {
        val provinces = Province.values()
        provinces.forEach { province ->
            if (province.id == pos) {
                return province
            }
        }
        return Province.QUEBEC
    }







    private fun changePicture(view : View) {
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedImage : Uri? = data?.data
                val imageView = view.findViewById<ImageView>(R.id.ivProfilePic)
                imageView.setImageURI(selectedImage)
                val byteArray = PictureConverter.convertDrawableToByteArray(imageView.drawable)
                val statement : PreparedStatement = Database.connectDB()!!.prepareStatement("UPDATE users SET picture = ? WHERE email = ?")
                statement.setBytes(1, byteArray)
                statement.setString(2, User.email)
                Database.update(statement)
                User.picture = byteArray
            }
        }
        val changePictureButton  = view.findViewById<Button>(R.id.bCamera)
        changePictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResult.launch(intent)
        }
    }



    private fun getProvince() : ArrayList<ProvinceSpinnerItem> {
        val provincesList = ArrayList<ProvinceSpinnerItem>()
        provincesList.add(ProvinceSpinnerItem("Quebec"))
        provincesList.add(ProvinceSpinnerItem("Ontario"))
        provincesList.add(ProvinceSpinnerItem("British Columbia"))
        provincesList.add(ProvinceSpinnerItem("Alberta"))
        provincesList.add(ProvinceSpinnerItem("Saskatchewan"))
        provincesList.add(ProvinceSpinnerItem("Manitoba"))
        provincesList.add(ProvinceSpinnerItem("Yukon"))
        provincesList.add(ProvinceSpinnerItem("Newfoundland and Labrador"))
        provincesList.add(ProvinceSpinnerItem("New Brunswick"))
        provincesList.add(ProvinceSpinnerItem("Nova Scotia"))
        provincesList.add(ProvinceSpinnerItem("Prince Edward Island"))
        return provincesList
    }
}
