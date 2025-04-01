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
import dev.ohjiho.budgetify.category.recyclerview.CategoryRecyclerView
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpBudgetsBinding
import kotlinx.coroutines.launch

class SetUpBudgetsFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpBudgetsBinding

    // Resources
    private val setUpCategoriesTitle by lazy { resources.getString(R.string.fragment_set_up_budgets_title) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpBudgetsBinding.inflate(inflater)

        (requireActivity() as AppCompatActivity).apply {
            title = setUpCategoriesTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.expenseCategories.collect {
                        categoryRecyclerView.setCategories(true, it)

                        if (it.size >= 3) {
                            budgetBar.setSegmentCategories(it)
                            budgetBar.setSegmentAmount(it[0].uid, 12f)
                            budgetBar.setSegmentAmount(it[1].uid, 6f)
                            budgetBar.setSegmentAmount(it[2].uid, 2f)
                        }
                    }
                }
            }

            // Budget Bar testing
            budgetBar.currency = viewModel.defaultCurrency
            budgetBar.maxProgress = viewModel.setUpIncomeState.value.amount.toFloat()

            categoryRecyclerView.setListener(object : CategoryRecyclerView.Listener {
                override fun onClick(category: Category) {
                    viewModel.updateCategory(category.uid)
                }
            })

            addCategoryButton.setOnClickListener {
                viewModel.addCategory()
            }

            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener {
                // TODO Return to main activity
            }
        }

        return binding.root
    }
}