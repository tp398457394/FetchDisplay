package com.fetch.fetchdisplay.domain.mapper

import com.fetch.fetchdisplay.data.ItemDTO
import com.fetch.fetchdisplay.domain.entity.Item
import com.fetch.fetchdisplay.domain.entity.Items

fun List<ItemDTO>?.toItems(): Items {
    return Items(
        itemList = this?.map {
            Item(
                id = it.id,
                listId = it.listId,
                name = it.name.orEmpty()
            )
        }.orEmpty()
    )
}