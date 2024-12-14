package dev.ohjiho.budgetify.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.domain.model.CategoryType
import dev.ohjiho.budgetify.theme.component.keypad.Keypad
import dev.ohjiho.budgetify.transaction.databinding.FragmentTransactionEditorBinding

@AndroidEntryPoint
class TransactionEditorFragment : Fragment(), Keypad.Listener {

    private lateinit var binding: FragmentTransactionEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.getInt(CATEGORY_TYPE)?.let {

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
        binding.text.append(key.toString())
    }

    override fun onDotPressed() {
        binding.text.append(".")
    }

    override fun onBackspacePressed() {
        binding.text.text = binding.text.text.let { if (it.isNotEmpty()) it.substring(0, it.length - 1) else it }
    }

    companion object {
        private const val CATEGORY_TYPE = "CATEGORY_TYPE"

        fun bundle(categoryType: CategoryType) = Bundle().apply {
            putInt(CATEGORY_TYPE, categoryType.ordinal)
        }
    }
}