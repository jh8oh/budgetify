package dev.ohjiho.budgetify.presentation.widget.moneyinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.ohjiho.budgetify.utils.data.getLocale
import java.math.BigDecimal
import java.util.Currency

class MoneyInputBottomSheetDialogViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val currency = savedStateHandle.getStateFlow(CURRENCY_SAVED_STATE_KEY, Currency.getInstance(getLocale()))
    val amount = savedStateHandle.getStateFlow(AMOUNT_SAVED_STATE_KEY, BigDecimal.ZERO)

    fun initState(currency: Currency?, amount: BigDecimal?) {
        currency?.let { savedStateHandle[CURRENCY_SAVED_STATE_KEY] = it }
        amount?.let { savedStateHandle[AMOUNT_SAVED_STATE_KEY] = it }
    }

    fun updateState(amount: BigDecimal?) {
        amount?.let { savedStateHandle[AMOUNT_SAVED_STATE_KEY] = it }
    }

    companion object {
        private const val CURRENCY_SAVED_STATE_KEY = "CurrencySavedState"
        private const val AMOUNT_SAVED_STATE_KEY = "AmountSavedState"
    }
}