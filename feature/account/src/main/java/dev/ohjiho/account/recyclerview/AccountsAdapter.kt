package dev.ohjiho.account.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.account.databinding.ItemAccountBinding
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat

internal class AccountsAdapter(private val onClick: (AccountEntity) -> Unit) :
    RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {

    private var accountList = listOf<AccountEntity>()

    inner class ViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(account: AccountEntity) {
            with(binding) {
                accountColor.setBackgroundColor(account.colorInt)
                accountName.text = account.name
                accountBalance.text =
                    "${account.balance.toCurrencyFormat(account.currency, binding.root.context)} ${account.currency.currencyCode}"
                root.setOnClickListener { onClick(account) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accountList[position])
    }

    override fun getItemCount() = accountList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAccountList(newAccountList: List<AccountEntity>) {
        // TODO Create diff util so that we don't have to use notifyDataSetChanged()
        accountList = newAccountList.sortedBy { it.previousId }
        notifyDataSetChanged()
    }
}