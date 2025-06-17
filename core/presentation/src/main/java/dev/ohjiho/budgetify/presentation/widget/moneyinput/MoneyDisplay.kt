package dev.ohjiho.budgetify.presentation.widget.moneyinput

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.google.android.material.shape.ShapeAppearanceModel
import dev.ohjiho.budgetify.presentation.R
import dev.ohjiho.budgetify.presentation.databinding.WidgetMoneyDisplayBinding
import dev.ohjiho.budgetify.presentation.widget.CutoutDrawable
import dev.ohjiho.budgetify.utils.data.getDecimalAmount
import dev.ohjiho.budgetify.utils.data.getLocale
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import dev.ohjiho.budgetify.utils.ui.getColor
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.util.Currency
import com.google.android.material.R as materialR
import dev.ohjiho.budgetify.theme.R as themeR

class MoneyDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = WidgetMoneyDisplayBinding.inflate(LayoutInflater.from(context), this, true)

    private var hint: String? = null
    private var currency: Currency = Currency.getInstance(getLocale(context))
    private var inDecimalMode = false
        private set(value) {
            field = value
            binding.decimal.visibility = if (value) View.VISIBLE else View.GONE
        }
    private val decimalSymbol = DecimalFormatSymbols.getInstance(getLocale(context)).decimalSeparator
    private var integerAmount: String = ""
        private set(value) {
            field = value
            binding.amount.text = value.toCurrencyFormat(currency, context).split(decimalSymbol)[0]
        }
    private var decimalAmount: String = ""
        private set(value) {
            field = value
            binding.decimal.text = if (value.isNotEmpty()) "$decimalSymbol$value" else ""
        }

    init {
        val borderShapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCornerSizes(BORDER_SHAPE_CORNER_SIZE)
            .build()
        val cutoutDrawable = CutoutDrawable.create(borderShapeAppearanceModel).apply {
            setStroke(
                BORDER_SHAPE_STROKE,
                context.getColor(materialR.attr.colorOutline, themeR.color.black_700_alpha_40, themeR.color.black_400)
            )
            fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
        }

        with(binding) {
            border.background = cutoutDrawable
            hint.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
                // offset the position by the margin of the content view
                val realLeft = left - border.left
                val realTop = top - border.top
                val realRigth = right - border.left
                val realBottom = bottom - border.top
                // update the cutout part of the drawable
                cutoutDrawable.setCutout(
                    realLeft.toFloat(),
                    realTop.toFloat(),
                    realRigth.toFloat(),
                    realBottom.toFloat()
                )
            }
        }

        context.withStyledAttributes(attrs, R.styleable.MoneyDisplay) {
            setHint(getString(R.styleable.MoneyDisplay_android_hint))
        }
        setCurrency(Currency.getInstance(getLocale(context)))
        setAmount(BigDecimal.ZERO)
    }

    fun setHint(hint: String?) {
        this.hint = hint

        with(binding.hint) {
            if (hint.isNullOrBlank()) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                text = hint
            }
        }
    }

    fun setCurrency(currency: Currency) {
        this.currency = currency

        if (currency.getDecimalAmount() == 0) {
            decimalAmount = ""
        } else if (decimalAmount.length > currency.getDecimalAmount()) {
            decimalAmount.dropLast(decimalAmount.length - currency.getDecimalAmount())
        }

        binding.currency.text = currency.currencyCode
        binding.amount.text = integerAmount.toCurrencyFormat(currency, context)
    }

    fun getCurrency() = currency

    fun setAmount(amount: BigDecimal) {
        val integerAndDecimal = amount.toString().split(decimalSymbol)
        val hasDecimal = amount.stripTrailingZeros().scale() > 0

        this.integerAmount = integerAndDecimal[0]
        this.decimalAmount = if (hasDecimal) integerAndDecimal[1] else ""
        inDecimalMode = hasDecimal
    }

    fun getAmount(): BigDecimal {
        val nonEmptyAmount = integerAmount.ifEmpty { "0" }
        val nonEmptyDecimalAmount = decimalAmount.ifEmpty { "0" }
        return if (nonEmptyAmount == "0" && nonEmptyDecimalAmount == "0") BigDecimal.ZERO else BigDecimal("$nonEmptyAmount.$nonEmptyDecimalAmount")
    }

    // Keypad methods
    fun addDigit(digit: Int) {
        if (!inDecimalMode) {
            if (integerAmount.isEmpty() && digit == 0) return
            integerAmount += digit
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
        } else if (integerAmount.isNotEmpty()) {
            integerAmount = integerAmount.dropLast(1)
        }
    }

    // Text color methods
    fun setCurrencyTextColor(@ColorInt color: Int) {
        binding.currency.setTextColor(color)
    }

    companion object {
        private const val BORDER_SHAPE_STROKE = 4f
        private const val BORDER_SHAPE_CORNER_SIZE = 16f
    }
}