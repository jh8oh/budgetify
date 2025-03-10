package dev.ohjiho.budgetify.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import dev.ohjiho.budgetify.domain.model.Repetition
import dev.ohjiho.budgetify.domain.model.Repetition.Companion.NEVER_REPEATED_DISPLAY_TEXT
import dev.ohjiho.budgetify.presentation.databinding.WidgetRepeatDisplayBinding

class RepeatDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: WidgetRepeatDisplayBinding = WidgetRepeatDisplayBinding.inflate(LayoutInflater.from(context), this, true)

    var repetition: Repetition? = null
        set(value) {
            field = value

            binding.repeatText.text = value?.toString() ?: NEVER_REPEATED_DISPLAY_TEXT
        }

    init {
        setUpDisplay()
    }

    private fun setUpDisplay() {
        binding.root.setOnClickListener {
            Toast.makeText(context, "Repeat Dialog to open", Toast.LENGTH_SHORT).show()
            // TODO: repeat dialog
        }
    }
}