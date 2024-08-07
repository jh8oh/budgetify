package dev.ohjiho.account.editor

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.account.R
import dev.ohjiho.account.databinding.FragmentAccountEditorBinding
import dev.ohjiho.currencypicker.CurrencySpinner
import java.util.Currency

@AndroidEntryPoint
class AccountEditorFragment : Fragment() {

    private val viewModel by viewModels<AccountEditorViewModel>()
    private lateinit var binding: FragmentAccountEditorBinding

    private var listener: Listener? = null

    private val currencySpinnerDialog: AlertDialog by lazy {
        val currencySpinner = CurrencySpinner(requireContext()).apply {
            setListener(object : CurrencySpinner.Listener {
                override fun onCurrencySelected(currency: Currency) {
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
                if (it != 0){
                    viewModel.initWithAccountId(it)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountEditorBinding.inflate(inflater)

        with(binding) {
        }

        return binding.root
    }

    private fun setListener(listener: Listener?) {
        this.listener = listener
    }

    companion object {
        private const val ACCOUNT_ID_ARG = "ACCOUNT_ID"

        fun newInstance(accountId: Int? = null, listener: Listener? = null) = AccountEditorFragment().apply {
            accountId?.let {
                arguments = Bundle().apply {
                    putInt(ACCOUNT_ID_ARG, it)
                }
            }
            setListener(listener)
        }
    }

    interface Listener {
        fun onSave()
    }
}