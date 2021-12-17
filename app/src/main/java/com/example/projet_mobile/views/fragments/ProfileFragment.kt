package com.example.projet_mobile.views.fragments
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.PictureConverter
import com.example.projet_mobile.modals.User
import java.sql.PreparedStatement
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile), LocationListener {

    private lateinit var locationManager : LocationManager

    @SuppressLint("WrongThread", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        initializeImageView()
        changePicture(view)
        manageLocation()
    }

    private fun initializeImageView() {
        val decodedBitmap = BitmapFactory.decodeByteArray(User.picture, 0, User.picture.size)
        val drawable = BitmapDrawable(resources, decodedBitmap)
        val imageView = requireView().findViewById<ImageView>(R.id.ivProfilePic)
        imageView.setImageDrawable(drawable)
    }

    private fun manageLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
        }
        locationManager = (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!;
        locationEnabled();
        getLocation();
    }

    private fun locationEnabled() {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            networkEnabled = lm!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!gpsEnabled && !networkEnabled) {
            AlertDialog.Builder(requireActivity())
                .setTitle("Enable GPS Service")
                .setMessage("We need your GPS location to show Near Places around you.")
                .setCancelable(false)
                .setPositiveButton("Enable",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun getLocation() {
        try {
            locationManager = (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5f, (this as LocationListener))
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun changePicture(view : View) {
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

    override fun onLocationChanged(location : Location) {
        try {
            val geocoder =
                Geocoder(context, Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
//            tvCity.setText(addresses[0].getLocality())
//            tvState.setText(addresses[0].getAdminArea())
//            tvCountry.setText(addresses[0].getCountryName())
//            tvPin.setText(addresses[0].getPostalCode())
            requireView().findViewById<TextView>(R.id.tvAddress).text = addresses[0].getAddressLine(0)
            requireView().findViewById<TextView>(R.id.tvCity).text = addresses[0].locality

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
