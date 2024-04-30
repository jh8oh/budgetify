package dev.ohjiho.budgetify.ui.currency

import android.annotation.SuppressLint
import android.icu.util.Currency
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import dev.ohjiho.budgetify.databinding.ItemCurrencyBinding

class CurrencyAdapter(private val currencies: List<Currency>, private val listener: Listener) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>(), Filterable {

    private var filteredCurrencies = currencies
    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if (!constraint.isNullOrEmpty()) {
                val lowerCaseConstraint = constraint.toString().lowercase()
                filteredCurrencies = currencies.filter {
                    it.currencyCode.lowercase().contains(lowerCaseConstraint) || it.displayName.lowercase().contains(lowerCaseConstraint)
                }
            }
            return FilterResults().apply {
                values = filteredCurrencies
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (filteredCurrencies.size != currencies.size) notifyDataSetChanged()
        }
    }

    interface Listener {
        fun onItemSelected(currency: Currency)
    }

    inner class ViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            binding.currencyCode.text = currency.currencyCode
            binding.currencyLongName.text = currency.displayName

            binding.root.setOnClickListener {
                listener.onItemSelected(currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredCurrencies[position])
    }

    override fun getItemCount() = filteredCurrencies.size

    override fun getFilter(): Filter = filter
}