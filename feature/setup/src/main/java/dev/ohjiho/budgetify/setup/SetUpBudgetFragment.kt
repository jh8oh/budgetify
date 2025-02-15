package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpBudgetBinding

internal class SetUpBudgetFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpBudgetBinding

    // Resources
    private val setUpBudgetTitle by lazy { resources.getString(R.string.fragment_set_up_budget_title) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpBudgetBinding.inflate(inflater)

        (requireActivity() as AppCompatActivity).title = setUpBudgetTitle

        with(binding) {
            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener {
                // Go back to main activity
            }
        }

        return binding.root
    }
}