package com.example.pictureoftheday

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {


//    private var _binding: FragmentSettingsBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.chipGroup.setOnCheckedChangeListener { _, position ->
//            binding.chipGroup.findViewById<Chip>(position)?.let {
//                Toast.makeText(context, "${it.text} was selected", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.chipClose.setOnCloseIconClickListener {
//            Toast.makeText(context, "Close is clicked", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(sharedPreferencesFile, Context.MODE_PRIVATE)

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()
            }
        }

        chipClose.setOnCloseIconClickListener {
            Toast.makeText(context, "Close is clicked", Toast.LENGTH_SHORT).show()
        }

        themeChanger.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            if (sharedPreferences.getInt(
                    THEME_PREFERENCE, R.style.Theme_PictureOfTheDay) == R.style.Theme_PictureOfTheDay
            )
                editor.putInt(THEME_PREFERENCE, R.style.Default).apply()
            else
                editor.putInt(THEME_PREFERENCE, R.style.Theme_PictureOfTheDay).apply()
            requireActivity().recreate()
        }
    }
}