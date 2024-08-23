package dev.ohjiho.budgetify.setup

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.account.editor.AccountEditorFragment
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
    private val setUpIncomeTitle by lazy { resources.getString(R.string.fragment_set_up_income_title) }

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
                    it.toastMessage.getContentIfNotHandled()?.let { message ->
                        Toast.makeText(this@SetUpActivity, message, Toast.LENGTH_LONG).show()
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

        navigateFragmentTo(WelcomeFragment())

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

        navigateFragmentTo(SetUpCurrencyFragment(), true)
        binding.navButtonContainer.visibility = View.VISIBLE

        prevScreen = SetUpScreen.SET_UP_CURRENCY
    }

    @SuppressLint("CommitTransaction")
    private fun showAccountsScreen() {
        binding.title.apply {
            visibility = View.VISIBLE
            text = setUpAccountsTitle
        }
        binding.navButtonContainer.visibility = View.GONE

        navigateFragmentTo(SetUpAccountsFragment(), true)
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        prevScreen = SetUpScreen.SET_UP_ACCOUNTS
    }

    @SuppressLint("CommitTransaction")
    private fun showAccountEditorScreen(accountId: Int?) {
        binding.title.visibility = View.GONE

        navigateFragmentTo(AccountEditorFragment.newInstance(this, accountId, true), true)
        binding.backgroundStartGuideline.setGuidelineBegin(0)
        binding.backgroundEndGuideline.setGuidelineBegin(0)

        prevScreen = accountId?.let { SetUpScreen.ACCOUNT_EDITOR_UPDATE } ?: SetUpScreen.ACCOUNT_EDITOR_ADD
    }

    override fun onEditorBack() {
        viewModel.onBackPressed()
    }


    private fun showIncomeScreen() {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        binding.title.text = setUpIncomeTitle

        navigateFragmentTo(SetUpIncomeFragment())

        prevScreen = SetUpScreen.SET_UP_INCOME
    }

    private fun showBudgetsScreen() {
        binding.backgroundStartGuideline.setGuidelineBegin(actionBarSize)
        binding.backgroundEndGuideline.setGuidelineBegin(actionBarSize)

        navigateFragmentTo(SetUpBudgetsFragment())

        prevScreen = SetUpScreen.SET_UP_BUDGET
    }

    @SuppressLint("CommitTransaction")
    private fun navigateFragmentTo(fragment: Fragment, isInstant: Boolean = false) {
        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (existingFragment != null && existingFragment.javaClass == fragment.javaClass) return

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        if (isInstant) {
            // executePendingTransactions() required so that commit() is not done asynchronously and is instead done instantly to
            // matched with the start of another animation
            supportFragmentManager.executePendingTransactions()
        }
    }

    companion object {
        private const val ANIMATION_DURATION_MILLIS: Long = 500
    }
}