package dev.ohjiho.budgetify.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.databinding.FragmentSetUpIncomeBinding

class SetUpIncomeFragment : Fragment() {
    private lateinit var binding: FragmentSetUpIncomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpIncomeBinding.inflate(inflater)

        return binding.root
    }
}