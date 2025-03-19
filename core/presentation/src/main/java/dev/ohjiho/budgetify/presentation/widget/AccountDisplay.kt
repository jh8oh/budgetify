package dev.ohjiho.budgetify.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.presentation.databinding.WidgetAccountDisplayBinding

class AccountDisplay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: WidgetAccountDisplayBinding = WidgetAccountDisplayBinding.inflate(LayoutInflater.from(context), this, true)

    var account: Account? = null
        set(value) {
            field = value
            updateText()
        }

    init {

    }

    private fun updateText() {
        with(binding) {
            account?.let {
                accountIcon.setImageResource(it.type.getIconRes())
                accountName.text = it.name
            }
        }
    }
}