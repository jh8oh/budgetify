package dev.ohjiho.budgetify.utils.ui

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import java.util.Currency

fun EditText.reformatBalanceAfterTextChange(currency: Currency) {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            setText(text.toString().toCurrencyFormat(currency, context))
        }
    }
    setOnEditorActionListener { _, actionId, _ ->
        return@setOnEditorActionListener when (actionId) {
            EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_PREVIOUS -> {
                setText(text.toString().toCurrencyFormat(currency, context))
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
                true
            }

            else -> false
        }
    }
}