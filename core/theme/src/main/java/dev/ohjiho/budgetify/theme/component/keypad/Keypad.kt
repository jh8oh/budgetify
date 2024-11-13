package dev.ohjiho.budgetify.theme.component.keypad

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import dev.ohjiho.budgetify.theme.databinding.ComponentKeypadBinding

class Keypad @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = ComponentKeypadBinding.inflate(LayoutInflater.from(context), this, true)

    private var listener: Listener? = null

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
}