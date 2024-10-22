package dev.ohjiho.budgetify.category.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ohjiho.budgetify.domain.NON_EXISTENT_ID
import dev.ohjiho.budgetify.domain.model.Category
import dev.ohjiho.budgetify.domain.model.CategoryType
import dev.ohjiho.budgetify.domain.repository.CategoryRepository
import dev.ohjiho.budgetify.theme.icon.Icon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CategoryEditorViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    var isNew = true
    val category: MutableStateFlow<Category> = MutableStateFlow(Category("", CategoryType.EXPENSE, Icon.HOME, true))

    fun initWithId(id: Int) {
        if (id == NON_EXISTENT_ID) return

        isNew = false
        viewModelScope.launch {
            category.update { categoryRepository.getCategory(id) }
        }
    }

    fun updateIconState(icon: Icon) {
        category.update {
            it.copy(icon = icon).apply { uid = it.uid }
        }
    }

    fun updateState(name: String, isNeed: Boolean) {
        category.update {
            it.copy(name = name, isNeed = if (it.type == CategoryType.EXPENSE) isNeed else null).apply { uid = it.uid }
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
}