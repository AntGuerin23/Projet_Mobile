package com.example.projet_mobile.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projet_mobile.R
import androidx.fragment.app.Fragment
import com.example.projet_mobile.views.fragments.LoginFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        changeFragment(LoginFragment())
    }

    fun changeFragment(fragment: Fragment) {
        currentFragment = fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, currentFragment)
            commit()
        }
    }
}