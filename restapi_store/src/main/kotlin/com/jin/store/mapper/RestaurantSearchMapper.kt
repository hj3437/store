package com.jin.store.mapper

import com.jin.store.model.Item
import com.jin.store.model.Restaurant
import org.apache.ibatis.annotations.*

@Mapper
interface RestaurantSearchMapper {
    @Select("SELECT id, name, address, image_url as imageUrl FROM Restaurants WHERE name LIKE #{searchStore}")
    fun getSearchStore(searchStore: String): List<Restaurant>
}