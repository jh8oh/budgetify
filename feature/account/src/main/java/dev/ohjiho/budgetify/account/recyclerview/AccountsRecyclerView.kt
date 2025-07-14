package dev.ohjiho.budgetify.account.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.domain.model.Account

class AccountsRecyclerView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attr, defStyleAttr) {

    private var listener: Listener? = if (context is Listener) context else null

    init {
        adapter = AccountsAdapter { listener?.onClick(it) }

        layoutManager = LinearLayoutManager(context)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setAccountList(accountList: List<Account>) {
        (adapter as AccountsAdapter).setAccountList(accountList)
    }

    interface Listener {
        fun onClick(account: Account)
    }
}