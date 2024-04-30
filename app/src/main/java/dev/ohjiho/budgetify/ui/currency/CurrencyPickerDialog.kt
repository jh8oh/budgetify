package dev.ohjiho.budgetify.ui.currency

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Currency
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ohjiho.budgetify.databinding.DialogCurrencyPickerBinding
import dev.ohjiho.budgetify.util.CurrencyCodes

class CurrencyPickerDialog : DialogFragment(), CurrencyAdapter.Listener {

    private lateinit var listener: Listener

    interface Listener {
        fun onCurrencySelected(currency: Currency)
    }

    companion object {
        const val TAG = "CurrencyPickerDialog"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context doesn't implement CurrencyPickerDialog.Listener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogCurrencyPickerBinding.inflate(layoutInflater)

        val adapter = CurrencyAdapter(CurrencyCodes.getAllCurrencies(), this)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?) = false
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return activity?.let {
            AlertDialog.Builder(context).setView(binding.root).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onItemSelected(currency: Currency) {
        listener.onCurrencySelected(currency)
    }
}