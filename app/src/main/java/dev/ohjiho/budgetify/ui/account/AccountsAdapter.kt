package dev.ohjiho.budgetify.ui.account

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.data.model.Account
import dev.ohjiho.budgetify.databinding.ItemAccountBinding
import dev.ohjiho.budgetify.util.getLocale
import dev.ohjiho.budgetify.util.toCurrencyFormat

class AccountsAdapter(private val onClick: (Account) -> Unit) :
    RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {

    private var accountList = emptyList<Account>()

    inner class ViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            binding.accountColor.setBackgroundColor(Color.parseColor(account.color))
            binding.accountName.text = account.name
            binding.accountIsFavourite.visibility = if (account.isFavourite) View.VISIBLE else View.INVISIBLE
            binding.accountCurrentAmount.text =
                account.currentAmount.toCurrencyFormat(getLocale(binding.root.context))

            binding.root.setOnClickListener { onClick(account) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accountList[position])
    }

    override fun getItemCount() = accountList.size

    fun setAccountList(accountList: List<Account>){
        this.accountList = accountList
    }
}