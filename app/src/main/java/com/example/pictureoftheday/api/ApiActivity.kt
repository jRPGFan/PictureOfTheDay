package com.example.pictureoftheday.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.pictureoftheday.R
import kotlinx.android.synthetic.main.activity_api.*

private const val EARTH = 0
private const val MARS = 1
private const val WEATHER = 2

class ApiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)
        viewPager.adapter = ViewPageAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        setHighlightedTab(EARTH)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                setHighlightedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }

    private fun setHighlightedTab(position: Int) {
        val layoutInflater = LayoutInflater.from(this@ApiActivity)
        tabLayout.getTabAt(0)?.customView = null
        tabLayout.getTabAt(1)?.customView = null
        tabLayout.getTabAt(2)?.customView = null

        when (position) {
            EARTH -> {
                setEarthTabHighlighted(layoutInflater)
            }
            MARS -> {
                setMarsTabHighlighted(layoutInflater)
            }
            WEATHER -> {
                setWeatherTabHighlighted(layoutInflater)
            }
            else -> {
                setEarthTabHighlighted(layoutInflater)
            }
        }
    }

    private fun setEarthTabHighlighted(layoutInflater: LayoutInflater) {
        val earth = layoutInflater.inflate(R.layout.earth_custom_tab, null)
        earth.findViewById<AppCompatTextView>(R.id.tabImageTV).setTextColor(
            ContextCompat.getColor(this@ApiActivity, R.color.colorAccent)
        )
        tabLayout.getTabAt(EARTH)?.customView = earth
        tabLayout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.mars_custom_tab, null)
        tabLayout.getTabAt(WEATHER)?.customView =
            layoutInflater.inflate(R.layout.weather_custom_tab, null)
    }

    private fun setMarsTabHighlighted(layoutInflater: LayoutInflater) {
        val mars = layoutInflater.inflate(R.layout.mars_custom_tab, null)
        mars.findViewById<AppCompatTextView>(R.id.tabImageTV).setTextColor(
            ContextCompat.getColor(this@ApiActivity, R.color.colorAccent)
        )
        tabLayout.getTabAt(EARTH)?.customView =
            layoutInflater.inflate(R.layout.earth_custom_tab, null)
        tabLayout.getTabAt(MARS)?.customView = mars
        tabLayout.getTabAt(WEATHER)?.customView =
            layoutInflater.inflate(R.layout.weather_custom_tab, null)
    }

    private fun setWeatherTabHighlighted(layoutInflater: LayoutInflater) {
        val weather = layoutInflater.inflate(R.layout.weather_custom_tab, null)
        weather.findViewById<AppCompatTextView>(R.id.tabImageTV).setTextColor(
            ContextCompat.getColor(this@ApiActivity, R.color.colorAccent)
        )
        tabLayout.getTabAt(EARTH)?.customView =
            layoutInflater.inflate(R.layout.earth_custom_tab, null)
        tabLayout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.mars_custom_tab, null)
        tabLayout.getTabAt(WEATHER)?.customView = weather
    }
}