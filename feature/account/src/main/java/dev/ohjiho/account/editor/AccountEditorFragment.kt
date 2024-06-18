package dev.ohjiho.account.editor

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.account.R
import dev.ohjiho.account.databinding.FragmentAccountEditorBinding
import dev.ohjiho.budgetify.utils.primitive.removeLeadingZeros
import dev.ohjiho.budgetify.utils.primitive.toDecimal
import dev.ohjiho.colorpicker.ColorPicker
import dev.ohjiho.currencypicker.CurrencySpinner
import java.math.BigDecimal
import java.util.Currency

@AndroidEntryPoint
class AccountEditorFragment : Fragment() {

    private val viewModel by viewModels<AccountEditorViewModel>()
    private lateinit var binding: FragmentAccountEditorBinding

    private val colorPickerDialog: AlertDialog by lazy {
        val colorPicker = ColorPicker(requireContext(), object : ColorPicker.Listener {
            override fun onColorClick(colorInt: Int) {
                viewModel.updateEditingAccount(colorInt = colorInt)
                colorPickerDialog.dismiss()
            }
        }).setColors(dev.ohjiho.budgetify.theme.R.array.colorpicker_colors).createView()
        AlertDialog.Builder(requireContext()).setView(colorPicker).create()
    }
    private val currencySpinnerDialog: AlertDialog by lazy {
        val currencySpinner = CurrencySpinner(requireContext()).apply {
            setListener(object : CurrencySpinner.Listener {
                override fun onCurrencySelected(currency: Currency) {
                    viewModel.updateEditingAccount(currency = currency)
                    currencySpinnerDialog.dismiss()
                }
            })
        }
        AlertDialog.Builder(requireContext()).setView(currencySpinner).create()
    }
    private val moreInfoDialog: AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_account_type_more_info)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(ACCOUNT_ID_ARG)?.let {
            viewModel.getEditingAccountFromId(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountEditorBinding.inflate(inflater)

        viewModel.editingAccount.observe(viewLifecycleOwner) {
            binding.account = it
        }

        with(binding) {
            accountColor.setOnClickListener { colorPickerDialog.show() }

            accountBalance.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    decimalizeAccountBalance()
                }
            }
            accountBalance.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_PREVIOUS -> {
                        decimalizeAccountBalance()
                        true
                    }

                    else -> false
                }
            }

            accountCurrency.setOnClickListener { currencySpinnerDialog.show() }

            moreInfoButton.setOnClickListener { moreInfoDialog.show() }
        }

        return binding.root
    }

    private fun decimalizeAccountBalance() {
        with(binding) {
            val decimal = accountBalance.text.toString().toDecimal(2).removeLeadingZeros()
            if (accountBalance.text.toString() != decimal) {
                viewModel.updateEditingAccount(balance = BigDecimal(decimal))
                accountBalance.setSelection(decimal.length)
            }
        }
    }

    companion object {
        private const val ACCOUNT_ID_ARG = "ACCOUNT_ID"

        fun newInstance(accountId: Int? = null) = AccountEditorFragment().apply {
            accountId?.let {
                arguments = Bundle().apply {
                    putInt(ACCOUNT_ID_ARG, it)
                }
            }
        }
    }
}