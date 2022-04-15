package com.hj.store.remote

import com.hj.store.data.Store
import com.hj.store.data.StoreDetail
import com.hj.store.data.StoreDetailEditItem
import com.hj.store.data.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


private const val BASE_URL = "https://dosorme.ga/api/"

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface StoreService {
    @Headers("accept: application/json", "content-type: application/json")

    // 모든 스토어 데이터 호출
    @GET("restaurants/")
    fun getRestaurants(): Call<List<Store>>

    // 특정 스토어 상세화면 데이터 호출
    @GET("restaurants/{store}/items")
    fun getRestaurantItems(@Path("store") store: Int): Call<StoreDetail>

    // 스토어 검색
    @GET("restaurants/search/{storeName}")
    fun searchRestaurant(@Path("storeName") storeName: String): Call<List<Store>>

    @DELETE("restaurants/{storeId}")
    fun deleteRestaurant(@Path("storeId") storeId: Int): Call<Void>

    @PUT("restaurants/{storeId}/")
    fun editStore(
        @Path("storeId") storeId: Int,
        @Body store: Store
    ): Call<Void>

    @POST("restaurants/")
    fun addStore(@Body store: Store): Call<Void>

    @DELETE("restaurants/{storeId}/items/{itemId}")
    fun deleteItem(
        @Path("storeId") storeId: Int,
        @Path("itemId") itemId: Int
    ): Call<Void>

    // 스토어 아이템 편집
    @PUT("restaurants/{storeId}/items/{itemId}")
    fun editItem(
        @Path("storeId") storeId: Int,
        @Path("itemId") itemId: Int,
        @Body storeItem: StoreDetailEditItem
    ): Call<Void>

    @POST("restaurants/{storeId}/items/")
    fun addItem(
        @Path("storeId") storeId: Int,
        @Body storeItem: StoreDetailEditItem
    ):Call<Void>
}

interface StoreLoginService {
    @Headers("accept: application/json", "content-type: application/json")

    // https://dosorme.ga/api/user/
    @POST("user/")
    fun loginUser(
        @Body user: User,
    ): Call<Boolean>
}

object StoreApi {
    val storeService: StoreService by lazy {
        retrofit.create(StoreService::class.java)
    }

    val storeLoginService: StoreLoginService by lazy {
        retrofit.create(StoreLoginService::class.java)
    }
}