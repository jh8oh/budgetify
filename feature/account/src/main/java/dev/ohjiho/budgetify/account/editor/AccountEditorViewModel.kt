package dev.ohjiho.budgetify.account.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.Account
import dev.ohjiho.budgetify.domain.model.AccountType
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import dev.ohjiho.budgetify.utils.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
internal class AccountEditorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    currencyRepository: CurrencyRepository,
) : ViewModel() {

    var isNew by Delegates.notNull<Boolean>()
    val account = combine(
        savedStateHandle.getStateFlow(UID_SAVED_STATE_KEY, 0),
        savedStateHandle.getStateFlow(NAME_SAVED_STATE_KEY, ""),
        savedStateHandle.getStateFlow(INSTITUTION_SAVED_STATE_KEY, ""),
        savedStateHandle.getStateFlow(ACCOUNT_TYPE_SAVED_STATE_KEY, AccountType.CASH),
        savedStateHandle.getStateFlow(BALANCE_SAVED_STATE_KEY, "0"),
        savedStateHandle.getStateFlow(CURRENCY_SAVED_STATE_KEY, currencyRepository.getDefaultCurrency().currencyCode),
        viewModelScope
    ) { uid, name, institution, accountType, balance, currencyCode ->
        Account(uid, name, institution, accountType, BigDecimal(balance), Currency.getInstance(currencyCode))
    }
    val uniqueInstitution = accountRepository.getAllUniqueInstitutions()

    fun initNew() {
        isNew = true
    }

    fun initExisting(id: Int) {
        isNew = false
        viewModelScope.launch {
            val a = accountRepository.getAccount(id) ?: throw NullPointerException(NO_ACCOUNT_FOUND_ERROR + id)
            savedStateHandle[UID_SAVED_STATE_KEY] = a.uid
            savedStateHandle[NAME_SAVED_STATE_KEY] = a.name
            savedStateHandle[INSTITUTION_SAVED_STATE_KEY] = a.institution
            savedStateHandle[ACCOUNT_TYPE_SAVED_STATE_KEY] = a.type
            savedStateHandle[BALANCE_SAVED_STATE_KEY] = a.balance.toString()
            savedStateHandle[CURRENCY_SAVED_STATE_KEY] = a.currency.currencyCode
        }
    }

    fun updateState(name: String, institution: String, type: AccountType, balance: BigDecimal, currency: Currency) {
        savedStateHandle[UID_SAVED_STATE_KEY] = account.value.uid
        savedStateHandle[NAME_SAVED_STATE_KEY] = name
        savedStateHandle[INSTITUTION_SAVED_STATE_KEY] = institution
        savedStateHandle[ACCOUNT_TYPE_SAVED_STATE_KEY] = type
        savedStateHandle[BALANCE_SAVED_STATE_KEY] = balance.toString()
        savedStateHandle[CURRENCY_SAVED_STATE_KEY] = currency.currencyCode
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
        private const val UID_SAVED_STATE_KEY = "UIDSavedState"
        private const val NAME_SAVED_STATE_KEY = "NameSavedState"
        private const val INSTITUTION_SAVED_STATE_KEY = "InstitutionSavedState"
        private const val ACCOUNT_TYPE_SAVED_STATE_KEY = "AccountTypeSavedState"
        private const val BALANCE_SAVED_STATE_KEY = "BalanceSavedState"
        private const val CURRENCY_SAVED_STATE_KEY = "CurrencySavedState"

        private const val NO_ACCOUNT_FOUND_ERROR = "No account found with id: "
    }
}