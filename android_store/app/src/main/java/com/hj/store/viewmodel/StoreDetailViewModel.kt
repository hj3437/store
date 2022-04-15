package com.hj.store.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hj.store.data.StoreDetail
import com.hj.store.data.StoreDetailEditItem
import com.hj.store.data.StoreListWithLogin
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreDetailViewModel : ViewModel() {
    private var _store = MutableLiveData<StoreListWithLogin>()
    val store: LiveData<StoreListWithLogin> get() = _store

    private var _storeDetail = MutableLiveData<StoreDetail>()
    val storeDetail: LiveData<StoreDetail> get() = _storeDetail

    private var _storeItemRemove = MutableLiveData<Boolean?>(false)
    val storeItemRemove: LiveData<Boolean?> = _storeItemRemove

    private var _storeItemEdit = MutableLiveData<Boolean?>(false)
    val storeItemEdit: LiveData<Boolean?> = _storeItemEdit

    private var _storeItemAdd = MutableLiveData<Boolean?>(false)
    val storeItemAdd: LiveData<Boolean?> = _storeItemAdd

    fun setStore(storeInfo: StoreListWithLogin) {
        _store.value = storeInfo
        getStoreItems(storeInfo.id)
    }

    private fun getStoreItems(storeId: Int) {
        StoreApi.storeService.getRestaurantItems(storeId).enqueue(object : Callback<StoreDetail> {
            override fun onResponse(call: Call<StoreDetail>, response: Response<StoreDetail>) {
                if (response.isSuccessful) {
                    _storeDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<StoreDetail>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun deleteItem(storeId: Int, itemId: Int) {
        StoreApi.storeService.deleteItem(storeId, itemId).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                Log.d("아이템", "Delete Item onResponse: $response")
                if(response.isSuccessful && response.code() == 200){
                    _storeItemRemove.value = true
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("아이템", " Delete ItemonFailure: $t")
            }
        })
    }

    fun editItem(storeId: Int, itemId: Int, storeItem:StoreDetailEditItem){
        StoreApi.storeService.editItem(storeId, itemId, storeItem).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                Log.d("아이템", " 편집 Item onResponse: $response")
                if(response.isSuccessful && response.code() == 200){
                    _storeItemEdit.value = true
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("아이템", " 편집 Item onFailure: $t")
            }
        })
    }

    fun addItem(storeId: Int, storeItem: StoreDetailEditItem) {
        StoreApi.storeService.addItem(storeId, storeItem).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                Log.d("아이템", " 추가 Item onResponse: $response")
                if(response.isSuccessful && response.code() == 200){
                    _storeItemAdd.value = true
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                Log.d("아이템", " 추가 Item onFailure: $t")
            }
        })
    }

    fun resetStoreItemRemoveState(){
        _storeItemRemove.value = null
    }

    fun resetStoreItemEditState(){
        _storeItemEdit.value = null
    }

    fun resetStoreItemAddState(){
        _storeItemAdd.value = null
    }
}