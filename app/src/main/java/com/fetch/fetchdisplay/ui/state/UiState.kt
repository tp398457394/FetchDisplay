package com.fetch.fetchdisplay.ui.state

import com.fetch.fetchdisplay.domain.entity.Item

data class UiState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val groupedItems: Map<Int, List<Item>> = emptyMap<Int, List<Item>>()
)
