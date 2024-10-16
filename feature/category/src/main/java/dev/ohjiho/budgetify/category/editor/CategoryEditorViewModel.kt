package dev.ohjiho.budgetify.category.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.NON_EXISTENT_ID
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.ExpenseCategory
import dev.ohjiho.budgetify.domain.model.IncomeCategory
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.theme.icon.Icon
import dev.ohjiho.budgetify.utils.flow.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryEditorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    var isNew = true
    val category: MutableStateFlow<Category> = MutableStateFlow(ExpenseCategory("", Icon.HOME, true))
    private val isExpense = category.map { it is ExpenseCategory }.stateIn(viewModelScope, WhileUiSubscribed, true)

    fun initWithId(isExpense: Boolean, id: Int) {
        if (id == NON_EXISTENT_ID) return

        isNew = false
        viewModelScope.launch {
            category.update {
                if (isExpense) {
                    categoryRepository.getExpenseCategory(id)
                } else {
                    categoryRepository.getIncomeCategory(id)
                }
            }
        }
    }

    fun updateState(name: String, icon: Icon, isNeed: Boolean) {
        if (isExpense.value) {
            category.update { ExpenseCategory(name, icon, isNeed).apply { uid = it.uid } }
        } else {
            category.update { IncomeCategory(name, icon).apply { uid = it.uid } }
        }
    }

    fun saveCategory() {
        viewModelScope.launch{
            if (isNew){
                categoryRepository.insert(category.value)
            } else {
                categoryRepository.update(category.value)
            }
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            categoryRepository.delete(category.value)
        }
    }
}