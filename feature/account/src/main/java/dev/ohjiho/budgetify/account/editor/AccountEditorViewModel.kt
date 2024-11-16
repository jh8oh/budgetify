package dev.ohjiho.budgetify.account.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import kotlin.properties.Delegates

@HiltViewModel
internal class AccountEditorViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    currencyRepository: CurrencyRepository,
) : ViewModel() {

    var isNew by Delegates.notNull<Boolean>()
    val account: MutableStateFlow<Account> =
        MutableStateFlow(Account("", "", AccountType.CASH, BigDecimal.ZERO, currencyRepository.getDefaultCurrency()))
    val uniqueInstitution = accountRepository.getAllUniqueInstitutions()

    fun initNew() {
        isNew = true
    }

    fun initExisting(id: Int) {
        isNew = false
        viewModelScope.launch {
            val a = accountRepository.getAccount(id) ?: throw NullPointerException(NO_ACCOUNT_FOUND_ERROR + id)
            account.update { a }
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

    companion object {
        private const val NO_ACCOUNT_FOUND_ERROR = "No account found with id: "
    }
}