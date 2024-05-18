package dev.ohjiho.budgetify.setup.accounts

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.setup.databinding.ItemAccountBinding
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat

internal class SetUpAccountsAdapter(private val onClick: (AccountEntity) -> Unit) :
    RecyclerView.Adapter<SetUpAccountsAdapter.ViewHolder>() {

    private var accountList = listOf<AccountEntity>()

    inner class ViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: AccountEntity) {
            binding.accountColor.setBackgroundColor(Color.parseColor(account.color))
            binding.accountName.text = account.name
            binding.accountBalance.text = account.balance.toCurrencyFormat(account.currency, binding.root.context)
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
        accountList = newAccountList
        notifyDataSetChanged()
    }
}