package dev.ohjiho.budgetify.category.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.TransactionType
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.icons.Icon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
internal class CategoryEditorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    var isNew by Delegates.notNull<Boolean>()
    val category: MutableStateFlow<Category> = MutableStateFlow(Category("", TransactionType.EXPENSE, Icon.HOME, true))

    fun initNew(type: TransactionType) {
        isNew = true
        category.update { it.copy(type = type) }
    }

    fun initExisting(id: Int) {
        isNew = false
        viewModelScope.launch {
            val c = categoryRepository.getCategory(id) ?: throw NullPointerException(NO_CATEGORY_FOUND_ERROR)
            category.update { c }
        }
    }

    fun updateIconState(icon: Icon) {
        category.update {
            it.copy(icon = icon).apply { uid = it.uid }
        }
    }

    fun updateState(name: String, isNeed: Boolean) {
        category.update {
            it.copy(name = name, isNeed = if (it.type == TransactionType.EXPENSE) isNeed else null).apply { uid = it.uid }
        }
    }

    fun saveCategory() {
        viewModelScope.launch {
            if (isNew) {
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

    companion object {
        private const val NO_CATEGORY_FOUND_ERROR = "No category found with id: "
    }
}