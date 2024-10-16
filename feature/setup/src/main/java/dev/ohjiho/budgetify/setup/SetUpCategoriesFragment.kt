package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.category.recyclerview.CategoryRecyclerView
import dev.ohjiho.budgetify.domain.NON_EXISTENT_ID
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpCategoriesBinding
import kotlinx.coroutines.launch

class SetUpCategoriesFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpCategoriesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpCategoriesBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseCategories.collect {
                    binding.categoryRecyclerView.setExpenseCategories(it)
                }
            }
        }

        with(binding) {
            categoryRecyclerView.setListener(object : CategoryRecyclerView.Listener {
                override fun onClick(category: Category) {
                    viewModel.addOrUpdateCategory(category.uid)
                }
            })

            addCategoryButton.setOnClickListener {
                viewModel.addOrUpdateCategory(NON_EXISTENT_ID)
            }

            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener { viewModel.nextScreen() }
        }

        return binding.root
    }
}