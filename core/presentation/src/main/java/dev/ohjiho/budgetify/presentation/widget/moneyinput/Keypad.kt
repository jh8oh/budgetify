package dev.ohjiho.budgetify.presentation.widget.moneyinput

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.ohjiho.budgetify.presentation.databinding.WidgetKeypadBinding
import dev.ohjiho.budgetify.utils.data.getDecimalAmount
import dev.ohjiho.budgetify.utils.data.getLocale
import java.text.DecimalFormatSymbols
import java.util.Currency

class Keypad @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = WidgetKeypadBinding.inflate(LayoutInflater.from(context), this, true)
    private var listener: Listener? = null

    private val decimalSymbol = DecimalFormatSymbols.getInstance(getLocale(context)).decimalSeparator

    init {
        setUpKeypad()
    }

    private fun setUpKeypad() {
        with(binding) {
            dot.text = decimalSymbol.toString()

            val keypadButtons = arrayOf(zero, one, two, three, four, five, six, seven, eight, nine)
            keypadButtons.forEachIndexed { index, button ->
                button.setOnClickListener {
                    listener?.onKeyPressed(index)
                }
            }
            dot.setOnClickListener {
                listener?.onDotPressed()
            }
            backspace.setOnClickListener {
                listener?.onBackspacePressed()
            }
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setCurrency(currency: Currency) {
        binding.dot.visibility = if (currency.getDecimalAmount() > 0) View.VISIBLE else View.INVISIBLE
    }

    interface Listener {
        fun onKeyPressed(key: Int)
        fun onDotPressed()
        fun onBackspacePressed()
    }
}