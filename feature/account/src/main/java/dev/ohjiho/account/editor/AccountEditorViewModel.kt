package dev.ohjiho.account.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

data class AccountEditorUiState(
    val name: String,
    val colorInt: Int,
    val balance: BigDecimal,
    val currency: Currency,
    val type: AccountType
)

@HiltViewModel
internal class AccountEditorViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {



    fun initWithAccountId(accountId: Int) {
        viewModelScope.launch {
        }
    }

    fun getDefaultCurrency() = currencyRepository.getDefaultCurrency()
}