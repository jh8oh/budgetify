package dev.ohjiho.account.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.account.databinding.ItemAccountBinding
import dev.ohjiho.account.databinding.ItemAccountHeaderBinding
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat

internal class AccountsAdapter(private val onClick: (AccountEntity) -> Unit) :
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
        fun bind(account: AccountEntity) {
            with(binding) {
                accountName.text = account.name
                accountInstitution.text = account.institution
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
                    onClick(accounts[adapterPosition] as AccountEntity)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER_VIEW_TYPE -> (holder as AccountHeaderViewHolder).bind(accounts[position] as AccountType)
            else -> (holder as AccountViewHolder).bind(accounts[position] as AccountEntity)
        }
    }

    override fun getItemCount() = accounts.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAccountList(newAccountList: List<AccountEntity>) {
        newAccountList.groupBy { it.accountType }.let { map ->
            val liquidAccounts = map[AccountType.LIQUID]?.let { listOf(AccountType.LIQUID) + it } ?: emptyList()
            val debtAccounts = map[AccountType.DEBT]?.let { listOf(AccountType.DEBT) + it } ?: emptyList()
            val investmentAccounts = map[AccountType.INVESTMENTS]?.let { listOf(AccountType.INVESTMENTS) + it } ?: emptyList()
            accounts = liquidAccounts + debtAccounts + investmentAccounts
        }

        // TODO Create diff util so that we don't have to use notifyDataSetChanged()
        notifyDataSetChanged()
    }

    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ACCOUNT_VIEW_TYPE = 1
    }
}