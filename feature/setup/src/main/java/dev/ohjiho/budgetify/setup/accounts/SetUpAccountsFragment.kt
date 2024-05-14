package dev.ohjiho.budgetify.setup.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpAccountsBinding

internal class SetUpAccountsFragment : Fragment() {

    private lateinit var binding: FragmentSetUpAccountsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpAccountsBinding.inflate(inflater)

        with (binding){

        }

        return binding.root
    }
}