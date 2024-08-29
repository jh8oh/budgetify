package dev.ohjiho.budgetify.account.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.account.databinding.ItemAccountBinding
import dev.ohjiho.account.databinding.ItemAccountHeaderBinding
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat

internal class AccountsAdapter(private val onClick: (Account) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var accounts = emptyList<Any>()

    inner class AccountHeaderViewHolder(private val binding: ItemAccountHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(type: AccountType) {
            with(binding) {
                accountType.text = type.toString()
            }
        }
    }

    inner class AccountViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(account: Account) {
            with(binding) {
                accountName.text = account.name
                if (account.institution.isBlank()) {
                    accountInstitution.visibility = View.GONE
                } else {
                    accountInstitution.text = account.institution
                }
                accountBalance.text = account.balance.toCurrencyFormat(account.currency, true, binding.root.context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (accounts[position]) {
            is AccountType -> HEADER_VIEW_TYPE
            else -> ACCOUNT_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER_VIEW_TYPE -> AccountHeaderViewHolder(ItemAccountHeaderBinding.inflate(layoutInflater, parent, false))
            else -> AccountViewHolder(ItemAccountBinding.inflate(layoutInflater, parent, false)).apply {
                itemView.setOnClickListener {
                    onClick(accounts[adapterPosition] as Account)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER_VIEW_TYPE -> (holder as AccountHeaderViewHolder).bind(accounts[position] as AccountType)
            else -> (holder as AccountViewHolder).bind(accounts[position] as Account)
        }
    }

    override fun getItemCount() = accounts.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAccountList(newAccountList: List<Account>) {
        newAccountList.groupBy { it.type }.let { map ->
            val cashAccounts = map[AccountType.CASH]?.let { listOf(AccountType.CASH) + it } ?: emptyList()
            val creditAccounts = map[AccountType.CREDIT]?.let { listOf(AccountType.CREDIT) + it } ?: emptyList()
            val investmentAccounts = map[AccountType.INVESTMENTS]?.let { listOf(AccountType.INVESTMENTS) + it } ?: emptyList()
            accounts = cashAccounts + creditAccounts + investmentAccounts
        }

        // TODO Create diff util so that we don't have to use notifyDataSetChanged()
        notifyDataSetChanged()
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ACCOUNT_VIEW_TYPE = 1
    }
}