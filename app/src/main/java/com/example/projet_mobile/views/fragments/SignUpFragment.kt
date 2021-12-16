package com.example.projet_mobile.views.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.*
import com.example.projet_mobile.views.activities.LoginActivity
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.PreparedStatement

class SignUpFragment : Fragment(R.layout.fragment_sign_up)  {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            (activity as LoginActivity).changeFragment(LoginFragment())
        }
        view.findViewById<Button>(R.id.bSignUp).setOnClickListener {
            val intent = Intent(activity, MainActivity :: class.java)
            if (fieldsAreFilled() && passwordsMatch() && emailIsValid()) {
                signUp()
                startActivity(intent)
            }
        }
    }

    private fun emailIsValid() : Boolean {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("SELECT * FROM users WHERE email = ?")
        statement.setString(1, getEmail())
        val data = TableConverter.getRows(Database.preparedQuery(statement))
        if (data.size != 0) {
            Toast.makeText(requireView().context, "This email is already taken.", Toast.LENGTH_SHORT).show()

        }
        return data.size == 0
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun signUp() {
        val defaultDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.default_pic)
        if (defaultDrawable != null) {
            UserCreator.createUser(getEmail(), getFirstname(), getLastname(), getPassword(), defaultDrawable)
        }
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("INSERT INTO users (email, firstname, lastname, password, picture) VALUES (?, ?, ?, ?, ?)")
        statement.setString(1, getEmail())
        statement.setString(2, getFirstname())
        statement.setString(3, getLastname())
        statement.setString(4, Hasher.hashString(getPassword()))
        statement.setBytes(5, PictureConverter.convertDrawableToByteArray(requireContext().resources.getDrawable(R.drawable.default_pic)))
        Database.update(statement)
    }

    private fun passwordsMatch() : Boolean {
        val valid = getPassword() == getPasswordConfirmation()
        if (!valid) {
            Toast.makeText(requireView().context, "The passwords do not match.", Toast.LENGTH_SHORT).show()
        }
        return valid
    }

    private fun fieldsAreFilled() : Boolean {
        val valid = getEmail().isNotEmpty() && getFirstname().isNotEmpty()
                && getLastname().isNotEmpty() && getPassword().isNotEmpty()
        if (!valid) {
            Toast.makeText(requireView().context, "Please fill every field.", Toast.LENGTH_SHORT).show()

        }
        return valid
    }

    private fun getEmail() : String {
        return requireView().findViewById<EditText>(R.id.etEmail).text.toString().trim()
    }

    private fun getFirstname() : String {
        return requireView().findViewById<EditText>(R.id.etFirstname).text.toString().trim()
    }

    private fun getLastname() : String {
        return requireView().findViewById<EditText>(R.id.etLastname).text.toString().trim()
    }

    private fun getPassword() : String {
        return requireView().findViewById<EditText>(R.id.etPassword).text.toString().trim()
    }

    private fun getPasswordConfirmation() : String {
        return requireView().findViewById<EditText>(R.id.etConfirmPassword).text.toString().trim()
    }
}