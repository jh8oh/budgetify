package dev.ohjiho.budgetify.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import dev.ohjiho.budgetify.domain.model.Reoccurrence
import dev.ohjiho.budgetify.domain.model.Reoccurrence.Companion.NEVER_REPEATED_DISPLAY_TEXT
import dev.ohjiho.budgetify.presentation.databinding.WidgetRepeatDisplayBinding

class RepeatDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: WidgetRepeatDisplayBinding = WidgetRepeatDisplayBinding.inflate(LayoutInflater.from(context), this, true)

    var reoccurrence: Reoccurrence? = null
        set(value) {
            field = value
            updateView()
        }

    init {
        updateView()
        setUpDisplay()
    }

    private fun setUpDisplay() {
        binding.root.setOnClickListener {
            Toast.makeText(context, "Repeat Dialog to open", Toast.LENGTH_SHORT).show()
            // TODO: repeat dialog
        }
    }

    private fun updateView() {
        binding.repeatText.text = reoccurrence?.toString() ?: NEVER_REPEATED_DISPLAY_TEXT
    }
}