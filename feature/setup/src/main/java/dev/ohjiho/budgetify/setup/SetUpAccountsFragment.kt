package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.account.recyclerview.AccountsRecyclerView
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpAccountsBinding
import kotlinx.coroutines.launch

internal class SetUpAccountsFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpAccountsBinding

    // Resources
    private val setUpAccountsTitle by lazy { resources.getString(R.string.fragment_set_up_accounts_title) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpAccountsBinding.inflate(inflater)

        (requireActivity() as AppCompatActivity).apply {
            title = setUpAccountsTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.accounts.collect {
                        accountsRecyclerView.setAccountList(it)
                    }
                }
            }

            accountsRecyclerView.setListener(object : AccountsRecyclerView.Listener {
                override fun onClick(account: Account) {
                    viewModel.updateAccount(account.uid)
                }
            })
            addAccountButton.setOnClickListener {
                viewModel.addAccount()
            }
            backButton.setOnClickListener { viewModel.onBackPressed() }
            nextButton.setOnClickListener { viewModel.nextScreen() }
        }

        return binding.root
    }
}