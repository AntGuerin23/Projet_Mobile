package com.example.projet_mobile.views.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.ProvinceSpinnerAdapter
import com.example.projet_mobile.modals.ProvinceSpinnerItem
import com.example.projet_mobile.views.activities.MainActivity

class FormFragment : Fragment(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.sProvince)
        val customSpinnerAdapter = ProvinceSpinnerAdapter(requireActivity(), getProvince())
        spinner.adapter = customSpinnerAdapter
        spinner.onItemSelectedListener = this

        view.findViewById<Button>(R.id.bConfirm).setOnClickListener {
            (activity as MainActivity).changeFragment(ConfirmationFragment())
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // Update province in user db
        // Call everytime the fragment is loaded...
        Toast.makeText(requireActivity(), "Province selected: " + parent.getItemAtPosition(pos) + " at position: " + pos, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Called when user selects nothing... but when does that happen?
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