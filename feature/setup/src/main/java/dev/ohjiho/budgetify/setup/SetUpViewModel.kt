package dev.ohjiho.budgetify.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.utils.ui.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class SetUpScreen {
    WELCOME, SET_UP_ACCOUNTS, SET_UP_INCOME, SET_UP_BUDGET
}

data class SetUpUiState(
    val screen: SetUpScreen = SetUpScreen.WELCOME,
    val accounts: List<AccountEntity> = emptyList(),
)

@HiltViewModel
internal class SetUpViewModel @Inject constructor(accountRepository: AccountRepository) : ViewModel() {
    private val screen = MutableStateFlow(SetUpScreen.WELCOME)
    private val accounts = accountRepository.getAllAccounts()

    val uiState = combine(screen, accounts) { screen, accounts ->
        SetUpUiState(screen = screen, accounts = accounts)
    }.stateIn(viewModelScope, WhileUiSubscribed, SetUpUiState())

    fun onBackPressed(): Boolean {
        val currentScreen = uiState.value.screen

        return when (currentScreen) {
            SetUpScreen.WELCOME -> true

            SetUpScreen.SET_UP_ACCOUNTS -> {
                screen.update { SetUpScreen.WELCOME }
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

            else -> true
        }
    }

}