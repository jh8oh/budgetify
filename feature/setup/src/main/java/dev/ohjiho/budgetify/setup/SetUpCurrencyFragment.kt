package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpCurrencyBinding
import dev.ohjiho.currencypicker.CurrencyPicker
import kotlinx.coroutines.launch
import java.util.Currency

internal class SetUpCurrencyFragment : Fragment(), CurrencyPicker.Listener {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpCurrencyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpCurrencyBinding.inflate(inflater)

        binding.currencyPicker.apply {
            setSelectedCurrency(viewModel.defaultCurrency)
            setListener(this@SetUpCurrencyFragment)
        }

        return binding.root
    }

    override fun onCurrencySelected(currency: Currency) {
        viewModel.defaultCurrency = currency
    }
}