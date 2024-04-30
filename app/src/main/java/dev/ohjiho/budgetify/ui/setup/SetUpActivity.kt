package dev.ohjiho.budgetify.ui.setup

import android.animation.ValueAnimator
import android.content.Intent
import android.icu.util.Currency
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.databinding.ContentSetUpBinding
import dev.ohjiho.budgetify.ui.currency.CurrencyPickerDialog
import dev.ohjiho.budgetify.ui.main.BudgetifyActivity
import kotlinx.coroutines.launch

class SetUpActivity : AppCompatActivity(), CurrencyPickerDialog.Listener {

    private val viewModel: SetUpViewModel by viewModels()
    private lateinit var binding: ContentSetUpBinding

    private var currentScreen = SetUpUiScreen.WELCOME

    // Views
    private val currencyPickerDialog = CurrencyPickerDialog()

    // Animations
    private val backgroundStartGuidelineAnimator by lazy {
        ValueAnimator.ofFloat(0.5f, 0.08f).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundStartGuideline.setGuidelinePercent(it.animatedValue as Float)
            }
        }
    }
    private val backgroundEndGuidelineAnimator by lazy {
        ValueAnimator.ofFloat(0.6f, 0.08f).apply {
            duration = ANIMATION_DURATION_MILLIS
            addUpdateListener {
                binding.backgroundEndGuideline.setGuidelinePercent(it.animatedValue as Float)
            }
        }
    }

    // Resources
    private val nextButtonWelcomeText by lazy { resources.getString(R.string.content_set_up_welcome_button_label) }
    private val nextButtonText by lazy { resources.getString(R.string.content_set_up_next_button_label) }
    private val incomeTitle by lazy { resources.getString(R.string.content_set_up_income_title) }

    companion object {
        private const val ANIMATION_DURATION_MILLIS: Long = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // General views
        binding.backButton.setOnClickListener {
            viewModel.onBackPressed()
        }

        binding.nextButton.setOnClickListener {
            if (viewModel.nextScreen()) {
                startActivity(Intent(this@SetUpActivity, BudgetifyActivity::class.java))
                finish()
            }
        }

        // Income
        binding.incomeInput.addTextChangedListener {
            viewModel.onIncomeTextChange(Integer.parseInt(it.toString()))
        }

        binding.currencyInput.setOnClickListener {
            currencyPickerDialog.show(supportFragmentManager, CurrencyPickerDialog.TAG)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState.screen) {
                        SetUpUiScreen.WELCOME -> showWelcomeScreen()
                        SetUpUiScreen.SET_UP_INCOME -> showIncomeScreen()
                        SetUpUiScreen.SET_UP_BUDGET -> showBudgetScreen()
                    }

                    binding.currencyInput.setText(uiState.mainCurrency.currencyCode)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            if (viewModel.onBackPressed()) finish()
        }
    }

    private fun showWelcomeScreen() {
        if (currentScreen != SetUpUiScreen.WELCOME) {
            currentScreen = SetUpUiScreen.WELCOME

            backgroundStartGuidelineAnimator.reverse()
            backgroundEndGuidelineAnimator.reverse()

            binding.appIcon.visibility = View.VISIBLE
            binding.title.visibility = View.GONE
            binding.welcomeContent.visibility = View.VISIBLE
            binding.incomeContent.visibility = View.GONE
            binding.backButton.visibility = View.GONE
            binding.nextButton.text = nextButtonWelcomeText
        }
    }

    private fun showIncomeScreen() {
        if (currentScreen != SetUpUiScreen.SET_UP_INCOME) {
            currentScreen = SetUpUiScreen.SET_UP_INCOME

            backgroundStartGuidelineAnimator.start()
            backgroundEndGuidelineAnimator.start()

            binding.appIcon.visibility = View.GONE
            binding.title.apply {
                visibility = View.VISIBLE
                text = incomeTitle
            }
            binding.welcomeContent.visibility = View.GONE
            binding.incomeContent.visibility = View.VISIBLE
            binding.backButton.visibility = View.VISIBLE
            binding.nextButton.text = nextButtonText
        }
    }

    private fun showBudgetScreen() {
        if (currentScreen != SetUpUiScreen.SET_UP_BUDGET) {
            currentScreen = SetUpUiScreen.SET_UP_BUDGET


        }
    }

    override fun onCurrencySelected(currency: Currency) {
        viewModel.onCurrencySelected(currency)
        currencyPickerDialog.dismiss()
    }
}