package dev.ohjiho.budgetify.presentation.widget.moneyinput

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ohjiho.budgetify.presentation.databinding.DialogMoneyInputBinding
import dev.ohjiho.budgetify.utils.data.getLocale
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency

class MoneyInputBottomSheetDialogFragment : BottomSheetDialogFragment(), Keypad.Listener {
    private val viewModel by viewModels<MoneyInputBottomSheetDialogViewModel>()
    private lateinit var binding: DialogMoneyInputBinding
    private var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val currency = arguments?.getString(CURRENCY_ARG)?.let { Currency.getInstance(it) }
            val amount = arguments?.getString(AMOUNT_ARG)?.let { BigDecimal(it) }

            viewModel.initState(currency, amount)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogMoneyInputBinding.inflate(inflater, container, false)

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.currency.collect {
                            keypad.setCurrency(it)
                            moneyDisplay.setCurrency(it)
                        }
                    }
                    launch {
                        viewModel.amount.collect {
                            moneyDisplay.setAmount(it)
                        }
                    }
                }
            }

            keypad.setListener(this@MoneyInputBottomSheetDialogFragment)
        }

        return binding.root
    }

    override fun onKeyPressed(key: Int) {
        binding.moneyDisplay.addDigit(key)
    }

    override fun onDotPressed() {
        binding.moneyDisplay.addDecimalDot()
    }

    override fun onBackspacePressed() {
        binding.moneyDisplay.backspace()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (listener?.isMoneyInputDialogShown == false) {
            super.show(manager, tag)
            listener?.isMoneyInputDialogShown = true
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (listener?.isMoneyInputDialogShown == false) {
            super.show(transaction, tag).also {
                listener?.isMoneyInputDialogShown = true
            }
        } else {
            -1
        }
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (listener?.isMoneyInputDialogShown == false) {
            super.showNow(manager, tag)
            listener?.isMoneyInputDialogShown = true
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.apply {
            isMoneyInputDialogShown = false
            onDialogDismiss(binding.moneyDisplay.getAmount())
        }
        super.onDismiss(dialog)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.updateState(binding.moneyDisplay.getAmount())
        super.onSaveInstanceState(outState)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        var isMoneyInputDialogShown: Boolean
        fun onDialogDismiss(amount: BigDecimal)
    }

    companion object {
        const val MONEY_INPUT_BSD_TAG = "MoneyInputBottomSheetDialog"

        private const val CURRENCY_ARG = "CURRENCY"
        private const val AMOUNT_ARG = "AMOUNT"

        fun getInstance(currency: Currency = Currency.getInstance(getLocale()), amount: BigDecimal = BigDecimal.ZERO) =
            MoneyInputBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(CURRENCY_ARG, currency.currencyCode)
                    putString(AMOUNT_ARG, amount.toString())
                }
            }
    }
}