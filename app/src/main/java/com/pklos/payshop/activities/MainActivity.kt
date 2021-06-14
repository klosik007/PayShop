package com.pklos.payshop.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pklos.payshop.R
import com.pklos.payshop.db.AppDb
import com.pklos.payshop.fragments.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavView.setOnNavigationItemSelectedListener(bottomNavListener)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.view_pager, HomeFragment())
            .commit()
    }

    private val bottomNavListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            lateinit var selectedFragment: Fragment
            when(it.itemId){
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_search -> selectedFragment = SearchFragment()
                R.id.nav_favorites -> selectedFragment = FavoritesFragment()
                R.id.nav_basket -> selectedFragment = BasketFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.view_pager, selectedFragment).commit()
            true
        }
}