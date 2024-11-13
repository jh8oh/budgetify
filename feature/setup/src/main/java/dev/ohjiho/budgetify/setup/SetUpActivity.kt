package dev.ohjiho.budgetify.setup

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
import dev.ohjiho.budgetify.category.editor.CategoryEditorFragment
import dev.ohjiho.budgetify.setup.databinding.ActivitySetUpBinding
import dev.ohjiho.budgetify.utils.ui.navigateTo
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetUpActivity : AppCompatActivity() {

    private val viewModel: SetUpViewModel by viewModels()
    private lateinit var binding: ActivitySetUpBinding

    // Resources
    private val setUpAccountsTitle by lazy { resources.getString(R.string.fragment_set_up_accounts_title) }
    private val setUpIncomeTitle by lazy { resources.getString(R.string.fragment_set_up_income_title) }
    private val setUpCategoriesTitle by lazy { resources.getString(R.string.fragment_setup_categories_title) }
    private val setUpBudgetTitle by lazy { resources.getString(R.string.fragment_set_up_budget_title) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar)
        binding.appBar.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.screen) {
                        SetUpScreen.WELCOME, SetUpScreen.SET_UP_CURRENCY -> showWelcomeAndSetUpCurrencyScreen()
                        SetUpScreen.SET_UP_ACCOUNTS -> showAccountsScreen()
                        SetUpScreen.ACCOUNT_EDITOR -> showAccountEditorScreen(viewModel.editingAccountId)
                        SetUpScreen.SET_UP_INCOME -> showIncomeScreen()
                        SetUpScreen.SET_UP_CATEGORIES -> showCategoriesScreen()
                        SetUpScreen.CATEGORY_EDITOR -> showCategoryEditorScreen(viewModel.editingCategoryId)
                        SetUpScreen.SET_UP_BUDGET -> showBudgetScreen()
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
        supportFragmentManager.navigateTo(R.id.fragment_container, WelcomeAndSetUpCurrencyFragment(), true)
    }

    private fun showAccountsScreen() {
        binding.appBar.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        title = setUpAccountsTitle

        supportFragmentManager.navigateTo(R.id.fragment_container, SetUpAccountsFragment(), true)
    }

    private fun showAccountEditorScreen(accountId: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.navigateTo(R.id.fragment_container, AccountEditorFragment.newSetUpInstance(accountId))
    }

    private fun showIncomeScreen() {
        title = setUpIncomeTitle

        // Requires new view so that the adapter is updated
        supportFragmentManager.navigateTo(R.id.fragment_container, SetUpIncomeFragment())
    }

    private fun showCategoriesScreen() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        title = setUpCategoriesTitle

        supportFragmentManager.navigateTo(R.id.fragment_container, SetUpCategoriesFragment())
    }

    private fun showCategoryEditorScreen(categoryId: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.navigateTo(R.id.fragment_container, CategoryEditorFragment.newSetUpInstance(categoryId))
    }

    private fun showBudgetScreen() {
        title = setUpBudgetTitle

        supportFragmentManager.navigateTo(R.id.fragment_container, SetUpBudgetFragment())
    }
}