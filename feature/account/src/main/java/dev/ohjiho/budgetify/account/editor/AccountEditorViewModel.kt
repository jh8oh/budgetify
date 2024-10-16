package dev.ohjiho.budgetify.account.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.NON_EXISTENT_ID
import dev.ohjiho.budgetify.domain.model.Account
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

    var isNew = true
    val account: MutableStateFlow<Account> =
        MutableStateFlow(Account("", "", AccountType.CASH, BigDecimal.ZERO, currencyRepository.getDefaultCurrency()))
    val uniqueInstitution = accountRepository.getAllUniqueInstitutions()

    fun initWithId(id: Int) {
        if (id == NON_EXISTENT_ID) return

        isNew = false
        viewModelScope.launch {
            account.update { accountRepository.getAccount(id) }
        }
    }

    fun updateState(name: String, institution: String, type: AccountType, balance: BigDecimal, currency: Currency) {
        account.update {
            Account(name, institution, type, balance, currency).apply {
                uid = it.uid
            }
        }
    }

    fun saveAccount() {
        viewModelScope.launch {
            if (isNew) {
                accountRepository.insert(account.value)
            } else {
                accountRepository.update(account.value)
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            accountRepository.delete(account.value)
        }
    }
}