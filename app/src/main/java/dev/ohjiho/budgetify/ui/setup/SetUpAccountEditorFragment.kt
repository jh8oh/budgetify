package dev.ohjiho.budgetify.ui.setup

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.data.model.AccountType
import dev.ohjiho.budgetify.databinding.FragmentAccountEditorBinding
import dev.ohjiho.budgetify.util.getLocale
import dev.ohjiho.budgetify.util.toCurrencyFormat
import dev.ohjiho.colorpicker.ColorPicker
import dev.ohjiho.currencypicker.CurrencySpinner
import kotlinx.coroutines.launch
import java.util.Currency

class SetUpAccountEditorFragment : Fragment(), ColorPicker.Listener, CurrencySpinner.Listener {

    private lateinit var binding: FragmentAccountEditorBinding
    private val viewModel by activityViewModels<SetUpViewModel>()

    private lateinit var colorPickerDialog: AlertDialog
    private lateinit var currencyPickerDialog: AlertDialog

    private val accountTypeArray = AccountType.entries

    override fun onAttach(context: Context) {
        super.onAttach(context)

        colorPickerDialog = AlertDialog.Builder(this.requireContext()).setView(
            ColorPicker(this.requireContext(), this).setColors(R.array.colors).createView()
        ).create()

        val currencySpinner = CurrencySpinner(this.requireContext()).apply {
            setListener(this@SetUpAccountEditorFragment)
        }
        currencyPickerDialog = AlertDialog.Builder(this.requireContext()).setView(currencySpinner).create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountEditorBinding.inflate(inflater)

        with(binding) {
            binding.accountColor.setOnClickListener {
                colorPickerDialog.show()
            }

            binding.accountCurrency.setOnClickListener {
                currencyPickerDialog.show()
            }

            val adapter = ArrayAdapter(
                this@SetUpAccountEditorFragment.requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                accountTypeArray.map { it.displayName }).also {
                binding.accountType.adapter = it
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { uiState ->
                        if (uiState.editingAccount == null){

                        }

                        uiState.editingAccount?.let { account ->
                            val locale = context?.let { getLocale(it) } ?: getLocale()
                            binding.accountName.setText(account.name)
                            binding.accountColor.setColorFilter(Color.parseColor(account.color))
                            binding.accountStartingAmount.setText(account.startingAmount.toCurrencyFormat(locale))
                            binding.accountCurrency.text = account.currency.currencyCode
                            binding.accountType.setSelection(adapter.getPosition(account.accountType.displayName))
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onColorClick(colorInt: Int) {
        TODO("Not yet implemented")
    }

    override fun onCurrencySelected(currency: Currency) {
        TODO("Not yet implemented")
    }
}