package dev.ohjiho.budgetify.data.sharedprefs

import android.content.SharedPreferences

internal class AccountSharedPrefs(private val sharedPrefs: SharedPreferences) {
    companion object {
        const val ACCOUNT_SHARED_PREFS = "AccountSharedPrefs"
        private const val LAST_ID_KEY = "last_id"
    }

    var lastId: Int
        get() = sharedPrefs.getInt(LAST_ID_KEY, 0)
        set(value) = sharedPrefs.edit().putInt(LAST_ID_KEY, value).apply()
}