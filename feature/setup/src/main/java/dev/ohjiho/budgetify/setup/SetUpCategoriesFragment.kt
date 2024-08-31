package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpCategoriesBinding

class SetUpCategoriesFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpCategoriesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpCategoriesBinding.inflate(inflater)

        with(binding) {
            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener { viewModel.nextScreen() }
        }

        return binding.root
    }
}