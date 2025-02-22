package dev.ohjiho.budgetify.theme.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import dev.ohjiho.budgetify.theme.databinding.WidgetMoneyDisplayBinding
import dev.ohjiho.budgetify.utils.data.getDecimalAmount
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.util.Currency

class MoneyDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = WidgetMoneyDisplayBinding.inflate(LayoutInflater.from(context), this, true)

    var currency: Currency = Currency.getInstance(getLocale(context))
        set(value) {
            field = value

            if (value.getDecimalAmount() == 0) {
                decimalAmount = ""
            } else if (decimalAmount.length > value.getDecimalAmount()) {
                decimalAmount.dropLast(decimalAmount.length - value.getDecimalAmount())
            }

            binding.currency.text = value.currencyCode
            binding.amount.text = amount.toCurrencyFormat(value, context)
        }
    private val decimalSymbol = DecimalFormatSymbols.getInstance(getLocale(context)).decimalSeparator

    private var inDecimalMode = false
        private set(value) {
            field = value
            binding.decimal.visibility = if (value) View.VISIBLE else View.GONE
        }
    private var amount: String = ""
        private set(value) {
            field = value
            binding.amount.text = value.toCurrencyFormat(currency, context).split(decimalSymbol)[0]
        }
    private var decimalAmount: String = ""
        private set(value) {
            field = value
            if (value.isNotEmpty()) {
                @SuppressLint("SetTextI18n")
                binding.decimal.text = "$decimalSymbol$value"
            } else {
                binding.decimal.text = ""
            }
        }

    init {
        currency = Currency.getInstance(getLocale(context))
        setAmount(BigDecimal.ZERO)
    }

    fun setAmount(amount: BigDecimal) {
        val fullAmount = amount.toCurrencyFormat(currency, context)
        val integerAndDecimal = fullAmount.split(decimalSymbol)
        val hasDecimal = amount.stripTrailingZeros().scale() > 0

        this.amount = integerAndDecimal[0]
        this.decimalAmount = if (hasDecimal) integerAndDecimal[1] else ""
        inDecimalMode = hasDecimal
    }

    fun getAmount() = BigDecimal("${amount.ifEmpty { "0" }}.${decimalAmount.ifEmpty { "0" }}")

    fun addDigit(digit: Int) {
        if (!inDecimalMode) {
            if (amount.isEmpty() && digit == 0) return
            amount += digit
        } else if (decimalAmount.length < currency.getDecimalAmount()) {
            decimalAmount += digit
        }
    }

    fun addDecimalDot() {
        if (!inDecimalMode) {
            inDecimalMode = true
        }
    }

    fun backspace() {
        if (inDecimalMode) {
            if (decimalAmount.isNotEmpty()) {
                decimalAmount = decimalAmount.dropLast(1)
            } else {
                inDecimalMode = false
            }
        } else if (amount.isNotEmpty()) {
            amount = amount.dropLast(1)
        }
    }

    fun setTextColor(@ColorInt color: Int) {
        with(binding) {
            currency.setTextColor(color)
            amount.setTextColor(color)
            decimal.setTextColor(color)
        }
    }
}