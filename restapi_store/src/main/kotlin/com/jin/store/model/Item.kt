package com.jin.store.model

data class Item(
    var id: Int = 0,
    val name: String = "",
    val price: Int = 0,
    var storeId: Int = 0,
    val imageUrl: String = ""
)