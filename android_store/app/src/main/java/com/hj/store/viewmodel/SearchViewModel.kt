package com.hj.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.Store
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel:ViewModel() {
    private var _searchStores = MutableLiveData<List<Store>>()
    val searchStore: LiveData<List<Store>> = _searchStores

    fun searchStoreItems(storeName: String) {
        StoreApi.storeService.searchRestaurant(storeName).enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                if(response.isSuccessful){
                    _searchStores.value = response.body() ?: emptyList()
                }
            }
            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                // TODO
            }
        })
    }
}