package dev.ohjiho.budgetify.setup

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.setup.databinding.FragmentWelcomeAndSetUpCurrencyBinding
import dev.ohjiho.budgetify.utils.ui.ScreenMetricsCompat
import dev.ohjiho.budgetify.utils.ui.navigateTo
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

internal class WelcomeAndSetUpCurrencyFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentWelcomeAndSetUpCurrencyBinding

    private var prevScreen: SetUpScreen? = null

    // Resources
    private var backgroundStartGuidelineHeight by Delegates.notNull<Int>()
    private var backgroundEndGuidelineHeight by Delegates.notNull<Int>()

    private val welcomeNextButtonText by lazy { resources.getString(R.string.fragment_welcome_next_button) }
    private val currencyNextButtonText by lazy { resources.getString(R.string.fragment_set_up_next_button) }

    // Animations
    private val backgroundGuidelineAnimator by lazy {
        val startGuidelineAnimator = ValueAnimator.ofInt(backgroundStartGuidelineHeight, 0).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundStartGuideline.setGuidelineBegin(it.animatedValue as Int)
            }
        }
        val endGuidelineAnimator = ValueAnimator.ofInt(backgroundEndGuidelineHeight, 0).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundEndGuideline.setGuidelineBegin(it.animatedValue as Int)
            }
        }
        AnimatorSet().apply {
            playTogether(startGuidelineAnimator, endGuidelineAnimator)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ScreenMetricsCompat.getScreenSize(requireActivity().applicationContext).height.let {
            backgroundStartGuidelineHeight = (it * BACKGROUND_START_GUIDELINE_HEIGHT).toInt()
            backgroundEndGuidelineHeight = (it * BACKGROUND_END_GUIDELINE_HEIGHT).toInt()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWelcomeAndSetUpCurrencyBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.screen) {
                        SetUpScreen.WELCOME -> showWelcomeScreen()
                        else -> showCurrencyScreen()
                    }
                }
            }
        }

        with(binding) {
            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener { viewModel.nextScreen() }
        }

        return binding.root
    }

    private fun showWelcomeScreen() {
        with(binding) {
            if (prevScreen == SetUpScreen.SET_UP_CURRENCY) {
                backgroundGuidelineAnimator.reverse()
            } else {
                backgroundStartGuideline.setGuidelineBegin(backgroundStartGuidelineHeight)
                backgroundEndGuideline.setGuidelineBegin(backgroundEndGuidelineHeight)
            }
            appIcon.visibility = View.VISIBLE
            backButton.visibility = View.GONE
            nextButton.text = welcomeNextButtonText
        }

        childFragmentManager.navigateTo(R.id.welcome_currency_fragment_container, WelcomeFragment())

        prevScreen = SetUpScreen.WELCOME
    }

    private fun showCurrencyScreen() {
        with(binding) {
            if (prevScreen == SetUpScreen.WELCOME) {
                backgroundGuidelineAnimator.start()
            } else {
                backgroundStartGuideline.setGuidelineBegin(0)
                backgroundEndGuideline.setGuidelineBegin(0)
            }
            appIcon.visibility = View.GONE
            backButton.visibility = View.VISIBLE
            nextButton.text = currencyNextButtonText
        }

        childFragmentManager.navigateTo(R.id.welcome_currency_fragment_container, SetUpCurrencyFragment())

        prevScreen = SetUpScreen.SET_UP_CURRENCY
    }

    companion object {
        private const val ANIMATION_DURATION_MILLIS: Long = 500

        private const val BACKGROUND_START_GUIDELINE_HEIGHT = 0.4
        private const val BACKGROUND_END_GUIDELINE_HEIGHT = 0.48
    }
}