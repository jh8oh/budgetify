package dev.ohjiho.budgetify.setup

import android.os.Bundle
import android.util.TypedValue
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
import dev.ohjiho.budgetify.presentation.fragment.MoneyInputBottomSheetDialogFragment
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpIncomeBinding
import kotlinx.coroutines.launch
import java.math.BigDecimal

internal class SetUpIncomeFragment : Fragment(), MoneyInputBottomSheetDialogFragment.Listener {

    private val viewModel: SetUpViewModel by activityViewModels()
    private lateinit var binding: FragmentSetUpIncomeBinding

    // Adapters
    private val frequencyAdapter by lazy {
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, FREQUENCY_LIST)
    }
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
                    viewModel.setUpIncomeState.collect {
                        val accountCurrency = it.account!!.currency
                        incomeBudgetToggle.check(if (it.isIncome) incomeButton.id else budgetButton.id)
                        onSwitchIncomeBudgetToggle(it.isIncome)
                        moneyDisplay.apply {
                            currency = accountCurrency
                            setAmount(it.amount)
                        }
                        frequency.setText(if (it.isMonthly) FREQUENCY_LIST[1] else FREQUENCY_LIST[0], false)
                        account.setText(it.account.name, false)
                    }
                }
            }

            incomeBudgetToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    onSwitchIncomeBudgetToggle(checkedId == incomeButton.id)
                }
            }
            moneyDisplay.setOnClickListener {
                MoneyInputBottomSheetDialogFragment.getInstance(moneyDisplay.getAmount()).apply {
                    setListener(this@SetUpIncomeFragment)
                }.show(childFragmentManager, MoneyInputBottomSheetDialogFragment.MONEY_INPUT_BSD_TAG)
            }
            frequency.apply {
                setAdapter(frequencyAdapter)
                threshold = IMPOSSIBLE_THRESHOLD
            }
            account.apply {
                setAdapter(accountAdapter)
                threshold = IMPOSSIBLE_THRESHOLD
                setOnItemClickListener { _, _, position, _ ->
                    val accountCurrency = viewModel.accounts.value[position].currency
                    moneyDisplay.apply {
                        currency = accountCurrency
                    }
                }
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

    override fun onDialogDismiss(amount: BigDecimal) {
        binding.moneyDisplay.setAmount(amount)
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
                frequency.text.toString() == FREQUENCY_LIST[1],
                viewModel.accounts.value.find { it.name == account.text.toString() }
            )
        }
    }

    private fun onSwitchIncomeBudgetToggle(isIncome: Boolean) {
        with(binding) {
            if (isIncome) {
                val typedValue = TypedValue()
                requireContext().theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
                val primaryColor = typedValue.data
                moneyDisplay.setCurrencyTextColor(primaryColor)

                perLabel.visibility = View.VISIBLE
                frequencyContainer.visibility = View.VISIBLE
                intoLabel.visibility = View.VISIBLE
                accountContainer.visibility = View.VISIBLE
            } else {
                resources.getColor(R.color.orange_700, requireContext().theme).let {
                    moneyDisplay.setCurrencyTextColor(it)
                }

                perLabel.visibility = View.GONE
                frequencyContainer.visibility = View.GONE
                intoLabel.visibility = View.GONE
                accountContainer.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val IMPOSSIBLE_THRESHOLD = 1000
        private val FREQUENCY_LIST = listOf("week", "month")
    }
}