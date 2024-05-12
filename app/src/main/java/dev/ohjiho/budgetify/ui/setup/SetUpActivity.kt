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
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.data.model.Account
import dev.ohjiho.budgetify.databinding.ContentSetUpBinding
import dev.ohjiho.budgetify.util.ScreenMetricsCompat
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
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

    private val nextButtonWelcomeText by lazy { resources.getString(R.string.fragment_set_up_welcome_button) }
    private val nextButtonText by lazy { resources.getString(R.string.content_set_up_next_button) }
    private val accountsTitle by lazy { resources.getString(R.string.fragment_set_up_accounts_title) }
    private val accountAddTitle by lazy { resources.getString(R.string.fragment_account_editor_add_title) }
    private val accountEditTitle by lazy { resources.getString(R.string.fragment_account_editor_edit_title) }
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState.screen) {
                        SetUpUiScreen.WELCOME -> showWelcomeScreen()
                        SetUpUiScreen.SET_UP_ACCOUNTS -> showAccountsScreen()
                        SetUpUiScreen.ACCOUNT_EDITOR -> showAccountEditorScreen(uiState.editingAccount)
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

        with(binding) {
            appIcon.visibility = View.VISIBLE
            appBarBack.visibility = View.GONE
            title.visibility = View.GONE

            backButton.visibility = View.GONE
            nextButton.apply {
                text = nextButtonWelcomeText
                setOnClickListener {
                    viewModel.nextScreen()
                    binding.setUpNavHostFragment.findNavController()
                        .navigate(R.id.action_nav_fragment_set_up_welcome_to_nav_fragment_set_up_accounts)
                }
            }
        }
    }

    private fun showAccountsScreen() {
        if (backgroundStartGuidelineAnimator.animatedFraction != 1f) {
            backgroundStartGuidelineAnimator.start()
            backgroundEndGuidelineAnimator.start()
        }

        with(binding) {
            appIcon.visibility = View.GONE
            appBarBack.visibility = View.GONE
            title.apply {
                visibility = View.VISIBLE
                text = accountsTitle
            }

            backButton.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    viewModel.onBackPressed()
                    binding.setUpNavHostFragment.findNavController()
                        .navigate(R.id.action_nav_fragment_set_up_accounts_to_nav_fragment_set_up_welcome)
                }
            }
            nextButton.apply {
                visibility = View.VISIBLE
                text = nextButtonText
                setOnClickListener {
                    if (viewModel.nextScreen()) {
                        binding.setUpNavHostFragment.findNavController()
                            .navigate(R.id.action_nav_fragment_set_up_accounts_to_nav_fragment_set_up_income)
                    }
                }
            }
        }
    }

    private fun showAccountEditorScreen(account: Account?) {
        if (backgroundStartGuidelineAnimator.animatedFraction != 1f) {
            backgroundStartGuidelineAnimator.start()
            backgroundEndGuidelineAnimator.start()
        }

        with(binding) {
            appBarBack.apply {
                visibility = View.VISIBLE
                if (!hasOnClickListeners()) {
                    setOnClickListener {
                        viewModel.onBackPressed()
                        binding.setUpNavHostFragment.findNavController()
                            .navigate(R.id.action_nav_fragment_set_up_account_editor_to_nav_fragment_set_up_accounts)
                    }
                }
            }
            title.apply {
                visibility = View.VISIBLE
                text = account?.let { accountEditTitle } ?: accountAddTitle
            }

            backButton.visibility = View.GONE
            nextButton.visibility = View.GONE
        }
    }

    private fun showIncomeScreen() {
        if (backgroundStartGuidelineAnimator.animatedFraction != 1f) {
            backgroundStartGuidelineAnimator.start()
            backgroundEndGuidelineAnimator.start()
        }

        with(binding) {
            title.apply {
                visibility = View.VISIBLE
                text = incomeTitle
            }

            backButton.setOnClickListener {
                viewModel.onBackPressed()
                binding.setUpNavHostFragment.findNavController()
                    .navigate(R.id.action_nav_fragment_set_up_income_to_nav_fragment_set_up_accounts)
            }
            nextButton.setOnClickListener {
                viewModel.nextScreen()
                binding.setUpNavHostFragment.findNavController()
                    .navigate(R.id.action_nav_fragment_set_up_income_to_nav_fragment_set_up_budget)
            }
        }
    }

    private fun showBudgetScreen() {
        if (backgroundStartGuidelineAnimator.animatedFraction != 1f) {
            backgroundStartGuidelineAnimator.start()
            backgroundEndGuidelineAnimator.start()
        }

        with(binding) {
            backgroundStartGuideline.setGuidelineBegin(actionBarSize)
            backgroundEndGuideline.setGuidelineBegin(actionBarSize)

            backButton.setOnClickListener {
                viewModel.onBackPressed()
                binding.setUpNavHostFragment.findNavController()
                    .navigate(R.id.action_nav_fragment_set_up_budget_to_nav_fragment_set_up_income)
            }
            nextButton.setOnClickListener {
                // TODO
            }
        }
    }
}