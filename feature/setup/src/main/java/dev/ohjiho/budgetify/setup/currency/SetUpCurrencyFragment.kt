package dev.ohjiho.budgetify.setup.currency

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.setup.SetUpViewModel
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpCurrencyBinding
import dev.ohjiho.currencypicker.CurrencySpinner
import kotlinx.coroutines.launch
import java.util.Currency

class SetUpCurrencyFragment : Fragment(), CurrencySpinner.Listener {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpCurrencyBinding

    private lateinit var dialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpCurrencyBinding.inflate(inflater)

        val spinner = CurrencySpinner(requireContext()).apply {
            setListener(this@SetUpCurrencyFragment)
        }
        dialog = AlertDialog.Builder(requireContext()).setView(spinner).create()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.defaultCurrency.text = it.defaultCurrency.currencyCode
                }
            }
        }

        binding.defaultCurrency.setOnClickListener {
            dialog.show()
        }

        return binding.root
    }

    override fun onCurrencySelected(currency: Currency) {
        viewModel.setDefaultCurrency(currency)
        dialog.dismiss()
    }
}