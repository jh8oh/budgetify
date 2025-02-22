package dev.ohjiho.budgetify.theme.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.ohjiho.budgetify.theme.databinding.WidgetKeypadBinding
import dev.ohjiho.budgetify.utils.data.getDecimalAmount
import dev.ohjiho.budgetify.utils.data.getLocale
import java.text.DecimalFormatSymbols
import java.util.Currency

class Keypad @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = WidgetKeypadBinding.inflate(LayoutInflater.from(context), this, true)
    private var moneyDisplay: MoneyDisplay? = null
    private var listener: Listener? = null

    private val decimalSymbol = DecimalFormatSymbols.getInstance(getLocale(context)).decimalSeparator

    interface Listener {
        fun onKeyPressed(key: Int)
        fun onDotPressed()
        fun onBackspacePressed()
    }

    init {
        setUpKeypad()
    }

    private fun setUpKeypad() {
        with(binding) {
            dot.text = decimalSymbol.toString()

            val keypadButtons = arrayOf(zero, one, two, three, four, five, six, seven, eight, nine)
            keypadButtons.forEachIndexed { index, button ->
                button.setOnClickListener {
                    moneyDisplay?.addDigit(index)
                    listener?.onKeyPressed(index)
                }
            }
            dot.setOnClickListener {
                moneyDisplay?.addDecimalDot()
                listener?.onDotPressed()
            }
            backspace.setOnClickListener {
                moneyDisplay?.backspace()
                listener?.onBackspacePressed()
            }
        }
    }

    fun setMoneyDisplay(moneyDisplay: MoneyDisplay) {
        this.moneyDisplay = moneyDisplay
        setCurrency(moneyDisplay.currency)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setCurrency(currency: Currency) {
        binding.dot.visibility = if (currency.getDecimalAmount() > 0) View.VISIBLE else View.INVISIBLE
    }
}