package com.fetch.fetchdisplay.data

import com.fetch.fetchdisplay.domain.entity.Items
import com.fetch.fetchdisplay.domain.mapper.toItems
import com.fetch.fetchdisplay.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DefaultItemsRepository(
    private val itemsApi: ItemsApi
): ItemRepository {
    override suspend fun getAllItems(): Flow<Items> = flowOf(
        itemsApi.getAllItems().toItems()
    )
}