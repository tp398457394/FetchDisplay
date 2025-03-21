package com.fetch.fetchdisplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.fetchdisplay.domain.repository.ItemRepository
import com.fetch.fetchdisplay.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    val uiState = MutableStateFlow(UiState())

    fun getItems() = viewModelScope.launch {
        itemRepository.getAllItems()
            .catch {
                uiState.update { curUiState ->
                    curUiState.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
            }
            .collectLatest { items ->
                uiState.update { curUiState ->
                    curUiState.copy(
                        isLoading = false,
                        items = items.itemList
                    )
                }
            }
    }
}