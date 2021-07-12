package com.example.pictureoftheday.api.epic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.EarthCustomTabBinding
import com.example.pictureoftheday.databinding.FragmentEarthBinding
import com.example.pictureoftheday.utilities.showSnackbar
import com.google.android.material.snackbar.Snackbar

class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EarthFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(EarthFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
//        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
        return binding.root
    }

    private fun renderData(data: EPICData) {
        when (data) {
            is EPICData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.image
                if (url.isNullOrEmpty()) {
                    binding.root.showSnackbar("Url is empty", Snackbar.LENGTH_SHORT)
                } else {
                    binding.root.showSnackbar("Loading successful", Snackbar.LENGTH_SHORT)
                    val dateArray =
                        serverResponseData.date?.substring(0, serverResponseData.date.indexOf(" "))
                            ?.replace("-", "/")

                    binding.epicView.load(
                        String.format(
                            "https://epic.gsfc.nasa.gov/archive/natural/{0}/jpg/{1}.jpg",
                            dateArray,
                            url
                        )
                    ) {
                        lifecycle(this@EarthFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.epicDescription.text = serverResponseData.caption
                }
            }
            is EPICData.Loading -> {
            }
            is EPICData.Error -> data.error.message?.let {
                binding.root.showSnackbar(
                    it,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }
}