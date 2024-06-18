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
import dev.ohjiho.account.recyclerview.AccountsRecyclerView
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpAccountsBinding
import kotlinx.coroutines.launch

internal class SetUpAccountsFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpAccountsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpAccountsBinding.inflate(inflater)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.accountsRecyclerView.setAccountList(it.accounts)
                }
            }
        }

        with(binding) {
            addAccountButton.setOnClickListener {
                viewModel.addOrUpdateAccount(null)
            }
            accountsRecyclerView.setListener(object : AccountsRecyclerView.Listener {
                override fun onClick(account: AccountEntity) {
                    viewModel.addOrUpdateAccount(account.uid)
                }
            })
        }

        return binding.root
    }
}