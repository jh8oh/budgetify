package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.ohjiho.budgetify.presentation.widget.moneyinput.MoneyInputBottomSheetDialogFragment
import dev.ohjiho.budgetify.presentation.widget.RepeatDisplay
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpIncomeBinding
import dev.ohjiho.budgetify.utils.ui.getColor
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal
import com.google.android.material.R as materialR
import dev.ohjiho.budgetify.theme.R as themeR

internal class SetUpIncomeFragment : Fragment() {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpIncomeBinding

    // Listener
    private val moneyInputBottomSheetDialogListener by lazy {
        object : MoneyInputBottomSheetDialogFragment.Listener {
            override fun onDialogDismiss(amount: BigDecimal) {
                binding.moneyDisplay.setAmount(amount)
            }
        }
    }

    private val repeatDisplayListener by lazy {
        object : RepeatDisplay.Listener {
            override fun showDialog(dialog: RepeatDisplay.Dialog) {
                dialog.show(childFragmentManager, RepeatDisplay.TAG)
            }
        }
    }

    // Adapters
    private val accountAdapter by lazy {
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.accounts.value.map { it.name })
    }

    // Resources
    private val setUpIncomeTitle by lazy { resources.getString(R.string.fragment_set_up_income_title) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetUpIncomeBinding.inflate(inflater)

        (requireActivity() as AppCompatActivity).title = setUpIncomeTitle

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.setUpIncomeState.combine(viewModel.accounts) { state, accounts -> Pair(state, accounts) }.collect { flow ->
                        val state = flow.first
                        val accounts = flow.second

                        // Return to previous screen if accounts is somehow empty
                        if (accounts.isEmpty()){
                            viewModel.onBackPressed()
                        }

                        val account = accounts.find { it.uid == state.accountId } ?: accounts[0]
                        incomeBudgetToggle.check(if (state.isIncome) incomeButton.id else budgetButton.id)
                        onSwitchIncomeBudgetToggle(state.isIncome)
                        moneyDisplay.apply {
                            setCurrency(account.currency)
                            setAmount(state.amount)
                        }
                        repeatDisplay.reoccurrence = state.reoccurrence
                        accountDisplay.selectedAccount = account
                        accountDisplay.accounts = accounts
                    }
                }
            }

            repeatDisplay.allowNeverRepeat = false
            repeatDisplay.setListener(repeatDisplayListener)

            incomeBudgetToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    onSwitchIncomeBudgetToggle(checkedId == incomeButton.id)
                }
            }
            moneyDisplay.setOnClickListener {
                MoneyInputBottomSheetDialogFragment.getInstance(moneyDisplay.getCurrency(), moneyDisplay.getAmount()).apply {
                    setListener(moneyInputBottomSheetDialogListener)
                }.show(childFragmentManager, MoneyInputBottomSheetDialogFragment.MONEY_INPUT_BSD_TAG)
            }
            backButton.setOnClickListener {
                saveIncomeState()
                viewModel.onBackPressed()
            }
            nextButton.setOnClickListener {
                saveIncomeState()
                viewModel.nextScreen()
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveIncomeState()
    }

    private fun saveIncomeState() {
        with(binding) {
            viewModel.updateIncomeState(
                incomeBudgetToggle.checkedButtonId == incomeButton.id,
                moneyDisplay.getAmount(),
                repeatDisplay.reoccurrence,
                accountDisplay.selectedAccount
            )
        }
    }

    private fun onSwitchIncomeBudgetToggle(isIncome: Boolean) {
        with(binding) {
            if (isIncome) {
                val primaryColor =
                    requireContext().getColor(materialR.attr.colorPrimary, themeR.color.teal_500)
                moneyDisplay.setCurrencyTextColor(primaryColor)

                repeatDisplay.visibility = View.VISIBLE
                accountDisplay.visibility = View.VISIBLE
            } else {
                resources.getColor(R.color.orange_700, requireContext().theme).let {
                    moneyDisplay.setCurrencyTextColor(it)
                }

                repeatDisplay.visibility = View.GONE
                accountDisplay.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val IMPOSSIBLE_THRESHOLD = 1000
    }
}