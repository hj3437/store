package com.jin.store.model

data class Items(
    val storeId: Int = 0,
    val storeName: String = "",
    val storeAddress: String = "",
    val storeImageUrl: String = "",
    var itemSize: Int = 0,
    var items: List<Item> = emptyList()
)