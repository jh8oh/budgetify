package dev.ohjiho.budgetify.finances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.finances.databinding.FragmentFinancesBinding

class FinancesFragment: Fragment() {

    private lateinit var binding: FragmentFinancesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinancesBinding.inflate(inflater)

        return binding.root
    }
}