package com.fetch.fetchdisplay.domain.entity

data class Items(
    val items: List<Item> = emptyList()
)

data class Item(
    val id: Int,
    val listId: Int,
    val name: String
)
