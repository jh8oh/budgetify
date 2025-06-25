package dev.ohjiho.budgetify.presentation.widget.moneyinput

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintSet
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

    private var style: Style = Style.STANDARD

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

    // ConstraintSets
    private val standardConstraintSet by lazy {
        ConstraintSet().apply {
            clone(largeConstraintSet)
            with(binding) {
                connect(amount.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, resources.getDimensionPixelSize(R.dimen.widget_money_display_padding_horizontal))
                clear(decimal.id, ConstraintSet.END)
                clear(currency.id, ConstraintSet.START)
                connect(currency.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, resources.getDimensionPixelSize(R.dimen.widget_money_display_padding_horizontal))
            }
        }
    }
    private val largeConstraintSet = ConstraintSet().apply { clone(binding.root) }

    // Resources
    private val borderStrokeWidth by lazy { resources.getDimension(R.dimen.widget_money_display_border_stroke_width) }
    private val borderStrokeCornerRadius by lazy { resources.getDimension(R.dimen.widget_money_display_border_stroke_corner_radius) }

    init {
        val borderShapeAppearanceModel = ShapeAppearanceModel.Builder()
            .setAllCornerSizes(borderStrokeCornerRadius)
            .build()
        val cutoutDrawable = CutoutDrawable.create(borderShapeAppearanceModel).apply {
            setStroke(
                borderStrokeWidth,
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
            setStyle(Style.entries.toTypedArray()[getInt(R.styleable.MoneyDisplay_style, 0)])
            setHint(getString(R.styleable.MoneyDisplay_android_hint))
        }
        setCurrency(Currency.getInstance(getLocale(context)))
        setAmount(BigDecimal.ZERO)
    }

    fun setStyle(style: Style) {
        this.style = style

        with(binding) {
            when (style) {
                Style.STANDARD -> {
                    currency.setTextAppearance(themeR.style.TextAppearance_Budgetify_Body)
                    amount.setTextAppearance(themeR.style.TextAppearance_Budgetify_Body)
                    decimal.setTextAppearance(themeR.style.TextAppearance_Budgetify_Body)

                    standardConstraintSet.applyTo(binding.root)
                }

                Style.LARGE -> {
                    currency.apply {
                        setTextAppearance(themeR.style.TextAppearance_Budgetify_BodyLarge)
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.widget_money_display_currency_text_size))
                        setTypeface(currency.typeface, Typeface.BOLD)
                    }
                    amount.setTextAppearance(themeR.style.TextAppearance_Budgetify_HeadlineExtraLarge)
                    decimal.setTextAppearance(themeR.style.TextAppearance_Budgetify_HeadlineLarge)

                    largeConstraintSet.applyTo(binding.root)
                }
            }
        }
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
        enum class Style {
            STANDARD, LARGE
        }
    }
}