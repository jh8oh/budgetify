package dev.ohjiho.budgetify.ui.setup

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.data.local.entity.AccountEntity
import dev.ohjiho.budgetify.data.model.Account
import dev.ohjiho.budgetify.data.repository.AccountRepository
import dev.ohjiho.budgetify.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class SetUpUiScreen {
    WELCOME, SET_UP_ACCOUNTS, ACCOUNT_EDITOR, SET_UP_INCOME, SET_UP_BUDGET
}

data class SetUpUiState(
    val screen: SetUpUiScreen = SetUpUiScreen.WELCOME,
    val accounts: List<Account> = emptyList(),
    val editingAccount: Account? = null,
)

@HiltViewModel
class SetUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private val screen = MutableStateFlow(SetUpUiScreen.WELCOME)
    private val accounts = accountRepository.getAllAccounts()
    private val editingAccount = MutableStateFlow<Account?>(null)

    val uiState = combine(screen, accounts, editingAccount) { screen, accounts, editingAccount ->
        SetUpUiState(screen = screen, accounts = accounts, editingAccount = editingAccount)
    }.stateIn(viewModelScope, WhileUiSubscribed, SetUpUiState())

    /**
     * Simulates pressing back on the screen.
     *
     * @return true if the current screen is the first screen (Welcome); false otherwise.
     */
    fun onBackPressed(): Boolean {
        val currentScreen = uiState.value.screen

        return when (currentScreen) {
            SetUpUiScreen.WELCOME -> true

            SetUpUiScreen.SET_UP_ACCOUNTS -> {
                screen.update { SetUpUiScreen.WELCOME }
                false
            }

            SetUpUiScreen.ACCOUNT_EDITOR -> {
                screen.update { SetUpUiScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpUiScreen.SET_UP_INCOME -> {
                screen.update { SetUpUiScreen.SET_UP_ACCOUNTS }
                false
            }

            SetUpUiScreen.SET_UP_BUDGET -> {
                screen.update { SetUpUiScreen.SET_UP_INCOME }
                false
            }
        }
    }

    /**
     * Goes to the next screen
     *
     * @return true when nav controller should navigate to next screen. False otherwise.
     */
    fun nextScreen(): Boolean {
        val currentScreen = uiState.value.screen
        return when (currentScreen) {
            SetUpUiScreen.WELCOME -> {
                screen.update { SetUpUiScreen.SET_UP_ACCOUNTS }
                true
            }
            SetUpUiScreen.SET_UP_ACCOUNTS -> {
                if (uiState.value.accounts.isEmpty()){
                    false
                } else {
                    screen.update { SetUpUiScreen.SET_UP_INCOME }
                    true
                }
            }
            SetUpUiScreen.SET_UP_INCOME -> {
                screen.update { SetUpUiScreen.SET_UP_BUDGET }
                true
            }
            else -> false
        }
    }

    fun toAccountEditorScreen(account: Account?) {
        editingAccount.update { account }
        screen.update { SetUpUiScreen.ACCOUNT_EDITOR }
    }

    fun createDefaultAccount() {

    }
}