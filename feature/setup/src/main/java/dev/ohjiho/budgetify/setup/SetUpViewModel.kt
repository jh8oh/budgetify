package dev.ohjiho.budgetify.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.flow.Event
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
    val toastMessage: Event<String?> = Event(null),
)

@HiltViewModel
internal class SetUpViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    accountRepository: AccountRepository,
) : ViewModel() {

    private val screen = MutableStateFlow(SetUpScreen.WELCOME)
    private val toastMessage = MutableStateFlow<Event<String?>>(Event(null))
    var editingAccountId: Int? = null

    val uiState = combine(screen, toastMessage) { screen, toastMessage ->
        SetUpUiState(screen = screen, toastMessage = toastMessage)
    }.stateIn(viewModelScope, WhileUiSubscribed, SetUpUiState())

    val defaultCurrency = currencyRepository.getDefaultCurrencyAsFlow()
        .stateIn(viewModelScope, WhileUiSubscribed, Currency.getInstance(getLocale()))
    val accounts = accountRepository.getAllAccounts().stateIn(viewModelScope, WhileUiSubscribed, emptyList())

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
                if (accounts.value.isEmpty()) {
                    toastMessage.update { Event(ACCOUNTS_EMPTY_TOAST_MSG) }
                } else {
                    screen.update { SetUpScreen.SET_UP_INCOME }
                }
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

    companion object {
        private const val ACCOUNTS_EMPTY_TOAST_MSG = "Please create an account to continue"
    }
}