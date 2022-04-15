package com.jin.store.controller

import com.jin.store.mapper.RestaurantMapper
import com.jin.store.model.Restaurant
import org.springframework.web.bind.annotation.*
import kotlin.math.log

@CrossOrigin
@RestController
class RestaurantController(var restaurantMapper: RestaurantMapper) {
    @GetMapping("/api/restaurants")
    fun getRestaurants() = restaurantMapper.getRestaurants()

    @GetMapping("/api/restaurants/{id}")
    fun getRestaurant(@PathVariable("id") id: Int) = restaurantMapper.getRestaurant(id)

    @PostMapping("/api/restaurants")
    fun addRestaurant(@RequestBody restaurant: Restaurant) {
        return restaurantMapper.addRestaurant(restaurant.name, restaurant.address, restaurant.imageUrl)
    }

    @PutMapping("/api/restaurants/{id}")
    fun updateRestaurant(
        @PathVariable("id") id: Int,
        @RequestBody restaurant: Restaurant
    )  {
        restaurant.id = id
        return restaurantMapper.updateRestaurant(restaurant.id, restaurant.name, restaurant.address, restaurant.imageUrl)
    }

    @DeleteMapping("/api/restaurants/{id}")
    fun removeRestaurant(@PathVariable("id") id: Int) = restaurantMapper.removeRestaurant(id)
}