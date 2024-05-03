package dev.ohjiho.budgetify.ui.setup

import android.icu.util.Currency
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class SetUpUiScreen {
    WELCOME, SET_UP_ACCOUNTS, SET_UP_INCOME, SET_UP_BUDGET
}

data class SetUpUiState(
    val screen: SetUpUiScreen = SetUpUiScreen.WELCOME,
)

@HiltViewModel
class SetUpViewModel @Inject constructor() : ViewModel() {
    private val screen = MutableStateFlow(SetUpUiScreen.WELCOME)

    val uiState = screen.map { screen ->
        SetUpUiState(screen = screen)
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
     * @return true if the current screen is the last screen (Budget); false otherwise.
     */
    fun nextScreen() {
        // TODO Check whether values of screens are correct
        val currentScreen = uiState.value.screen
        when (currentScreen) {
            SetUpUiScreen.WELCOME -> screen.update { SetUpUiScreen.SET_UP_ACCOUNTS }
            SetUpUiScreen.SET_UP_ACCOUNTS -> screen.update { SetUpUiScreen.SET_UP_INCOME }
            SetUpUiScreen.SET_UP_INCOME -> screen.update { SetUpUiScreen.SET_UP_BUDGET }
            SetUpUiScreen.SET_UP_BUDGET -> return
        }
    }
}