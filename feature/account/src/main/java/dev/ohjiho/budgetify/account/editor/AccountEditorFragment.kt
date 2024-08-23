package dev.ohjiho.budgetify.account.editor

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.account.R
import dev.ohjiho.account.databinding.FragmentAccountEditorBinding
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.utils.data.toBigDecimalAfterSanitize
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import dev.ohjiho.budgetify.utils.ui.reformatBalanceAfterTextChange
import dev.ohjiho.currencypicker.CurrencySpinner
import kotlinx.coroutines.launch
import java.util.Currency

@AndroidEntryPoint
class AccountEditorFragment : Fragment() {

    private val viewModel by viewModels<AccountEditorViewModel>()
    private lateinit var binding: FragmentAccountEditorBinding
    private var listener: Listener? = null

    // Adapter
    private val institutionAdapter by lazy {
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayListOf<String>())
    }

    // Dialogs
    private val currencySpinnerDialog: AlertDialog by lazy {
        val currencySpinner = CurrencySpinner(requireContext()).apply {
            setListener(object : CurrencySpinner.Listener {
                override fun onCurrencySelected(currency: Currency) {
                    binding.accountCurrency.setText(currency.currencyCode)
                    binding.accountBalance.reformatBalanceAfterTextChange(currency)
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

    // Resources
    private val accountEditorNewTitle by lazy { resources.getString(R.string.fragment_account_editor_add_title) }
    private val appBarNonSetUpColor by lazy {
        val typedValue = TypedValue()
        context?.theme?.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
        typedValue.data
    }
    private val onAppBarNonSetUpColor by lazy {
        val typedValue = TypedValue()
        context?.theme?.resolveAttribute(com.google.android.material.R.attr.colorOnBackground, typedValue, true)
        typedValue.data
    }
    private val accountNameBlankError by lazy { resources.getString(R.string.fragment_account_editor_name_blank_error) }

    interface Listener {
        fun onEditorBack()
    }

    private fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (listener == null) {
            try {
                this.listener = context as Listener
            } catch (e: ClassCastException) {
                Log.e("AccountEditorFragment", "Context must implement AccountEditorFragment.Listener")
                throw e
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.getInt(ACCOUNT_ID_ARG)?.let {
                if (it != 0) {
                    viewModel.initWithAccountId(it)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountEditorBinding.inflate(inflater)

        with(binding) {
            // Check if from set up (Used to set color)
            if (arguments?.getBoolean(FROM_SET_UP_ARG) != true) {
                appBar.setBackgroundColor(appBarNonSetUpColor)
                appBarBack.setColorFilter(onAppBarNonSetUpColor)
                appBarDelete.setColorFilter(onAppBarNonSetUpColor)
                title.setTextColor(onAppBarNonSetUpColor)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Check if new account
                    launch {
                        viewModel.isNewAccount.collect {
                            if (it){
                                appBarDelete.visibility = View.GONE
                                title.text = accountEditorNewTitle
                            }
                        }
                    }
                    // Populate institution adapter
                    launch {
                        viewModel.uniqueInstitution.collect {
                            institutionAdapter.apply {
                                clear()
                                addAll(it)
                                notifyDataSetChanged()
                            }
                        }
                    }
                    // Populate various account EditTexts
                    launch {
                        viewModel.editorAccount.collect { account ->
                            accountName.setText(account.name)
                            accountInstitution.setText(account.institution)
                            accountBalance.setText(account.balance.toCurrencyFormat(account.currency, false, context))
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

            // App bar
            appBarBack.setOnClickListener {
                listener?.onEditorBack()
            }
            appBarDelete.setOnClickListener {
                viewModel.deleteAccount()
                listener?.onEditorBack()
            }
            // Name
            accountName.doAfterTextChanged {
                // Remove error once any text has been inputted
                accountName.error = null
            }
            // Institution
            accountInstitution.setAdapter(institutionAdapter)
            // Currency
            accountCurrency.setOnClickListener { currencySpinnerDialog.show() }
            accountCurrencyContainer.setEndIconOnClickListener { currencySpinnerDialog.show() }
            // Balance
            accountBalance.reformatBalanceAfterTextChange(viewModel.editorAccount.value.currency)
            // More Info
            moreInfoButton.setOnClickListener { moreInfoDialog.show() }
            // Save button
            saveButton.setOnClickListener {
                if (accountName.text.isNullOrBlank()) {
                    accountName.setText("")     // Clears text in case it only contains whitespace
                    accountName.error = accountNameBlankError
                } else {
                    updateAccount()
                    viewModel.saveAccount()
                    listener?.onEditorBack()
                }
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateAccount()
    }

    private fun updateAccount() {
        with(binding){
            viewModel.updateEditorAccount(
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
        private const val FROM_SET_UP_ARG = "FROM_SET_UP"

        fun newInstance(listener: Listener? = null, accountId: Int? = null, fromSetUp: Boolean = false) = AccountEditorFragment().apply {
            arguments = Bundle().apply {
                accountId?.let { putInt(ACCOUNT_ID_ARG, it) }
                putBoolean(FROM_SET_UP_ARG, fromSetUp)
            }

            setListener(listener)
        }
    }
}