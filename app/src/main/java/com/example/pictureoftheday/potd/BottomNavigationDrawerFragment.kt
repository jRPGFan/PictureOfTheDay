package com.example.pictureoftheday.potd

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pictureoftheday.animations.AnimationSandboxActivity
import com.example.pictureoftheday.R
import com.example.pictureoftheday.animations.ConstraintSetAnimationActivity
import com.example.pictureoftheday.databinding.BottomNavigationLayoutBinding
import com.example.pictureoftheday.recycler.RecyclerActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> activity?.let {
                    startActivity(Intent(it, AnimationSandboxActivity::class.java))
                }
                R.id.navigation_two -> activity?.let {
                    startActivity(Intent(it, ConstraintSetAnimationActivity::class.java))
                }
                R.id.navigationThree -> activity?.let {
                    startActivity(Intent(it, RecyclerActivity::class.java))
                }
            }
            dismiss()
            true
        }
    }
}