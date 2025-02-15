package dev.ohjiho.budgetify.setup

import android.os.Bundle
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
import dev.ohjiho.budgetify.category.editor.CategoryEditorFragment
import dev.ohjiho.budgetify.setup.databinding.ActivitySetUpBinding
import dev.ohjiho.budgetify.utils.ui.navigateTo
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetUpActivity : AppCompatActivity() {

    private val viewModel: SetUpViewModel by viewModels()
    private lateinit var binding: ActivitySetUpBinding

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
                        SetUpScreen.WELCOME, SetUpScreen.SET_UP_CURRENCY -> showScreen(WelcomeAndSetUpCurrencyFragment(), true)
                        SetUpScreen.SET_UP_ACCOUNTS -> showScreen(SetUpAccountsFragment(), true)
                        SetUpScreen.ACCOUNT_EDITOR -> showScreen(AccountEditorFragment.getSetUpInstance(viewModel.editingAccountId))
                        SetUpScreen.SET_UP_INCOME -> showScreen(SetUpIncomeFragment())
                        SetUpScreen.SET_UP_CATEGORIES -> showScreen(SetUpCategoriesFragment())
                        SetUpScreen.CATEGORY_EDITOR -> showScreen(CategoryEditorFragment.getSetUpInstance(viewModel.editingCategoryId))
                        SetUpScreen.SET_UP_BUDGET -> showScreen(SetUpBudgetFragment())
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

    private fun showScreen(fragment: Fragment, isInstant: Boolean = false) {
        supportFragmentManager.navigateTo(R.id.fragment_container, fragment, isInstant)
    }
}