package dev.ohjiho.budgetify.ui.setup

import android.icu.util.Currency
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Locale
import javax.inject.Inject

enum class SetUpUiScreen {
    WELCOME, SET_UP_INCOME, SET_UP_BUDGET
}

data class SetUpUiState(
    val screen: SetUpUiScreen = SetUpUiScreen.WELCOME,
    val income: Int? = null,
    val mainCurrency: Currency = Currency.getInstance(Locale.getDefault()),
)

@HiltViewModel
class SetUpViewModel @Inject constructor() : ViewModel() {
    private val screen = MutableStateFlow(SetUpUiScreen.WELCOME)
    private val income: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val mainCurrency = MutableStateFlow(Currency.getInstance(Locale.getDefault()))

    val uiState = combine(screen, income, mainCurrency) { screen, income, mainCurrency ->
        SetUpUiState(screen = screen, income = income, mainCurrency = mainCurrency)
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

            SetUpUiScreen.SET_UP_INCOME -> {
                screen.update { SetUpUiScreen.WELCOME }
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
    fun nextScreen(): Boolean {
        // TODO Check whether values of screens are correct
        val currentScreen = uiState.value.screen
        return when (currentScreen) {
            SetUpUiScreen.WELCOME -> {
                screen.update { SetUpUiScreen.SET_UP_INCOME }
                false
            }

            SetUpUiScreen.SET_UP_INCOME -> {
                screen.update { SetUpUiScreen.SET_UP_BUDGET }
                false
            }

            SetUpUiScreen.SET_UP_BUDGET -> true
        }
    }

    fun onIncomeTextChange(input: Int?){
        income.update { input }
    }

    fun onCurrencySelected(currency: Currency) {
        mainCurrency.update { currency }
    }
}