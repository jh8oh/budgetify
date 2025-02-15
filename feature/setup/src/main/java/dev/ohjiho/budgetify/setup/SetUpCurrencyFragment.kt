package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpCurrencyBinding
import dev.ohjiho.currencypicker.CurrencyPicker
import java.util.Currency

internal class SetUpCurrencyFragment : Fragment(), CurrencyPicker.Listener {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpCurrencyBinding

    // Resources
    private val setUpCurrencyTitle by lazy { resources.getString(R.string.fragment_set_up_currency_title) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpCurrencyBinding.inflate(inflater)

        (requireActivity() as AppCompatActivity).title = setUpCurrencyTitle

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