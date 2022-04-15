package com.hj.store.data

data class StoreDetailItem(
    val id: Int = -1,
    var name: String = "",
    var price: Int = 0,
    var storeId: Int = 0,
    var imageUrl: String = "",
    var isLogin: Boolean = false
)
