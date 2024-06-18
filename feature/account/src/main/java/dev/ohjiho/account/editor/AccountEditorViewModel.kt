package dev.ohjiho.account.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

@HiltViewModel
internal class AccountEditorViewModel @Inject constructor(
    currencyRepository: CurrencyRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {
    val editingAccount = MutableLiveData(AccountEntity.newInstance(currencyRepository.getDefaultCurrency()))

    fun getEditingAccountFromId(id: Int) {
        viewModelScope.launch {
            if (id != 0) {
                editingAccount.value = accountRepository.getAccount(id)
            }
        }
    }

    fun updateEditingAccount(
        name: String? = null,
        colorInt: Int? = null,
        accountType: AccountType? = null,
        balance: BigDecimal? = null,
        currency: Currency? = null,
    ) {
        editingAccount.value = editingAccount.value?.let {
            AccountEntity(
                name ?: it.name,
                colorInt ?: it.colorInt,
                accountType ?: it.accountType,
                balance ?: it.balance,
                currency ?: it.currency
            )
        }
    }
}