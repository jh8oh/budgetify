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
import dev.ohjiho.budgetify.setup.databinding.FragmentSetUpIncomeBinding
import dev.ohjiho.budgetify.utils.data.toBigDecimalAfterSanitize
import dev.ohjiho.budgetify.utils.data.toCurrencyFormat
import dev.ohjiho.budgetify.utils.ui.reformatBalanceAfterTextChange
import kotlinx.coroutines.launch
import java.math.BigDecimal

internal class SetUpIncomeFragment : Fragment() {

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
                        currency.text = accountCurrency.currencyCode
                        if (it.amount != BigDecimal.ZERO) {
                            amount.setText(it.amount.toCurrencyFormat(accountCurrency, context))
                        } else {
                            amount.hint = BigDecimal.ZERO.toCurrencyFormat(accountCurrency, context)
                        }
                        amount.reformatBalanceAfterTextChange(accountCurrency)
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
            frequency.apply {
                setAdapter(frequencyAdapter)
                threshold = IMPOSSIBLE_THRESHOLD
            }
            account.apply {
                setAdapter(accountAdapter)
                threshold = IMPOSSIBLE_THRESHOLD
                setOnItemClickListener { _, _, position, _ ->
                    val accountCurrency = viewModel.accounts.value[position].currency
                    currency.text = accountCurrency.currencyCode
                    amount.reformatBalanceAfterTextChange(accountCurrency)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveIncomeState()
    }

    private fun saveIncomeState() {
        with(binding) {
            viewModel.updateIncomeState(
                incomeBudgetToggle.checkedButtonId == incomeButton.id,
                amount.text.toString().toBigDecimalAfterSanitize(),
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
                currency.setTextColor(primaryColor)
                amount.setTextColor(primaryColor)

                perLabel.visibility = View.VISIBLE
                frequencyContainer.visibility = View.VISIBLE
                intoLabel.visibility = View.VISIBLE
                accountContainer.visibility = View.VISIBLE
            } else {
                resources.getColor(R.color.orange_700, requireContext().theme).let {
                    currency.setTextColor(it)
                    amount.setTextColor(it)
                }

                perLabel.visibility = View.GONE
                frequencyContainer.visibility = View.GONE
                intoLabel.visibility = View.GONE
                accountContainer.visibility = View.GONE
            }
        }
    }

    companion object {
        private val IMPOSSIBLE_THRESHOLD = 1000
        private val FREQUENCY_LIST = listOf("week", "month")
    }
}