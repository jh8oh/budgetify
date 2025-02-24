package dev.ohjiho.budgetify.account.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import dev.ohjiho.budgetify.account.R
import dev.ohjiho.budgetify.account.databinding.WidgetAccountContainerBinding
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.model.AccountType

class AccountContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = WidgetAccountContainerBinding.inflate(LayoutInflater.from(context), this, true)

    var account: Account? = null
        set(value) {
            field = value

            value?.let {
                binding.accountIcon.setImageResource(
                    when (it.type) {
                        AccountType.CASH -> R.drawable.ic_account_cash
                        AccountType.CREDIT -> R.drawable.ic_account_credit
                        AccountType.INVESTMENTS -> R.drawable.ic_account_investment
                    }
                )

                binding.accountName.text = it.name
            }
        }
}