package com.pmm.wonderlast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(){
    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        bottomNavigationView = findViewById(R.id.footbar)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Ganti fragment dengan HomeFragment
                    replaceFragment(HomeFragment())
                    item.setIcon(R.drawable.home_icon_active)
                    // Reset ikon yang lain
                    bottomNavigationView.menu.findItem(R.id.navigation_mark).setIcon(R.drawable.bookmark_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_plan).setIcon(R.drawable.plan_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_icon)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_mark -> {
                    // Ganti fragment dengan HomeFragment
                    replaceFragment(MarkFragment())
                    item.setIcon(R.drawable.bookmark_icon_active)
                    bottomNavigationView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_plan).setIcon(R.drawable.plan_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_icon)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_plan -> {
                    // Ganti fragment dengan HomeFragment
                    replaceFragment(PlanFragment())
                    item.setIcon(R.drawable.plan_icon_active)
                    bottomNavigationView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_mark).setIcon(R.drawable.bookmark_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_icon)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_profile -> {
                    // Ganti fragment dengan HomeFragment
                    replaceFragment(ProfileFragment())
                    item.setIcon(R.drawable.profile_icon_active)
                    bottomNavigationView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_mark).setIcon(R.drawable.bookmark_icon)
                    bottomNavigationView.menu.findItem(R.id.navigation_plan).setIcon(R.drawable.plan_icon)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
