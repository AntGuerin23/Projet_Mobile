package com.example.projet_mobile.views.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.*
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList

class FormFragment : Fragment(), AdapterView.OnItemSelectedListener, LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var mainView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner>(R.id.sProvince)
        mainView = view
        val customSpinnerAdapter = ProvinceSpinnerAdapter(requireActivity(), getProvince())
        spinner.adapter = customSpinnerAdapter
        spinner.onItemSelectedListener = this
        manageLocation()
        spinner.setSelection(User.province.id)
        view.findViewById<Button>(R.id.bConfirm).setOnClickListener {
            (activity as MainActivity).changeFragment(ConfirmationFragment())
            writeToDB()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        User.province = getProvinceFromPosition(pos)
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("UPDATE users SET province_id = ? WHERE user_id = ?")
        statement.setInt(1, pos)
        statement.setInt(2, User.id)
        Database.update(statement)
    }

    override fun onLocationChanged(location: Location) {
        try {
            val geocoder =
                Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            initializeFields(addresses[0])
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    private fun getProvinceFromPosition(pos: Int): Province {
        val provinces = Province.values()
        provinces.forEach { province ->
            if (province.id == pos) {
                return province
            }
        }
        return Province.QUEBEC
    }

    private fun getProvince(): ArrayList<ProvinceSpinnerItem> {
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

    private fun initializeFields(address: Address) {
        mainView.findViewById<EditText>(R.id.etCity).setText(address.locality)
        mainView.findViewById<EditText>(R.id.etAddress)
            .setText(address.getAddressLine(0).toString().substringBefore(","))
        mainView.findViewById<EditText>(R.id.etPostalCode).setText(address.postalCode)
    }

    private fun writeToDB() {
        val orderId = createOrder()
        createItems(orderId)
        deleteCart()
    }

    private fun createOrder(): Int {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement(
                "INSERT INTO orders (user_id, province, city, address, zip_code, total) VALUES (?, ?, ?, ?, ?, ?) RETURNING order_id",
                Statement.RETURN_GENERATED_KEYS
            )
        statement.setInt(1, Integer.valueOf(User.id.toString()))
        statement.setString(2, User.province.provinceName)
        statement.setString(3, mainView.findViewById<EditText>(R.id.etCity).text.toString())
        statement.setString(4, mainView.findViewById<EditText>(R.id.etAddress).text.toString())
        statement.setString(5, mainView.findViewById<EditText>(R.id.etPostalCode).text.toString())
        statement.setDouble(6, User.total)
        Database.update(statement)
        statement.generatedKeys.next()
        return statement.generatedKeys.getInt(1)
    }

    private fun createItems(orderId: Int) {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("SELECT * FROM cart_items ci JOIN products p ON ci.id_products = p.product_id WHERE ci.id_user = ?")
        statement.setInt(1, User.id)
        val results: ResultSet? = Database.preparedQuery(statement)
        val resultTable = TableConverter.getRows(results)
        for (product in resultTable) {
            val insertStatement: PreparedStatement = Database.connectDB()!!
                .prepareStatement("INSERT INTO order_items (price, quantity, order_id, product_id) VALUES (?, ?, ?, ?)")
            insertStatement.setDouble(1, product["price"]!!.toDouble())
            insertStatement.setInt(2, Integer.valueOf(product["quantity"].toString()))
            insertStatement.setInt(3, orderId)
            insertStatement.setInt(4, Integer.valueOf(product["product_id"].toString()))
            Database.update(insertStatement)
        }
    }

    private fun deleteCart() {
        val deleteStatement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("DELETE FROM cart_items WHERE id_user = ?")
        deleteStatement.setInt(1, User.id)
        Database.update(deleteStatement)
    }

    private fun locationEnabled() {
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
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
            AlertDialog.Builder(requireContext())
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
            locationManager =
                (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                50000f,
                (this as LocationListener)
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
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
        locationManager =
            (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
        locationEnabled()
        getLocation()
    }
}