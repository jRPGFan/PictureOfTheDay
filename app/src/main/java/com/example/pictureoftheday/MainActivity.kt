package com.example.pictureoftheday

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StyleRes

const val sharedPreferencesFile = "PODsharedpreference"
const val THEME_PREFERENCE = "themePref"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)
        setTheme(sharedPreferences.getInt(THEME_PREFERENCE, R.style.Theme_PictureOfTheDay))
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}