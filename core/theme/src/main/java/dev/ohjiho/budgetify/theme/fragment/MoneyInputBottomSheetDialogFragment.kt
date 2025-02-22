package dev.ohjiho.budgetify.theme.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ohjiho.budgetify.theme.databinding.DialogMoneyInputBinding
import java.math.BigDecimal

class MoneyInputBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: DialogMoneyInputBinding
    private var listener: Listener? = null

    interface Listener {
        fun onDialogDismiss(amount: BigDecimal)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogMoneyInputBinding.inflate(inflater, container, false)

        with(binding) {
            keypad.setMoneyDisplay(moneyDisplay)
            moneyDisplay.setAmount(arguments?.getString(AMOUNT_ARG)?.let { BigDecimal(it) } ?: BigDecimal.ZERO)
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onDialogDismiss(binding.moneyDisplay.getAmount())
        super.onDismiss(dialog)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    companion object {
        const val MONEY_INPUT_BSD_TAG = "MoneyInputBottomSheetDialog"

        private const val AMOUNT_ARG = "AMOUNT"

        fun getInstance(amount: BigDecimal = BigDecimal.ZERO) = MoneyInputBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putString(AMOUNT_ARG, amount.toString())
            }
        }
    }
}