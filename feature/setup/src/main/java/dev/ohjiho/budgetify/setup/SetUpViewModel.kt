package dev.ohjiho.budgetify.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.ui.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Currency
import javax.inject.Inject

enum class SetUpScreen {
    WELCOME, SET_UP_CURRENCY, SET_UP_ACCOUNTS, ACCOUNT_EDITOR_ADD, ACCOUNT_EDITOR_UPDATE, SET_UP_INCOME, SET_UP_BUDGET
}

data class SetUpUiState(
    val screen: SetUpScreen = SetUpScreen.WELCOME,
    val defaultCurrency: Currency = Currency.getInstance(getLocale()),
    val accounts: List<AccountEntity> = emptyList(),
)

@HiltViewModel
internal class SetUpViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    accountRepository: AccountRepository,
) : ViewModel() {

    private val screen = MutableStateFlow(SetUpScreen.WELCOME)
    private val defaultCurrency = currencyRepository.getDefaultCurrencyAsFlow()
    private val accounts = accountRepository.getAllAccounts()
    var editingAccountId: Int? = null

    val uiState = combine(screen, defaultCurrency, accounts) { screen, defaultCurrency, accounts ->
        SetUpUiState(screen = screen, defaultCurrency = defaultCurrency, accounts = accounts)
    }.stateIn(viewModelScope, WhileUiSubscribed, SetUpUiState())

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

            SetUpScreen.ACCOUNT_EDITOR_ADD -> {
                screen.update { SetUpScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpScreen.ACCOUNT_EDITOR_UPDATE -> {
                screen.update { SetUpScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpScreen.SET_UP_INCOME -> {
                screen.update { SetUpScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpScreen.SET_UP_BUDGET -> {
                screen.update { SetUpScreen.SET_UP_INCOME }
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
                screen.update { SetUpScreen.SET_UP_INCOME }
                false
            }

            SetUpScreen.SET_UP_INCOME -> {
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
    fun addOrUpdateAccount(accountId: Int?) {
        editingAccountId = accountId
        if (accountId == null) {
            screen.update { SetUpScreen.ACCOUNT_EDITOR_ADD }
        } else {
            screen.update { SetUpScreen.ACCOUNT_EDITOR_UPDATE }
        }
    }
}