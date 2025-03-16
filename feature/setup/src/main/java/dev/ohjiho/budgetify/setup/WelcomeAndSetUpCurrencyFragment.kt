package dev.ohjiho.budgetify.setup

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.setup.databinding.FragmentWelcomeAndSetUpCurrencyBinding
import dev.ohjiho.budgetify.utils.ui.ScreenMetricsCompat
import dev.ohjiho.currencypicker.CurrencyPicker
import kotlinx.coroutines.launch
import java.util.Currency
import kotlin.properties.Delegates

internal class WelcomeAndSetUpCurrencyFragment : Fragment(), CurrencyPicker.Listener {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentWelcomeAndSetUpCurrencyBinding

    private var prevScreen: SetUpScreen? = null

    // Resources
    private var backgroundStartGuidelineHeight by Delegates.notNull<Int>()
    private var backgroundEndGuidelineHeight by Delegates.notNull<Int>()

    private val setUpCurrencyTitle by lazy { resources.getString(R.string.fragment_set_up_currency_title) }

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
    private var backgroundGuidelineAnimatorWelcomeLandListener: AnimatorListener? = null
    private var backgroundGuidelineAnimatorCurrencyLandListener: AnimatorListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenSize = ScreenMetricsCompat.getScreenSize(requireContext())
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            backgroundStartGuidelineHeight = (screenSize.width * BACKGROUND_START_GUIDELINE_HEIGHT).toInt()
            backgroundEndGuidelineHeight = (screenSize.width * BACKGROUND_END_GUIDELINE_HEIGHT).toInt()

            backgroundGuidelineAnimatorWelcomeLandListener = object : AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {}
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationStart(animator: Animator) = (activity as SetUpActivity).setAppBarVisibility(View.GONE)
            }
            backgroundGuidelineAnimatorCurrencyLandListener = object : AnimatorListener {
                override fun onAnimationRepeat(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) = (activity as SetUpActivity).setAppBarVisibility(View.VISIBLE)
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationStart(animator: Animator) {}
            }
        } else {
            backgroundStartGuidelineHeight = (screenSize.height * BACKGROUND_START_GUIDELINE_HEIGHT).toInt()
            backgroundEndGuidelineHeight = (screenSize.height * BACKGROUND_END_GUIDELINE_HEIGHT).toInt()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            currencyPicker.apply {
                setSelectedCurrency(viewModel.defaultCurrency)
                setListener(this@WelcomeAndSetUpCurrencyFragment)
            }

            startEndSplitGuideline?.setGuidelineBegin(backgroundEndGuidelineHeight)

            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener { viewModel.nextScreen() }
        }

        return binding.root
    }

    private fun showWelcomeScreen() {
        with(binding) {
            // Title
            (requireActivity() as AppCompatActivity).title = null

            // Start/End Guideline
            if (prevScreen == SetUpScreen.SET_UP_CURRENCY) {
                backgroundGuidelineAnimator.apply {
                    addListener(backgroundGuidelineAnimatorWelcomeLandListener)
                    removeListener(backgroundGuidelineAnimatorCurrencyLandListener)
                }.reverse()
            } else {
                backgroundStartGuideline.setGuidelineBegin(backgroundStartGuidelineHeight)
                backgroundEndGuideline.setGuidelineBegin(backgroundEndGuidelineHeight)
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    (activity as SetUpActivity).setAppBarVisibility(View.GONE)
                }
            }

            // Content
            welcomeLayout.visibility = View.VISIBLE
            currencyDescription.visibility = View.GONE
            currencyPicker.visibility = View.GONE

            // App icon & navigation buttons
            appIcon.visibility = View.VISIBLE
            backButton.visibility = View.GONE
            nextButton.text = welcomeNextButtonText
        }

        prevScreen = SetUpScreen.WELCOME
    }

    private fun showCurrencyScreen() {
        with(binding) {
            // Title
            (requireActivity() as AppCompatActivity).title = setUpCurrencyTitle

            // Start/End Guideline
            if (prevScreen == SetUpScreen.WELCOME) {
                backgroundGuidelineAnimator.apply {
                    addListener(backgroundGuidelineAnimatorCurrencyLandListener)
                    removeListener(backgroundGuidelineAnimatorWelcomeLandListener)
                }.start()
            } else {
                backgroundStartGuideline.setGuidelineBegin(0)
                backgroundEndGuideline.setGuidelineBegin(0)
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    (activity as SetUpActivity).setAppBarVisibility(View.VISIBLE)
                }
            }

            // Content
            welcomeLayout.visibility = View.GONE
            currencyDescription.visibility = View.VISIBLE
            currencyPicker.visibility = View.VISIBLE

            // App icon & navigation buttons
            appIcon.visibility = View.GONE
            backButton.visibility = View.VISIBLE
            nextButton.text = currencyNextButtonText
        }

        prevScreen = SetUpScreen.SET_UP_CURRENCY
    }

    override fun onCurrencySelected(currency: Currency) {
        viewModel.defaultCurrency = currency
    }

    companion object {
        private const val ANIMATION_DURATION_MILLIS: Long = 500

        private const val BACKGROUND_START_GUIDELINE_HEIGHT = 0.4
        private const val BACKGROUND_END_GUIDELINE_HEIGHT = 0.48
    }
}