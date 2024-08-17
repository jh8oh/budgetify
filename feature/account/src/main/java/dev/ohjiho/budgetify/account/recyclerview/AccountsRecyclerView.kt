package dev.ohjiho.budgetify.account.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.domain.model.AccountEntity

class AccountsRecyclerView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attr, defStyleAttr) {

    private var listener: Listener? = null

    interface Listener{
        fun onClick(account: AccountEntity)
    }

    init {
        adapter = AccountsAdapter{
            if (listener == null){
                try {
                    listener = context as Listener
                } catch (e: ClassCastException) {
                    Log.e("AccountsRecyclerView", "Context must implement AccountsRecyclerView.Listener")
                    throw e
                }
            }

            listener!!.onClick(it)
        }

        layoutManager = LinearLayoutManager(context)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setAccountList(accountList: List<AccountEntity>){
        (adapter as AccountsAdapter).setAccountList(accountList)
    }
}