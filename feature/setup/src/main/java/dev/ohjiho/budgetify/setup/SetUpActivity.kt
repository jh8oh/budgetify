package dev.ohjiho.budgetify.setup

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.setup.accounts.SetUpAccountsFragment
import dev.ohjiho.budgetify.setup.currency.SetUpCurrencyFragment
import dev.ohjiho.budgetify.setup.databinding.ActivitySetUpBinding
import dev.ohjiho.budgetify.utils.ui.ScreenMetricsCompat
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SetUpActivity : AppCompatActivity() {

    private val viewModel: SetUpViewModel by viewModels()
    private lateinit var binding: ActivitySetUpBinding

    private var prevScreen: SetUpScreen? = null

    // Animations
    private val backgroundGuidelineAnimator by lazy {
        val startGuidelineAnimator = ValueAnimator.ofInt(fortyFiveHeight, actionBarSize).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundStartGuideline.setGuidelineBegin(it.animatedValue as Int)
            }
        }
        val endGuidelineAnimator = ValueAnimator.ofInt(fiftyFiveHeight, actionBarSize).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundEndGuideline.setGuidelineBegin(it.animatedValue as Int)
            }
        }
        AnimatorSet().apply {
            playTogether(startGuidelineAnimator, endGuidelineAnimator)
        }
    }

    // Resources
    private val actionBarSize by lazy {
        applicationContext.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).let {
            val size = it.getDimensionPixelSize(0, 0)
            it.recycle()
            size
        }
    }
    private var fortyFiveHeight by Delegates.notNull<Int>()
    private var fiftyFiveHeight by Delegates.notNull<Int>()

    private val welcomeNextButtonText by lazy { resources.getString(R.string.fragment_welcome_next_button) }
    private val nextButtonText by lazy { resources.getString(R.string.fragment_set_up_next_button) }
    private val setUpCurrencyTitle by lazy { resources.getString(R.string.fragment_set_up_currency_title) }
    private val setUpAccountsTitle by lazy { resources.getString(R.string.fragment_set_up_accounts_title) }

    companion object {
        private const val ANIMATION_DURATION_MILLIS: Long = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ScreenMetricsCompat.getScreenSize(applicationContext).height.let {
            fortyFiveHeight = (it * 0.45).toInt()
            fiftyFiveHeight = (it * 0.55).toInt()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.screen) {
                        SetUpScreen.WELCOME -> showWelcomeScreen()
                        SetUpScreen.SET_UP_CURRENCY -> showCurrencyScreen()
                        SetUpScreen.SET_UP_ACCOUNTS -> showAccountsScreen()
                        SetUpScreen.SET_UP_INCOME -> showIncomeScreen()
                        SetUpScreen.SET_UP_BUDGET -> showBudgetsScreen()
                    }
                }
            }
        }

        with(binding) {
            backButton.setOnClickListener {
                viewModel.onBackPressed()
            }

            nextButton.setOnClickListener {
                viewModel.nextScreen()
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            if (viewModel.onBackPressed()) finish()
        }
    }

    @SuppressLint("CommitTransaction")
    private fun showWelcomeScreen() {
        if (prevScreen == SetUpScreen.SET_UP_CURRENCY) {
            backgroundGuidelineAnimator.reverse()
        } else {
            binding.backgroundStartGuideline.setGuidelineBegin(fortyFiveHeight)
            binding.backgroundEndGuideline.setGuidelineBegin(fiftyFiveHeight)
        }

        binding.appIcon.visibility = View.VISIBLE
        binding.title.visibility = View.GONE
        binding.backButton.visibility = View.GONE
        binding.nextButton.text = welcomeNextButtonText

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WelcomeFragment()).commit()

        prevScreen = SetUpScreen.WELCOME
    }

    @SuppressLint("CommitTransaction")
    private fun showCurrencyScreen() {
        if (prevScreen == SetUpScreen.WELCOME) {
            backgroundGuidelineAnimator.start()
        } else {
            binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
            binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)
        }

        binding.appIcon.visibility = View.GONE
        binding.title.apply {
            visibility = View.VISIBLE
            text = setUpCurrencyTitle
        }
        binding.backButton.visibility = View.VISIBLE
        binding.nextButton.text = nextButtonText

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetUpCurrencyFragment()).commit()

        prevScreen = SetUpScreen.SET_UP_CURRENCY
    }

    @SuppressLint("CommitTransaction")
    private fun showAccountsScreen() {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        binding.title.text = setUpAccountsTitle

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetUpAccountsFragment()).commit()

        prevScreen = SetUpScreen.SET_UP_ACCOUNTS
    }

    @SuppressLint("CommitTransaction")
    private fun showIncomeScreen() {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetUpIncomeFragment()).commit()

        prevScreen = SetUpScreen.SET_UP_INCOME
    }

    @SuppressLint("CommitTransaction")
    private fun showBudgetsScreen() {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetUpBudgetsFragment()).commit()

        prevScreen = SetUpScreen.SET_UP_BUDGET
    }
}