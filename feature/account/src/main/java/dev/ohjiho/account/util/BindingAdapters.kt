package dev.ohjiho.account.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import java.util.Currency

internal object BindingAdapters {
    @JvmStatic
    @BindingAdapter("app:tint")
    fun ImageView.setImageTint(@ColorInt color: Int) {
        setColorFilter(color)
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("currencySymbol")
    @JvmStatic
    fun TextView.setCurrencySymbol(currency: Currency) {
        text = "$text ${currency.currencyCode}"
    }
}
