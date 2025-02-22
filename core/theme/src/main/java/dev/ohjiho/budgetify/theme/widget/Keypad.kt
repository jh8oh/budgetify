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

            one.setOnClickListener { listener?.onKeyPressed(1) }
            two.setOnClickListener { listener?.onKeyPressed(2) }
            three.setOnClickListener { listener?.onKeyPressed(3) }
            four.setOnClickListener { listener?.onKeyPressed(4) }
            five.setOnClickListener { listener?.onKeyPressed(5) }
            six.setOnClickListener { listener?.onKeyPressed(6) }
            seven.setOnClickListener { listener?.onKeyPressed(7) }
            eight.setOnClickListener { listener?.onKeyPressed(8) }
            nine.setOnClickListener { listener?.onKeyPressed(9) }
            zero.setOnClickListener { listener?.onKeyPressed(0) }
            dot.setOnClickListener { listener?.onDotPressed() }
            backspace.setOnClickListener { listener?.onBackspacePressed() }
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setCurrency(currency: Currency) {
        binding.dot.visibility = if (currency.getDecimalAmount() > 0) View.VISIBLE else View.INVISIBLE
    }
}