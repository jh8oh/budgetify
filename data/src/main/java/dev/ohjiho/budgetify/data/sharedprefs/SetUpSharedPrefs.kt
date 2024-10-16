package dev.ohjiho.budgetify.data.sharedprefs

import android.content.SharedPreferences

internal class SetUpSharedPrefs(private val sharedPrefs: SharedPreferences) {
    companion object {
        const val SET_UP_SHARED_PREFS = "SetUpSharedPrefs"
        private const val IS_SET_UP_KEY = "IsSetUp"
    }

    var isSetUp: Boolean
        get() = sharedPrefs.getBoolean(IS_SET_UP_KEY, false)
        set(value) = sharedPrefs.edit().putBoolean(IS_SET_UP_KEY, value).apply()
}
