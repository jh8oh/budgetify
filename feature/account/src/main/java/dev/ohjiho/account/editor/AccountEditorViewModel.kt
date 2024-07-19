package dev.ohjiho.account.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.AccountEntity
import dev.ohjiho.budgetify.domain.repository.AccountRepository
import dev.ohjiho.budgetify.domain.repository.CurrencyRepository
import javax.inject.Inject

@HiltViewModel
internal class AccountEditorViewModel @Inject constructor(
    currencyRepository: CurrencyRepository,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val account = MutableLiveData<AccountEntity?>(null)

    // UI Fields

    fun setAccount(id: Int){
        
    }

    fun initFields(){

    }
}