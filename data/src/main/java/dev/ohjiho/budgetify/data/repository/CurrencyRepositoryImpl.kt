package dev.ohjiho.budgetify.data.repository

import dev.ohjiho.budgetify.data.sharedprefs.CurrencySharedPrefs
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import java.util.Currency
import javax.inject.Inject

internal class CurrencyRepositoryImpl @Inject constructor(private val sharedPrefs: CurrencySharedPrefs) : CurrencyRepository {
    override fun getDefaultCurrency() = sharedPrefs.defaultCurrency

    override fun setDefaultCurrency(currency: Currency) {
        sharedPrefs.defaultCurrency = currency
    }
}