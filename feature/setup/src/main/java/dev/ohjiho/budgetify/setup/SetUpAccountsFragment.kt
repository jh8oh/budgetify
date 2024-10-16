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
import dev.ohjiho.budgetify.account.recyclerview.AccountsRecyclerView
import dev.ohjiho.budgetify.domain.NON_EXISTENT_ID
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpAccountsBinding
import kotlinx.coroutines.launch

internal class SetUpAccountsFragment : Fragment(){

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpAccountsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpAccountsBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accounts.collect {
                    binding.accountsRecyclerView.setAccountList(it)
                }
            }
        }

        with(binding) {
            addAccountButton.setOnClickListener {
                viewModel.addOrUpdateAccount(NON_EXISTENT_ID)
            }
            accountsRecyclerView.setListener(object : AccountsRecyclerView.Listener {
                override fun onClick(account: Account) {
                    viewModel.addOrUpdateAccount(account.uid)
                }
            })
            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener { viewModel.nextScreen() }
        }

        return binding.root
    }
}