package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpBudgetBinding
import kotlinx.coroutines.launch

internal class SetUpBudgetFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpBudgetBinding

    // Resources
    private val setUpBudgetTitle by lazy { resources.getString(R.string.fragment_set_up_budget_title) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpBudgetBinding.inflate(inflater)

        (requireActivity() as AppCompatActivity).title = setUpBudgetTitle

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    budgetBar.currency = viewModel.defaultCurrency
                    budgetBar.maxProgress = viewModel.setUpIncomeState.value.amount.toFloat()
                    budgetBar.setSegmentCategories(viewModel.expenseCategories.value)
                    budgetBar.setSegmentAmount(viewModel.expenseCategories.value.get(0).uid, 12f)
                    budgetBar.setSegmentAmount(viewModel.expenseCategories.value.get(1).uid, 6f)
                    budgetBar.setSegmentAmount(viewModel.expenseCategories.value.get(2).uid, 2f)
                }
            }

            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener {
                // Go back to main activity
            }
        }

        return binding.root
    }
}