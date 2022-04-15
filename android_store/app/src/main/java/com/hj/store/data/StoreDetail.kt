package com.hj.store.data

data class StoreDetail(
    val storeId: Int = -1,
    var storeName: String = "",
    var storeAddress: String = "",
    var storeImageUrl: String = "",
    var itemSize: String = "",
    var items: List<StoreDetailItem> = emptyList()
)