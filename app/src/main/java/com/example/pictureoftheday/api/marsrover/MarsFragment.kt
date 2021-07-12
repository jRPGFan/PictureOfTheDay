package com.example.pictureoftheday.api.marsrover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentMarsBinding
import com.example.pictureoftheday.utilities.showSnackbar
import com.google.android.material.snackbar.Snackbar

class MarsFragment : Fragment() {
    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MarsFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(MarsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
        return binding.root
    }

    private fun renderData(data: MarsData) {
        when (data) {
            is MarsData.Success -> {
                val serverResponseData = data.serverResponseData.latest_photos[0]
                val url = serverResponseData?.img_src
                if (url.isNullOrEmpty()) {
                    binding.root.showSnackbar("Url is empty", Snackbar.LENGTH_SHORT)
                } else {
                    binding.root.showSnackbar("Loading successful", Snackbar.LENGTH_SHORT)
                    binding.marsView.load(url) {
                        lifecycle(this@MarsFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.earthDate.text = serverResponseData.earth_date
                }
            }
            is MarsData.Loading -> {
            }
            is MarsData.Error -> data.error.message?.let {
                binding.root.showSnackbar(
                    it,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }
}