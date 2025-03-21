package com.fetch.fetchdisplay.domain.repository

import com.fetch.fetchdisplay.domain.entity.Items
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems(): Flow<Items>
}