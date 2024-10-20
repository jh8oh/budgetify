package dev.ohjiho.budgetify.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.Currency

interface CurrencyRepository {
    fun getDefaultCurrencyAsFlow(): Flow<Currency>
    fun getDefaultCurrency(): Currency
    fun setDefaultCurrency(currency: Currency)
}