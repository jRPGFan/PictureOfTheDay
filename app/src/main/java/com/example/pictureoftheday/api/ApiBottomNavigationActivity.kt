package com.example.pictureoftheday.api

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pictureoftheday.R
import com.example.pictureoftheday.api.epic.EarthFragment
import com.example.pictureoftheday.api.marsrover.MarsFragment
import kotlinx.android.synthetic.main.activity_api_bottom_navigation.*

class ApiBottomNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_api_bottom_navigation)
        apiBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.apiBottomEarth -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.apiBottomNavigationContainer, EarthFragment()).commit()
                    true
                }
                R.id.apiBottomMars -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.apiBottomNavigationContainer, MarsFragment()).commit()
                    true
                }
                R.id.apiBottomWeather -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.apiBottomNavigationContainer, WeatherFragment()).commit()
                    true
                }
                else -> false
            }
        }
        apiBottomNavigation.selectedItemId = R.id.apiBottomEarth
    }
}