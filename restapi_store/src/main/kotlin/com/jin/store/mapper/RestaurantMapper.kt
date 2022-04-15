package com.jin.store.mapper

import com.jin.store.model.Restaurant
import org.apache.ibatis.annotations.*

@Mapper
interface RestaurantMapper {
    @Select("SELECT id, name, address, image_url as imageUrl FROM Restaurants")
    fun getRestaurants(): List<Restaurant>

    @Select("SELECT id, name, address, image_url as imageUrl FROM Restaurants WHERE id = #{id}")
    fun getRestaurant(id: Int): Restaurant

    @Insert("INSERT INTO Restaurants (name, address, image_url) values(#{name}, #{address}, #{imageUrl})")
    fun addRestaurant(name: String, address: String, imageUrl: String)

    @Update("UPDATE Restaurants SET name = #{name}, address = #{address}, image_url = #{imageUrl} WHERE id = #{id}")
    fun updateRestaurant(id: Int, name: String, address: String, imageUrl: String)

    @Delete("DELETE FROM Restaurants WHERE id = #{id}")
    fun removeRestaurant(id: Int)
}