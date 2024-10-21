package dev.ohjiho.budgetify.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.overview.databinding.FragmentOverviewBinding

class OverviewFragment: Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOverviewBinding.inflate(inflater)

        return binding.root
    }
}