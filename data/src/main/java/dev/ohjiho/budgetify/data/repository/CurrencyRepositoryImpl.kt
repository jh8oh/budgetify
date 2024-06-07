package dev.ohjiho.budgetify.data.repository

import android.content.SharedPreferences
import dev.ohjiho.budgetify.data.sharedprefs.CurrencySharedPrefs
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Currency
import javax.inject.Inject

internal class CurrencyRepositoryImpl @Inject constructor(private val sharedPrefs: CurrencySharedPrefs) : CurrencyRepository {
    override fun getDefaultCurrency(): Flow<Currency> = sharedPrefs.getDefaultCurrencyAsFlow()

    override fun setDefaultCurrency(currency: Currency) {
        sharedPrefs.defaultCurrency = currency
    }
}