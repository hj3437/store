package com.jin.store.controller

import com.jin.store.mapper.RestaurantSearchMapper
import com.jin.store.model.Restaurant
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class RestaurantSearchController(val restaurantSearchMapper: RestaurantSearchMapper) {
    @GetMapping("/api/restaurants/search/{store}")
    fun getRestaurantItems(@PathVariable(name = "store") store: String): List<Restaurant> {
        val searchStore = "%$store%"
        return restaurantSearchMapper.getSearchStore(searchStore)
    }
}