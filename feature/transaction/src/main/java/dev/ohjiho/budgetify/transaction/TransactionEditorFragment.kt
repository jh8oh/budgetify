package dev.ohjiho.budgetify.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.ohjiho.budgetify.domain.model.CategoryType
import dev.ohjiho.budgetify.transaction.databinding.FragmentTransactionEditorBinding

@AndroidEntryPoint
class TransactionEditorFragment: Fragment() {

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

        return binding.root
    }

    companion object{
        private const val CATEGORY_TYPE = "CATEGORY_TYPE"

        fun newInstance(categoryType: CategoryType) = TransactionEditorFragment().apply {
            arguments = Bundle().apply {
                putInt(CATEGORY_TYPE, categoryType.ordinal)
            }
        }
    }
}