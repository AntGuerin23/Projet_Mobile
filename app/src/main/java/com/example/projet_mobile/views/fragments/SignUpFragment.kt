package com.example.projet_mobile.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projet_mobile.R
import com.example.projet_mobile.views.activities.LoginActivity
import com.example.projet_mobile.views.activities.MainActivity

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            (activity as LoginActivity).changeFragment(LoginFragment())
        }

        view.findViewById<Button>(R.id.bSignUp).setOnClickListener {
            val intent = Intent(activity, MainActivity :: class.java)
            startActivity(intent)
        }
    }
}