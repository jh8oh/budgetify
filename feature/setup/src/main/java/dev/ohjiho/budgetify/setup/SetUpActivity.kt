package dev.ohjiho.budgetify.setup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.account.editor.AccountEditorFragment
import dev.ohjiho.budgetify.setup.databinding.ActivitySetUpBinding
import dev.ohjiho.budgetify.utils.ui.navigateTo
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetUpActivity : AppCompatActivity(), AccountEditorFragment.Listener {

    private val viewModel: SetUpViewModel by viewModels()
    private lateinit var binding: ActivitySetUpBinding

    // Fragments
    private val welcomeAndSetUpCurrencyFragment by lazy { WelcomeAndSetUpCurrencyFragment() }
    private val setUpAccountsFragment by lazy { SetUpAccountsFragment() }
    private val setUpIncomeFragment by lazy { SetUpIncomeFragment() }
    private val setUpBudgetsFragment by lazy { SetUpBudgetsFragment() }

    // Resources
    private val setUpAccountsTitle by lazy { resources.getString(R.string.fragment_set_up_accounts_title) }
    private val setUpIncomeTitle by lazy { resources.getString(R.string.fragment_set_up_income_title) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.screen) {
                        SetUpScreen.WELCOME, SetUpScreen.SET_UP_CURRENCY -> showWelcomeAndSetUpCurrencyScreen()
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

        onBackPressedDispatcher.addCallback(this) {
            if (viewModel.onBackPressed()) finish()
        }
    }

    private fun showWelcomeAndSetUpCurrencyScreen() {
        binding.appBar.visibility = View.GONE
        supportFragmentManager.navigateTo(R.id.fragment_container, welcomeAndSetUpCurrencyFragment, true)
    }

    private fun showAccountsScreen() {
        binding.appBar.visibility = View.VISIBLE
        binding.title.text = setUpAccountsTitle

        supportFragmentManager.navigateTo(R.id.fragment_container, setUpAccountsFragment, true)
    }

    @SuppressLint("CommitTransaction")
    private fun showAccountEditorScreen(accountId: Int?) {
        binding.appBar.visibility = View.GONE

        supportFragmentManager.navigateTo(R.id.fragment_container, AccountEditorFragment.newInstance(this, accountId, true), true)
    }

    override fun onEditorBack() {
        viewModel.onBackPressed()
    }


    private fun showIncomeScreen() {
        binding.title.text = setUpIncomeTitle

        supportFragmentManager.navigateTo(R.id.fragment_container, setUpIncomeFragment)
    }

    private fun showBudgetsScreen() {
        supportFragmentManager.navigateTo(R.id.fragment_container, setUpBudgetsFragment)
    }
}