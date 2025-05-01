package dev.ohjiho.budgetify.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import dev.ohjiho.budgetify.presentation.R
import dev.ohjiho.budgetify.presentation.databinding.WidgetMoneyDisplayBinding
import dev.ohjiho.budgetify.utils.data.getDecimalAmount
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.util.Currency
import dev.ohjiho.budgetify.theme.R as themeR

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

    var size: Size = Size.LARGE
        set(value) {
            field = value
            with(binding) {
                when (value) {
                    Size.LARGE -> {
                        amount.setTextAppearance(themeR.style.TextAppearance_Budgetify_HeadlineExtraLarge)
                        currency.apply {
                            setTextAppearance(themeR.style.TextAppearance_Budgetify_BodyLarge)
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.widget_money_display_currency_text_size))
                            setTypeface(currency.typeface, Typeface.BOLD)
                        }
                    }

                    Size.SMALL -> {
                        amount.setTextAppearance(themeR.style.TextAppearance_Budgetify_HeadlineLarge)
                        currency.apply {
                            setTextAppearance(themeR.style.TextAppearance_Budgetify_Body)
                            setTypeface(currency.typeface, Typeface.BOLD)
                        }
                    }
                }
            }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.MoneyDisplay) {
            size = Size.entries.toTypedArray()[getInt(R.styleable.MoneyDisplay_size, 0)]
        }
        currency = Currency.getInstance(getLocale(context))
        setAmount(BigDecimal.ZERO)
    }

    fun setAmount(amount: BigDecimal) {
        val integerAndDecimal = amount.toString().split(decimalSymbol)
        val hasDecimal = amount.stripTrailingZeros().scale() > 0

        this.amount = integerAndDecimal[0]
        this.decimalAmount = if (hasDecimal) integerAndDecimal[1] else ""
        inDecimalMode = hasDecimal
    }

    fun getAmount(): BigDecimal {
        val nonEmptyAmount = amount.ifEmpty { "0" }
        val nonEmptyDecimalAmount = decimalAmount.ifEmpty { "0" }
        return if (nonEmptyAmount == "0" && nonEmptyDecimalAmount == "0") BigDecimal.ZERO else BigDecimal("$nonEmptyAmount.$nonEmptyDecimalAmount")
    }

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

    fun setCurrencyTextColor(@ColorInt color: Int) {
        binding.currency.setTextColor(color)
    }

    companion object {
        enum class Size {
            LARGE, SMALL
        }
    }
}