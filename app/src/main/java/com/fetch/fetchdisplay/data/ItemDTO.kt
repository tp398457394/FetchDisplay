package com.fetch.fetchdisplay.data


data class ItemsDTO(
    val itemsDto: List<ItemDTO>?
)

data class ItemDTO(
    val id: Int,
    val listId: Int,
    val name: String
)
