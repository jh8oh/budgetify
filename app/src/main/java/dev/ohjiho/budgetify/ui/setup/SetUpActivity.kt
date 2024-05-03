package dev.ohjiho.budgetify.ui.setup

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.databinding.ContentSetUpBinding
import dev.ohjiho.budgetify.util.ScreenMetricsCompat
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SetUpActivity : AppCompatActivity() {

    private val viewModel: SetUpViewModel by viewModels()
    private lateinit var binding: ContentSetUpBinding

    // Animations
    private val backgroundStartGuidelineAnimator by lazy {
        ValueAnimator.ofInt(fiftyHeight, actionBarSize).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundStartGuideline.setGuidelineBegin(it.animatedValue as Int)
            }
        }
    }
    private val backgroundEndGuidelineAnimator by lazy {
        ValueAnimator.ofInt(sixtyHeight, actionBarSize).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundEndGuideline.setGuidelineBegin(it.animatedValue as Int)
            }
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
    private var fiftyHeight by Delegates.notNull<Int>()
    private var sixtyHeight by Delegates.notNull<Int>()

    private val nextButtonWelcomeText by lazy { resources.getString(R.string.fragment_set_up_welcome_button_label) }
    private val nextButtonText by lazy { resources.getString(R.string.content_set_up_next_button_label) }
    private val accountsTitle by lazy { resources.getString(R.string.fragment_set_up_accounts_title) }
    private val incomeTitle by lazy { resources.getString(R.string.fragment_set_up_income_title) }
    private val budgetTitle by lazy { resources.getString(R.string.fragment_set_up_budget_title) }

    companion object {
        private const val ANIMATION_DURATION_MILLIS: Long = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // General views
        ScreenMetricsCompat.getScreenSize(applicationContext, false).height.let {
            fiftyHeight = (it * 0.5).toInt()
            sixtyHeight = (it * 0.6).toInt()
        }
        binding.backgroundStartGuideline.setGuidelineBegin(fiftyHeight)
        binding.backgroundEndGuideline.setGuidelineBegin(sixtyHeight)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState.screen) {
                        SetUpUiScreen.WELCOME -> showWelcomeScreen()
                        SetUpUiScreen.SET_UP_ACCOUNTS -> showAccountsScreen()
                        SetUpUiScreen.SET_UP_INCOME -> showIncomeScreen()
                        SetUpUiScreen.SET_UP_BUDGET -> showBudgetScreen()
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            binding.setUpNavHostFragment.findNavController().navigateUp()
            if (viewModel.onBackPressed()) finish()
        }
    }

    private fun showWelcomeScreen() {
        if (backgroundStartGuidelineAnimator.animatedFraction != 0f) {
            backgroundStartGuidelineAnimator.reverse()
            backgroundEndGuidelineAnimator.reverse()
        }

        binding.appIcon.visibility = View.VISIBLE
        binding.title.visibility = View.GONE
        binding.backButton.visibility = View.GONE
        binding.nextButton.apply {
            text = nextButtonWelcomeText
            setOnClickListener {
                viewModel.nextScreen()
                binding.setUpNavHostFragment.findNavController()
                    .navigate(R.id.action_nav_fragment_set_up_welcome_to_nav_fragment_set_up_accounts)
            }
        }
    }

    private fun showAccountsScreen() {
        if (backgroundStartGuidelineAnimator.animatedFraction != 1f) {
            backgroundStartGuidelineAnimator.start()
            backgroundEndGuidelineAnimator.start()
        }

        binding.appIcon.visibility = View.GONE
        binding.title.apply {
            visibility = View.VISIBLE
            text = accountsTitle
        }
        binding.backButton.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                viewModel.onBackPressed()
                binding.setUpNavHostFragment.findNavController()
                    .navigate(R.id.action_nav_fragment_set_up_accounts_to_nav_fragment_set_up_welcome)
            }
        }
        binding.nextButton.apply {
            text = nextButtonText
            setOnClickListener {
                //TODO Check accounts all OK
                viewModel.nextScreen()
                binding.setUpNavHostFragment.findNavController()
                    .navigate(R.id.action_nav_fragment_set_up_accounts_to_nav_fragment_set_up_income)
            }
        }
    }

    private fun showIncomeScreen() {
        binding.title.text = incomeTitle

        binding.backButton.setOnClickListener {
            viewModel.onBackPressed()
            binding.setUpNavHostFragment.findNavController()
                .navigate(R.id.action_nav_fragment_set_up_income_to_nav_fragment_set_up_accounts)
        }
        binding.nextButton.setOnClickListener {
            // TODO Check income all OK
            viewModel.nextScreen()
            binding.setUpNavHostFragment.findNavController()
                .navigate(R.id.action_nav_fragment_set_up_income_to_nav_fragment_set_up_budget)
        }

    }

    private fun showBudgetScreen() {
        binding.backButton.setOnClickListener {
            viewModel.onBackPressed()
            binding.setUpNavHostFragment.findNavController()
                .navigate(R.id.action_nav_fragment_set_up_budget_to_nav_fragment_set_up_income)
        }
        binding.nextButton.setOnClickListener {
            // TODO
        }
    }
}