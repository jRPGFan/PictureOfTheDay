package com.example.pictureoftheday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.databinding.FragmentChipsBinding
import com.google.android.material.chip.Chip

class ChipsFragment : Fragment() {

    private var _binding: FragmentChipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener { _, position ->
            binding.chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "${it.text} was selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.chipClose.setOnCloseIconClickListener {
            Toast.makeText(context, "Close is clicked", Toast.LENGTH_SHORT).show()
        }
    }
}