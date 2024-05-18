package dev.ohjiho.budgetify.setup.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ohjiho.budgetify.setup.SetUpViewModel
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpAccountsBinding
import dev.ohjiho.component.recyclerview.SpaceItemDecoration
import kotlinx.coroutines.launch

internal class SetUpAccountsFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpAccountsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpAccountsBinding.inflate(inflater)

        val adapter = SetUpAccountsAdapter {
            // TODO Navigate to AccountsEditor Fragment
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    adapter.setAccountList(it.accounts)
                }
            }
        }

        with(binding) {
            binding.accountsRecyclerView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(SpaceItemDecoration(verticalSpace = 4))
            }

            binding.addAccountButton.setOnClickListener {
                // TODO Navigate to AccountsEditor Fragment
            }
        }

        return binding.root
    }
}