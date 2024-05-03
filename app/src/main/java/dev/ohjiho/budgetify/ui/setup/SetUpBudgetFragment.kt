package dev.ohjiho.budgetify.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.databinding.FragmentSetUpBudgetBinding

class SetUpBudgetFragment : Fragment() {

    private lateinit var binding: FragmentSetUpBudgetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetUpBudgetBinding.inflate(inflater)

        with(binding) {

        }

        return binding.root
    }
}