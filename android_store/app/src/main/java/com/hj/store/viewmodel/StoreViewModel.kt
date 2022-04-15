package com.hj.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.Store
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel : ViewModel() {

    private var _stores = MutableLiveData<List<Store>>()
    val store: LiveData<List<Store>> = _stores

    private var _storeRemove = MutableLiveData<Boolean?>(false)
    val storeRemove: LiveData<Boolean?> = _storeRemove

    init {
        getStores()
    }

    fun getStores() {
        StoreApi.storeService.getRestaurants().enqueue(object : Callback<List<Store>> {
            override fun onResponse(
                call: Call<List<Store>>,
                response: Response<List<Store>>
            ) {
                if (response.isSuccessful) {
                    _stores.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                // TODO
            }
        })
    }

    //
    fun deleteStore(storeId: Int) {
        StoreApi.storeService.deleteRestaurant(storeId).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (response.isSuccessful) {
                    _storeRemove.value = true
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                // Log.d("삭제실패", "onResponse: $t")
            }
        })
    }

    fun resetStoreStatus() {
        _storeRemove.value = null
    }
}