package dev.ohjiho.account.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
internal class AccountEditorViewModel @Inject constructor(
    currencyRepository: CurrencyRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    val accountEntity: MutableStateFlow<AccountEntity?> = MutableStateFlow(null)
    val accountCurrency = MutableStateFlow(currencyRepository.getDefaultCurrency())
    val uniqueInstitution = accountRepository.getAllUniqueInstitutions()

    fun initWithAccountId(accountId: Int) {
        viewModelScope.launch {
            accountRepository.getAccount(accountId).let { account ->
                accountEntity.update { account }
                accountCurrency.update { account.currency }
            }
        }
    }

    fun updateCurrency(currency: Currency) {
        accountCurrency.update { currency }
    }

    fun saveAccount(accountName: String, accountInstitution: String, accountType: AccountType, accountBalance: BigDecimal) {
        viewModelScope.launch {
            accountEntity.value?.let {
                accountRepository.update(
                    it.apply {
                        this.name = accountName
                        this.institution = accountInstitution
                        this.accountType = accountType
                        this.balance = accountBalance
                        this.currency = accountCurrency.value
                    }
                )
            } ?: run {
                accountRepository.insert(
                    AccountEntity(
                        accountName,
                        accountInstitution,
                        accountType,
                        accountBalance,
                        accountCurrency.value
                    )
                )
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            accountEntity.value?.let {
                accountRepository.delete(it)
            }
        }
    }
}