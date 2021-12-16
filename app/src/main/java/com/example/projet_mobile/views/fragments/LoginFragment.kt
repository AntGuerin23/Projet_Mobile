package com.example.projet_mobile.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.example.projet_mobile.modals.Hasher
import com.example.projet_mobile.modals.TableConverter
import com.example.projet_mobile.modals.UserCreator
import com.example.projet_mobile.views.activities.LoginActivity
import com.example.projet_mobile.views.activities.MainActivity
import java.sql.PreparedStatement

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tvSignUp).setOnClickListener {
            (activity as LoginActivity).changeFragment(SignUpFragment())
        }
        view.findViewById<Button>(R.id.bLogin).setOnClickListener {
            val intent = Intent(activity, MainActivity :: class.java)
            if (login()) {
                startActivity(intent)
            }
        }
    }

    private fun login() : Boolean {
        var valid = false
        var userData :  ArrayList<HashMap<String, String>>? = null
        if (passwordsMatch()) {
            userData = authenticate()
            if (userData.size == 0) {
                Toast.makeText(requireView().context, "Wrong username or password", Toast.LENGTH_SHORT).show()
            } else {
                UserCreator.createUser(userData.get(0))
                valid = true
            }
        } else {
            Toast.makeText(requireView().context, "The passwords do not match", Toast.LENGTH_SHORT).show()
        }
        return valid
    }

    private fun passwordsMatch() : Boolean {
        return getPassword() == getPasswordConfirmation()
    }

    private fun authenticate(): ArrayList<HashMap<String, String>> {
        val statement: PreparedStatement = Database.connectDB()!!
            .prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")
        statement.setString(1, getEmail())
        statement.setString(2, Hasher.hashString(getPassword()))
        val result = Database.preparedQuery(statement)
        return TableConverter.getRows(result)
    }

    private fun getEmail() : String {
        return requireView().findViewById<EditText>(R.id.etEmail).text.toString().trim()
    }

    private fun getPassword() : String {
        return requireView().findViewById<EditText>(R.id.etPassword).text.toString().trim()
    }

    private fun getPasswordConfirmation() : String {
        return requireView().findViewById<EditText>(R.id.etConfirmPassword).text.toString().trim()
    }
}