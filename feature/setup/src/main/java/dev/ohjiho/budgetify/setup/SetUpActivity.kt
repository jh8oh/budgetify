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
import dev.ohjiho.account.editor.AccountEditorFragment
import dev.ohjiho.budgetify.setup.databinding.ActivitySetUpBinding
import dev.ohjiho.budgetify.utils.ui.ScreenMetricsCompat
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SetUpActivity : AppCompatActivity(), AccountEditorFragment.Listener {

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
    private val accountEditorAddTitle by lazy { resources.getString(R.string.fragment_account_editor_add_title) }
    private val accountEditorUpdateTitle by lazy { resources.getString(R.string.fragment_account_editor_update_title) }

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
                        SetUpScreen.ACCOUNT_EDITOR_ADD, SetUpScreen.ACCOUNT_EDITOR_UPDATE -> showAccountEditorScreen(viewModel.editingAccountId)
                        SetUpScreen.SET_UP_INCOME -> showIncomeScreen()
                        SetUpScreen.SET_UP_BUDGET -> showBudgetsScreen()
                    }
                }
            }
        }

        with(binding) {
            appBarBack.setOnClickListener {
                viewModel.onBackPressed()
            }

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
        binding.nextButton.apply {
            visibility = View.VISIBLE
            text = nextButtonText
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetUpCurrencyFragment()).commit()

        prevScreen = SetUpScreen.SET_UP_CURRENCY
    }

    @SuppressLint("CommitTransaction")
    private fun showAccountsScreen() {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        binding.appBarBack.visibility = View.GONE
        binding.title.text = setUpAccountsTitle
        binding.backButton.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetUpAccountsFragment()).commit()

        prevScreen = SetUpScreen.SET_UP_ACCOUNTS
    }

    @SuppressLint("CommitTransaction")
    private fun showAccountEditorScreen(accountId: Int?) {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        binding.appBarBack.visibility = View.VISIBLE
        binding.title.text = accountId?.let { accountEditorUpdateTitle } ?: accountEditorAddTitle
        binding.backButton.visibility = View.GONE
        binding.nextButton.visibility = View.GONE

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AccountEditorFragment.newInstance(accountId, this)).commit()

        prevScreen = accountId?.let { SetUpScreen.ACCOUNT_EDITOR_UPDATE } ?: SetUpScreen.ACCOUNT_EDITOR_ADD
    }

    override fun onSave() {
        viewModel.onBackPressed()
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