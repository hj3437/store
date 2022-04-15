package com.hj.store.data

data class StoreListWithLogin(
    var id: Int = -1,
    var name: String = "",
    var address: String = "",
    var imageUrl: String = "",
    var isLogin: Boolean = false
)