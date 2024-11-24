package dev.ohjiho.budgetify.data.sharedprefs

import android.content.SharedPreferences
import dev.ohjiho.budgetify.utils.data.getLocale
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Currency

internal class CurrencySharedPrefs(private val sharedPrefs: SharedPreferences) {
    companion object {
        const val CURRENCY_SHARED_PREFS = "CurrencySharedPrefs"
        private const val DEFAULT_CURRENCY_KEY = "default_currency"
    }

    var defaultCurrency: Currency
        get() = sharedPrefs.getString(DEFAULT_CURRENCY_KEY, null)?.let { Currency.getInstance(it) } ?: Currency.getInstance(getLocale())
        set(value) = sharedPrefs.edit().putString(DEFAULT_CURRENCY_KEY, value.currencyCode).apply()
}