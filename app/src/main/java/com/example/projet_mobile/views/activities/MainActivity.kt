package com.example.projet_mobile.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.projet_mobile.views.fragments.CartFragment
import com.example.projet_mobile.views.fragments.MainPageFragment
import com.example.projet_mobile.views.fragments.ProfileFragment
import com.example.projet_mobile.R
import com.example.projet_mobile.modals.Database
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView : NavigationView
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Database()
        db.testQuery()

        changeFragment(MainPageFragment())
        initializeDrawerMenu()
        initalizeDrawerNavigation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeDrawerMenu() {
        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initalizeDrawerNavigation() {
        navigationView = findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.iMainPage -> changeFragment(MainPageFragment())
                R.id.iProfile -> changeFragment(ProfileFragment())
                R.id.iCart -> changeFragment(CartFragment())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        currentFragment = fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, currentFragment)
            commit()
        }
    }
}