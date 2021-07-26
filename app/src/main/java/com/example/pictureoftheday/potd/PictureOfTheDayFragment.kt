package com.example.pictureoftheday.potd

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.transition.*
import android.view.*
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.load
import com.example.pictureoftheday.MainActivity
import com.example.pictureoftheday.R
import com.example.pictureoftheday.ScrollingFragment
import com.example.pictureoftheday.api.ApiActivity
import com.example.pictureoftheday.api.ApiBottomNavigationActivity
import com.example.pictureoftheday.settings.SettingsFragment
import com.example.pictureoftheday.databinding.FragmentMainStartBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_animation_enlarge.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {
    private var isEnlarged = false
    private var _binding: FragmentMainStartBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
            .observe(viewLifecycleOwner, { renderData(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)
        setBottomAppBar(view)
        activity?.let {
            binding.chipToday.typeface =
                Typeface.createFromAsset(it.assets, "fonts/DroidRobotRegular-DBWx.ttf")
            val spannable = SpannableString(getString(R.string.day_before))
            spannable.setSpan(
                ForegroundColorSpan(Color.CYAN), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                StrikethroughSpan(), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                UnderlineSpan(), 4, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.chipDayBefore.text = spannable
            binding.chipYesterday.typeface = Typeface.createFromAsset(it.assets, "fonts/gantz.ttf")
        }

        binding.chipToday.setOnClickListener {
            viewModel.getData()
                .observe(viewLifecycleOwner, { renderData(it) })
        }

        binding.chipYesterday.setOnClickListener {
            viewModel.getData(getDate(-1))
                .observe(viewLifecycleOwner, { renderData(it) })
        }

        binding.chipDayBefore.setOnClickListener {
            viewModel.getData(getDate(-2))
                .observe(viewLifecycleOwner, { renderData(it) })
        }

        binding.imageView.setOnClickListener {
            isEnlarged = !isEnlarged
            TransitionManager.beginDelayedTransition(
                binding.main, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )
            val parameters: ViewGroup.LayoutParams = binding.imageView.layoutParams
            parameters.height =
                if (isEnlarged) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            binding.imageView.layoutParams = parameters
            binding.imageView.scaleType =
                if (isEnlarged) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> activity?.let {
                startActivity(
                    Intent(
                        it,
                        ApiBottomNavigationActivity::class.java
                    )
                )
            }
            R.id.app_bar_search -> snackbar("Search")
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(
                        it.supportFragmentManager,
                        "tag"
                    )
                }
            }
            R.id.app_bar_settings ->
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.container,
                    SettingsFragment()
                )?.addToBackStack(null)?.commit()
            R.id.app_bar_api -> activity?.let { startActivity(Intent(it, ApiActivity::class.java)) }
            R.id.scrollingFragment -> activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.container,
                ScrollingFragment()
            )?.addToBackStack(null)?.commit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    snackbar("Url is empty")
                } else {
                    snackbar("Loading successful")
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    serverResponseData.explanation?.let {
                        binding.imageText.text = it
                    }
                }
            }

            is PictureOfTheDayData.Loading -> {

            }
            is PictureOfTheDayData.Error -> {
                snackbar(data.error.message)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(days: Int): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DATE, days)
        return dateFormat.format(cal.time)
    }

    private fun Fragment.snackbar(message: String?) {
        view?.let { Snackbar.make(it, message.toString(), Snackbar.LENGTH_SHORT).show() }
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}