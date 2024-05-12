package dev.ohjiho.budgetify.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ohjiho.budgetify.R
import dev.ohjiho.budgetify.databinding.FragmentSetUpAccountsBinding
import dev.ohjiho.budgetify.ui.account.AccountsAdapter
import kotlinx.coroutines.launch

class SetUpAccountsFragment : Fragment() {

    private lateinit var binding: FragmentSetUpAccountsBinding
    private val viewModel by activityViewModels<SetUpViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpAccountsBinding.inflate(inflater)

        val adapter = AccountsAdapter {
            viewModel.toAccountEditorScreen(it)
            findNavController().navigate(R.id.action_nav_fragment_set_up_accounts_to_nav_fragment_set_up_account_editor)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    adapter.setAccountList(uiState.accounts)
                }
            }
        }

        with(binding) {
            binding.accountsRecyclerView.apply {
                this.adapter = adapter
                this.layoutManager = LinearLayoutManager(context)
            }

            binding.addAccountButton.setOnClickListener {
                viewModel.toAccountEditorScreen(null)
                findNavController().navigate(R.id.action_nav_fragment_set_up_accounts_to_nav_fragment_set_up_account_editor)
            }
        }

        return binding.root
    }
}