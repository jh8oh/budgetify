package dev.ohjiho.budgetify.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.theme.widget.Keypad
import dev.ohjiho.budgetify.transaction.databinding.FragmentTransactionEditorBinding

@AndroidEntryPoint
class TransactionEditorFragment : Fragment(), Keypad.Listener {

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
            binding.keypad.setListener(this@TransactionEditorFragment)
        }

        return binding.root
    }

    override fun onKeyPressed(key: Int) {
        binding.display.addDigit(key)
    }

    override fun onDotPressed() {
        binding.display.addDecimalDot()
    }

    override fun onBackspacePressed() {
        binding.display.backspace()
    }

    companion object {
        private const val TRANSACTION_TYPE = "TRANSACTION_TYPE"

        fun bundle(transactionType: TransactionType) = Bundle().apply {
            putInt(TRANSACTION_TYPE, transactionType.ordinal)
        }
    }
}