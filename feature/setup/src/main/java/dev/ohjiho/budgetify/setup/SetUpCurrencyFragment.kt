package dev.ohjiho.budgetify.setup

import android.os.Bundle
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.defaultCurrency.collect {
                    binding.currencyPicker.setSelectedCurrency(it)
                }
            }
        }

        binding.currencyPicker.setListener(this)

        return binding.root
    }

    override fun onCurrencySelected(currency: Currency) {
        viewModel.setDefaultCurrency(currency)
    }
}