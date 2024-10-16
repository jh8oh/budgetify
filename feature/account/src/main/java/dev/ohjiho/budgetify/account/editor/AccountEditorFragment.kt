package dev.ohjiho.budgetify.account.editor

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.account.R
import dev.ohjiho.budgetify.account.databinding.FragmentAccountEditorBinding
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.theme.fragment.EditorFragment
import dev.ohjiho.budgetify.utils.data.toBigDecimalAfterSanitize
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import dev.ohjiho.budgetify.utils.ui.reformatBalanceAfterTextChange
import dev.ohjiho.currencypicker.CurrencyPicker
import kotlinx.coroutines.launch
import java.util.Currency

@AndroidEntryPoint
class AccountEditorFragment : EditorFragment() {

    private val viewModel by viewModels<AccountEditorViewModel>()
    private lateinit var binding: FragmentAccountEditorBinding

    // Adapter
    private val institutionAdapter by lazy {
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayListOf<String>())
    }

    // Dialogs
    private val currencyPicker by lazy {
        CurrencyPicker(requireContext()).apply {
            setListener(object : CurrencyPicker.Listener {
                override fun onCurrencySelected(currency: Currency) {
                    binding.accountCurrency.setText(currency.currencyCode)
                    binding.accountBalance.reformatBalanceAfterTextChange(currency)
                    currencySpinnerDialog.dismiss()
                }
            })
        }
    }
    private val currencySpinnerDialog: AlertDialog by lazy {
        AlertDialog.Builder(requireContext()).setView(currencyPicker).create()
    }
    private val moreInfoDialog: AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_account_type_more_info)
            .create()
    }

    // Resources
    override val newTitle by lazy { resources.getString(R.string.fragment_account_editor_add_title) }
    override val updateTitle by lazy { resources.getString(R.string.fragment_account_editor_update_title) }
    private val accountNameBlankError by lazy { resources.getString(R.string.fragment_account_editor_name_blank_error) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.getInt(ACCOUNT_ID_ARG)?.let {
                viewModel.initWithId(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountEditorBinding.inflate(inflater)

        setUpEditorAppBar(viewModel.isNew)

        with(binding) {
            // Populate views
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.uniqueInstitution.collect {
                            institutionAdapter.apply {
                                clear()
                                addAll(it)
                                notifyDataSetChanged()
                            }
                        }
                    }
                    launch {
                        viewModel.account.collect { account ->
                            accountName.setText(account.name)
                            accountInstitution.setText(account.institution)
                            accountBalance.setText(account.balance.toCurrencyFormat(account.currency, false, context))
                            currencyPicker.setSelectedCurrency(account.currency)
                            accountCurrency.setText(account.currency.currencyCode)
                            when (account.type) {
                                AccountType.CASH -> accountTypeToggleGroup.check(cashButton.id)
                                AccountType.CREDIT -> accountTypeToggleGroup.check(creditButton.id)
                                AccountType.INVESTMENTS -> accountTypeToggleGroup.check(investmentsButton.id)
                            }
                        }
                    }
                }
            }

            // Name
            accountName.doAfterTextChanged {
                // Remove error once any text has been inputted
                accountName.error = null
            }
            // Institution
            accountInstitution.setAdapter(institutionAdapter)
            // Currency
            accountCurrency.setOnClickListener { showCurrencyPickerDialog() }
            accountCurrencyContainer.setEndIconOnClickListener { showCurrencyPickerDialog() }
            // Balance
            accountBalance.reformatBalanceAfterTextChange(viewModel.account.value.currency)
            // More Info
            moreInfoButton.setOnClickListener { moreInfoDialog.show() }
            // Save button
            saveButton.setOnClickListener {
                if (accountName.text.isNullOrBlank()) {
                    accountName.setText("")     // Clears text in case it only contains whitespace
                    accountName.error = accountNameBlankError
                } else {
                    saveState()
                    viewModel.saveAccount()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        return binding.root
    }

    private fun showCurrencyPickerDialog() {
        currencyPicker.setSelectedCurrency(Currency.getInstance(binding.accountCurrency.text.toString()))
        currencySpinnerDialog.show()
    }

    override fun onDelete() {
        viewModel.deleteAccount()
    }

    override fun saveState() {
        with(binding) {
            viewModel.updateState(
                accountName.text.toString().trim(),
                accountInstitution.text.toString().trim(),
                getAccountType(),
                accountBalance.text.toString().toBigDecimalAfterSanitize(),
                Currency.getInstance(accountCurrency.text.toString())
            )
        }
    }

    private fun getAccountType(): AccountType {
        with(binding) {
            return when (accountTypeToggleGroup.checkedButtonId) {
                cashButton.id -> AccountType.CASH
                creditButton.id -> AccountType.CREDIT
                else -> AccountType.INVESTMENTS
            }
        }
    }

    companion object {
        private const val ACCOUNT_ID_ARG = "ACCOUNT_ID"

        fun newInstance(accountId: Int, fromSetUp: Boolean = false) = AccountEditorFragment().apply {
            arguments = Bundle().apply {
                putInt(ACCOUNT_ID_ARG, accountId)
                putBoolean(FROM_SET_UP_ARG, fromSetUp)
            }
        }
    }
}