package dev.ohjiho.budgetify.ui.setup

import android.icu.util.Currency
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.databinding.FragmentSetUpIncomeBinding
import dev.ohjiho.budgetify.ui.currency.CurrencyPickerDialog

class SetUpIncomeFragment : Fragment(), CurrencyPickerDialog.Listener {
    private lateinit var binding: FragmentSetUpIncomeBinding

    private val currencyPickerDialog = CurrencyPickerDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetUpIncomeBinding.inflate(inflater)

        with(binding) {
            currencyInput.setOnClickListener {
                currencyPickerDialog.show(childFragmentManager, CurrencyPickerDialog.TAG)
            }
        }

        return binding.root
    }

    override fun onCurrencySelected(currency: Currency) {
        currencyPickerDialog.dismiss()
    }
}