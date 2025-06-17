package dev.ohjiho.budgetify.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.presentation.widget.moneyinput.MoneyInputBottomSheetDialogFragment
import dev.ohjiho.budgetify.transaction.databinding.FragmentTransactionEditorBinding
import java.math.BigDecimal

@AndroidEntryPoint
class TransactionEditorFragment : Fragment() {

    private lateinit var binding: FragmentTransactionEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.getInt(TRANSACTION_TYPE)?.let {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransactionEditorBinding.inflate(inflater)

        with(binding) {
            display.setOnClickListener {
                MoneyInputBottomSheetDialogFragment.getInstance(display.getCurrency(), display.getAmount()).apply {
                    setListener(object : MoneyInputBottomSheetDialogFragment.Listener {
                        override fun onDialogDismiss(amount: BigDecimal) {
                            display.setAmount(amount)
                        }
                    })
                }.show(childFragmentManager, MoneyInputBottomSheetDialogFragment.MONEY_INPUT_BSD_TAG)
            }
        }

        return binding.root
    }

    companion object {
        private const val TRANSACTION_TYPE = "TRANSACTION_TYPE"

        fun bundle(transactionType: TransactionType) = Bundle().apply {
            putInt(TRANSACTION_TYPE, transactionType.ordinal)
        }
    }
}