package dev.ohjiho.budgetify.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.flow.Event
import dev.ohjiho.budgetify.utils.flow.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

internal enum class SetUpScreen {
    WELCOME, SET_UP_CURRENCY, SET_UP_ACCOUNTS, ACCOUNT_EDITOR, SET_UP_INCOME, SET_UP_CATEGORIES, CATEGORY_EDITOR, SET_UP_BUDGET
}

internal data class SetUpUiState(
    val screen: SetUpScreen = SetUpScreen.WELCOME,
    val toastMessage: Event<String?> = Event(null),
)

internal data class SetUpIncomeState(
    val isIncome: Boolean = true,
    val amount: BigDecimal = BigDecimal.ZERO,
    val isMonthly: Boolean = true,
    val account: Account? = null,
)

@HiltViewModel
internal class SetUpViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    accountRepository: AccountRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val screen = MutableStateFlow(SetUpScreen.WELCOME)
    private val toastMessage = MutableStateFlow<Event<String?>>(Event(null))
    var editingAccountId: Int? = null
    var editingCategoryId: Int? = null

    val uiState = combine(screen, toastMessage) { screen, toastMessage ->
        SetUpUiState(screen = screen, toastMessage = toastMessage)
    }.stateIn(viewModelScope, WhileUiSubscribed, SetUpUiState())

    val defaultCurrency = currencyRepository.getDefaultCurrencyAsFlow()
        .stateIn(viewModelScope, WhileUiSubscribed, Currency.getInstance(getLocale()))
    val accounts = accountRepository.getAllAccounts().stateIn(viewModelScope, WhileUiSubscribed, emptyList())
    val setUpIncomeState = MutableStateFlow(SetUpIncomeState())
    val expenseCategories = categoryRepository.getAllExpenseCategories().stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    // Screen
    fun onBackPressed(): Boolean {
        val currentScreen = uiState.value.screen

        return when (currentScreen) {
            SetUpScreen.WELCOME -> true

            SetUpScreen.SET_UP_CURRENCY -> {
                screen.update { SetUpScreen.WELCOME }
                false
            }

            SetUpScreen.SET_UP_ACCOUNTS -> {
                screen.update { SetUpScreen.SET_UP_CURRENCY }
                false
            }

            SetUpScreen.ACCOUNT_EDITOR -> {
                screen.update { SetUpScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpScreen.SET_UP_INCOME -> {
                screen.update { SetUpScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpScreen.SET_UP_CATEGORIES -> {
                screen.update { SetUpScreen.SET_UP_INCOME }
                false
            }

            SetUpScreen.CATEGORY_EDITOR -> {
                screen.update { SetUpScreen.SET_UP_CATEGORIES }
                false
            }

            SetUpScreen.SET_UP_BUDGET -> {
                screen.update { SetUpScreen.SET_UP_CATEGORIES }
                false
            }
        }
    }

    fun nextScreen(): Boolean {
        val currentScreen = uiState.value.screen
        return when (currentScreen) {
            SetUpScreen.WELCOME -> {
                screen.update { SetUpScreen.SET_UP_CURRENCY }
                false
            }

            SetUpScreen.SET_UP_CURRENCY -> {
                screen.update { SetUpScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpScreen.SET_UP_ACCOUNTS -> {
                if (accounts.value.isEmpty()) {
                    toastMessage.update { Event(ACCOUNTS_EMPTY_TOAST_MSG) }
                } else{
                    replaceIncomeAccountIfNotExist()
                    screen.update { SetUpScreen.SET_UP_INCOME }
                }
                false
            }

            SetUpScreen.SET_UP_INCOME -> {
                if (setUpIncomeState.value.amount == BigDecimal.ZERO) {
                    toastMessage.update { Event(INCOME_ZERO_TOAST_MSG) }
                } else {
                    screen.update { SetUpScreen.SET_UP_CATEGORIES }
                }
                false
            }

            SetUpScreen.SET_UP_CATEGORIES -> {
                screen.update { SetUpScreen.SET_UP_BUDGET }
                false
            }

            SetUpScreen.SET_UP_BUDGET -> true

            else -> false
        }
    }

    // Default Currency
    fun setDefaultCurrency(currency: Currency) {
        currencyRepository.setDefaultCurrency(currency)
    }

    // Editing Account
    fun addAccount() {
        editingAccountId = null
        screen.update { SetUpScreen.ACCOUNT_EDITOR }
    }

    fun updateAccount(accountId: Int) {
        editingAccountId = accountId
        screen.update { SetUpScreen.ACCOUNT_EDITOR }
    }

    // Income
    private fun replaceIncomeAccountIfNotExist() {
        if (accounts.value.isEmpty()) return

        // Check if the account set in income still exists. Otherwise, set the income account to be the first of list of accounts if it's not empty
        if (setUpIncomeState.value.account == null || !accounts.value.contains(setUpIncomeState.value.account)) {
            setUpIncomeState.update { setUpIncomeState.value.copy(account = accounts.value[0]) }
        }
    }

    // Editing Category
    fun addCategory() {
        editingCategoryId = null
        screen.update { SetUpScreen.CATEGORY_EDITOR }
    }

    fun updateCategory(categoryId: Int) {
        editingCategoryId = categoryId
        screen.update { SetUpScreen.CATEGORY_EDITOR }
    }

    fun updateIncomeState(isIncome: Boolean, amount:BigDecimal, isMonthly: Boolean, account: Account?) {
        setUpIncomeState.update { SetUpIncomeState(isIncome, amount, isMonthly, account) }
    }

    companion object {
        private const val ACCOUNTS_EMPTY_TOAST_MSG = "Please create an account to continue"
        private const val INCOME_ZERO_TOAST_MSG = "Please set an income/budget"
    }
}