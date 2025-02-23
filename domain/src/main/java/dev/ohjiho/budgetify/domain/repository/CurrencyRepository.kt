package dev.ohjiho.budgetify.domain.repository

import java.util.Currency

interface CurrencyRepository {
    fun getDefaultCurrency(): Currency
    fun setDefaultCurrency(currency: Currency)
}