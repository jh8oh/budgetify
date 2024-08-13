package dev.ohjiho.account.editor

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import dev.ohjiho.budgetify.utils.data.reformat
import dev.ohjiho.budgetify.utils.data.toBigDecimalAfterSanitize
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import dev.ohjiho.currencypicker.CurrencySpinner
import kotlinx.coroutines.launch
import java.util.Currency

@AndroidEntryPoint
class AccountEditorFragment : Fragment() {

    private val viewModel by viewModels<AccountEditorViewModel>()
    private lateinit var binding: FragmentAccountEditorBinding
    private var listener: Listener? = null

    // Dialogs
    private val currencySpinnerDialog: AlertDialog by lazy {
        val currencySpinner = CurrencySpinner(requireContext()).apply {
            setListener(object : CurrencySpinner.Listener {
                override fun onCurrencySelected(currency: Currency) {
                    viewModel.updateCurrency(currency)
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
    private val accountEditorUpdateTitle by lazy { resources.getString(R.string.fragment_account_editor_update_title) }
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

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayListOf())

        with(binding) {
            appBarBack.setOnClickListener {
                listener?.onEditorBack()
            }
            appBarDelete.setOnClickListener {
                viewModel.deleteAccount()
                listener?.onEditorBack()
            }
            accountName.doAfterTextChanged {
                // Remove error once any text has been inputted
                accountName.error = null
            }
            accountInstitution.setAdapter(adapter)
            accountCurrency.setOnClickListener { currencySpinnerDialog.show() }
            accountCurrencyContainer.setEndIconOnClickListener { currencySpinnerDialog.show() }
            accountBalance.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    reformatBalanceEditText()
                }
            }
            accountBalance.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_PREVIOUS -> {
                        reformatBalanceEditText()
                        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            accountBalance.windowToken,
                            0
                        )
                        true
                    }

                    else -> false
                }
            }
            moreInfoButton.setOnClickListener { moreInfoDialog.show() }
            saveButton.setOnClickListener {
                reformatBalanceEditText()
                if (accountName.text.isNullOrBlank()) {
                    accountName.setText("")     // Clears text in case it only contains whitespace
                    accountName.error = accountNameBlankError
                } else {
                    viewModel.saveAccount(
                        accountName.text.toString().trim(),
                        accountInstitution.text.toString().trim(),
                        getAccountType(),
                        accountBalance.text.toString().toBigDecimalAfterSanitize()
                    )
                    listener?.onEditorBack()
                }
            }

            if (arguments?.getBoolean(FROM_SET_UP_ARG) != true) {
                appBar.setBackgroundColor(appBarNonSetUpColor)
                appBarBack.setColorFilter(onAppBarNonSetUpColor)
                appBarDelete.setColorFilter(onAppBarNonSetUpColor)
                title.setTextColor(onAppBarNonSetUpColor)
            }

            savedInstanceState?.let { state ->
                if (state.getBoolean(IS_CURRENCY_DIALOG_SHOWN)) {
                    currencySpinnerDialog.show()
                }
                if (state.getBoolean(IS_MORE_INFO_DIALOG_SHOWN)) {
                    moreInfoDialog.show()
                }

                accountName.setText(state.getString(NAME_TEXT))
                accountInstitution.setText(state.getString(INSTITUTION_TEXT))
                accountBalance.setText(state.getString(BALANCE_TEXT))
                accountTypeToggleGroup.check(state.getInt(ACCOUNT_TYPE_ID, liquidButton.id))
            } ?: run {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.accountEntity.collect { account ->
                            account?.let {
                                title.text = accountEditorUpdateTitle
                                appBarDelete.visibility = View.VISIBLE
                                accountName.setText(it.name)
                                accountInstitution.setText(it.institution)
                                accountBalance.setText(it.balance.toCurrencyFormat(it.currency, false, context))
                                when (it.accountType) {
                                    AccountType.LIQUID -> accountTypeToggleGroup.check(liquidButton.id)
                                    AccountType.DEBT -> accountTypeToggleGroup.check(debtButton.id)
                                    AccountType.INVESTMENTS -> accountTypeToggleGroup.check(investmentsButton.id)
                                }
                            } ?: run {
                                reformatBalanceEditText()       // Reformats the default balance text ("0")
                            }
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.uniqueInstitution.collect{
                            adapter.apply {
                                clear()
                                addAll(it)
                                notifyDataSetChanged()
                            }
                        }
                    }
                    launch {
                        viewModel.accountCurrency.collect {
                            accountCurrency.setText(it.currencyCode)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_CURRENCY_DIALOG_SHOWN, currencySpinnerDialog.isShowing)
        outState.putBoolean(IS_MORE_INFO_DIALOG_SHOWN, moreInfoDialog.isShowing)
        with(binding) {
            outState.putString(NAME_TEXT, accountName.text.toString())
            outState.putString(INSTITUTION_TEXT, accountInstitution.text.toString())
            outState.putString(BALANCE_TEXT, accountBalance.text.toString())
            outState.putInt(ACCOUNT_TYPE_ID, accountTypeToggleGroup.checkedButtonId)
        }
    }

    private fun setListener(listener: Listener?) {
        this.listener = listener
    }

    private fun reformatBalanceEditText() {
        binding.accountBalance.setText(binding.accountBalance.text.toString().reformat(viewModel.accountCurrency.value, context))
    }

    private fun getAccountType(): AccountType {
        with(binding) {
            return when (accountTypeToggleGroup.checkedButtonId) {
                liquidButton.id -> AccountType.LIQUID
                debtButton.id -> AccountType.DEBT
                else -> AccountType.INVESTMENTS
            }
        }
    }

    companion object {
        private const val ACCOUNT_ID_ARG = "ACCOUNT_ID"
        private const val FROM_SET_UP_ARG = "FROM_SET_UP"
        private const val IS_CURRENCY_DIALOG_SHOWN = "IS_CURRENCY_DIALOG_SHOWN"
        private const val IS_MORE_INFO_DIALOG_SHOWN = "IS_MORE_INFO_DIALOG_SHOWN"
        private const val NAME_TEXT = "NAME_TEXT"
        private const val INSTITUTION_TEXT = "INSTITUTION_TEXT"
        private const val BALANCE_TEXT = "ACCOUNT_BALANCE_TEXT"
        private const val ACCOUNT_TYPE_ID = "ACCOUNT_TYPE_ID"

        fun newInstance(accountId: Int? = null, listener: Listener? = null, fromSetUp: Boolean = false) = AccountEditorFragment().apply {
            arguments = Bundle().apply {
                accountId?.let { putInt(ACCOUNT_ID_ARG, it) }
                putBoolean(FROM_SET_UP_ARG, fromSetUp)
            }

            setListener(listener)
        }
    }

    interface Listener {
        fun onEditorBack()
    }
}