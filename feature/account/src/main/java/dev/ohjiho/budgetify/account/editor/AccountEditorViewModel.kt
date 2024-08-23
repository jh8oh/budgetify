package dev.ohjiho.budgetify.account.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
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

    var isNewAccount = MutableStateFlow(true)
    val editorAccount: MutableStateFlow<AccountEntity> =
        MutableStateFlow(AccountEntity("", "", AccountType.CASH, BigDecimal.ZERO, currencyRepository.getDefaultCurrency()))
    val uniqueInstitution = accountRepository.getAllUniqueInstitutions()

    fun initWithAccountId(accountId: Int) {
        isNewAccount.update { false }
        viewModelScope.launch {
            editorAccount.update { accountRepository.getAccount(accountId) }
        }
    }

    fun updateEditorAccount(name: String, institution: String, type: AccountType, balance: BigDecimal, currency: Currency) {
        editorAccount.getAndUpdate {
            it.copy(name, institution, type, balance, currency).apply {
                uid = it.uid
            }
        }
    }

    fun saveAccount() {
        viewModelScope.launch {
            if (isNewAccount.value) {
                accountRepository.insert(editorAccount.value)
            } else {
                accountRepository.update(editorAccount.value)
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            accountRepository.delete(editorAccount.value)
        }
    }
}