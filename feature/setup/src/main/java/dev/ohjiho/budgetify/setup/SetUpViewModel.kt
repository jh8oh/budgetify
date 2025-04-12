package dev.ohjiho.budgetify.setup

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.model.Interval
import dev.ohjiho.budgetify.domain.model.Reoccurrence
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import dev.ohjiho.budgetify.utils.flow.Event
import dev.ohjiho.budgetify.utils.flow.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import javax.inject.Inject

internal enum class SetUpScreen {
    WELCOME, SET_UP_CURRENCY, SET_UP_ACCOUNTS, SET_UP_INCOME, SET_UP_BUDGETS, ACCOUNT_EDITOR, CATEGORY_EDITOR,
}

internal data class SetUpUiState(
    val screen: SetUpScreen = SetUpScreen.WELCOME,
    val toastMessage: Event<String?> = Event(null),
)

@Parcelize
internal data class SetUpIncomeState(
    val isIncome: Boolean = true,
    val amount: BigDecimal = BigDecimal.ZERO,
    val reoccurrence: Reoccurrence? = Reoccurrence.with(Interval.WEEKLY, setOf(1)),
    val accountIndex: Int = 0
) : Parcelable

@HiltViewModel
internal class SetUpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val currencyRepository: CurrencyRepository,
    accountRepository: AccountRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val screen = savedStateHandle.getStateFlow(SCREEN_SAVED_STATE_KEY, SetUpScreen.WELCOME)
    private val toastMessage = MutableStateFlow<Event<String?>>(Event(null))
    var editingAccountId: Int? = null
    var editingCategoryId: Int? = null

    val uiState = combine(screen, toastMessage) { screen, toastMessage ->
        SetUpUiState(screen = screen, toastMessage = toastMessage)
    }.stateIn(viewModelScope, WhileUiSubscribed, SetUpUiState())

    var defaultCurrency = currencyRepository.getDefaultCurrency()
        set(value) {
            currencyRepository.setDefaultCurrency(value)
            field = value
        }
    val accounts = accountRepository.getAllAccounts().stateIn(viewModelScope, WhileUiSubscribed, emptyList())
    val setUpIncomeState = savedStateHandle.getStateFlow(INCOME_STATE_SAVED_STATE_KEY, SetUpIncomeState())
    val expenseCategories = categoryRepository.getAllExpenseCategories().stateIn(viewModelScope, WhileUiSubscribed, emptyList())

    // Screen
    fun onBackPressed(): Boolean {
        return when (val currentScreen = uiState.value.screen) {
            SetUpScreen.WELCOME -> true

            SetUpScreen.SET_UP_CURRENCY, SetUpScreen.SET_UP_ACCOUNTS, SetUpScreen.SET_UP_INCOME, SetUpScreen.SET_UP_BUDGETS -> {
                savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.entries[currentScreen.ordinal - 1]
                false
            }

            SetUpScreen.ACCOUNT_EDITOR -> {
                savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.SET_UP_ACCOUNTS
                false
            }

            SetUpScreen.CATEGORY_EDITOR -> {
                savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.SET_UP_BUDGETS
                false
            }

        }
    }

    fun nextScreen(): Boolean {
        return when (val currentScreen = uiState.value.screen) {
            SetUpScreen.WELCOME, SetUpScreen.SET_UP_CURRENCY -> {
                savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.entries[currentScreen.ordinal + 1]
                false
            }

            SetUpScreen.SET_UP_ACCOUNTS -> {
                if (accounts.value.isEmpty()) {
                    toastMessage.update { Event(ACCOUNTS_EMPTY_TOAST_MSG) }
                } else {
                    replaceIncomeAccountIfNotExist()
                    savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.SET_UP_INCOME
                }
                false
            }

            SetUpScreen.SET_UP_INCOME -> {
                if (setUpIncomeState.value.amount == BigDecimal.ZERO) {
                    toastMessage.update { Event(INCOME_ZERO_TOAST_MSG) }
                } else {
                    savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.SET_UP_BUDGETS
                }
                false
            }

            SetUpScreen.SET_UP_BUDGETS -> {
                // Category Check
                true
            }

            else -> false
        }
    }

    // Editing Account
    fun addAccount() {
        editingAccountId = null
        savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.ACCOUNT_EDITOR
    }

    fun updateAccount(accountId: Int) {
        editingAccountId = accountId
        savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.ACCOUNT_EDITOR
    }

    // Income
    private fun replaceIncomeAccountIfNotExist() {
        if (accounts.value.isEmpty()) return

        // Check if the account set in income still exists. Otherwise, set the income account to be the first of list of accounts if it's not empty
        if (setUpIncomeState.value.accountIndex > accounts.value.size) {
            savedStateHandle[INCOME_STATE_SAVED_STATE_KEY] = setUpIncomeState.value.copy(accountIndex = 0)
        }
    }

    fun updateIncomeState(isIncome: Boolean, amount: BigDecimal, reoccurrence: Reoccurrence?, account: Account?) {
        val accountIndex = account?.let {
            val unsafeIndex = accounts.value.indexOf(account)
            if (unsafeIndex == -1) 0 else unsafeIndex
        } ?: 0

        savedStateHandle[INCOME_STATE_SAVED_STATE_KEY] = SetUpIncomeState(isIncome, amount, reoccurrence, accountIndex)
    }

    // Editing Category
    fun addCategory() {
        editingCategoryId = null
        savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.CATEGORY_EDITOR
    }

    fun updateCategory(categoryId: Int) {
        editingCategoryId = categoryId
        savedStateHandle[SCREEN_SAVED_STATE_KEY] = SetUpScreen.CATEGORY_EDITOR
    }

    companion object {
        private const val SCREEN_SAVED_STATE_KEY = "ScreenSavedState"
        private const val INCOME_STATE_SAVED_STATE_KEY = "IncomeSavedState"

        private const val ACCOUNTS_EMPTY_TOAST_MSG = "Please create an account to continue"
        private const val INCOME_ZERO_TOAST_MSG = "Please set an income/budget"
    }
}