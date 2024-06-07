package dev.ohjiho.budgetify.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.Currency

interface CurrencyRepository {
    fun getDefaultCurrency(): Flow<Currency>
    fun setDefaultCurrency(currency: Currency)
}