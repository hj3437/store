package com.jin.store.mapper

import com.jin.store.model.Item
import org.apache.ibatis.annotations.*

@Mapper
interface RestaurantItemMapper {
    @Select("SELECT id, name, price, store_id as storeId, image_url as imageUrl FROM Items WHERE store_id = #{storeId}")
    fun getRestaurantItems(storeId: Int): List<Item>

    @Select("SELECT id, name, price, store_id as storeId, image_url as imageUrl FROM Items WHERE store_id = #{storeId} and id = #{itemId}")
    fun getRestaurantItem(storeId: Int, itemId: Int): Item

    @Insert("INSERT INTO Items (name, price, store_id, image_url) values(#{name}, #{price}, #{storeId}, #{imageUrl})")
    fun addRestaurantItem(storeId: Int, name: String, price: Int, imageUrl: String)

    @Update("UPDATE Items SET name = #{name}, price = #{price}, image_url = #{imageUrl} WHERE store_id = #{storeId} and id = #{itemId}")
    fun updateRestaurantItem(storeId: Int, itemId: Int, name: String, price: Int, imageUrl: String)

    @Delete("DELETE FROM Items WHERE store_id = #{storeId} and id = #{itemId}")
    fun removeRestaurantItem(storeId: Int, itemId: Int)
}