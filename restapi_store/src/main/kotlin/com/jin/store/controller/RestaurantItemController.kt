package com.jin.store.controller

import com.jin.store.mapper.RestaurantItemMapper
import com.jin.store.mapper.RestaurantMapper
import com.jin.store.model.Item
import com.jin.store.model.Items
import com.jin.store.model.Restaurant
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class RestaurantItemController(
    var restaurantMapper: RestaurantMapper,
    val restaurantItemMapper: RestaurantItemMapper
) {
    @GetMapping("/api/restaurants/{storeId}/items")
    fun getRestaurantItems(@PathVariable("storeId") storeId: Int): Items {
        val restaurantItems: List<Item> = restaurantItemMapper.getRestaurantItems(storeId)
        val restaurant: Restaurant = restaurantMapper.getRestaurant(storeId)

        return Items(
            storeId = storeId,
            storeName = restaurant.name,
            storeAddress = restaurant.address,
            storeImageUrl = restaurant.imageUrl,
            itemSize = restaurantItems.size,
            items = restaurantItems
        )
    }

    @GetMapping("/api/restaurants/{storeId}/items/{itemId}")
    fun getRestaurantItem(
        @PathVariable("storeId") storeId: Int,
        @PathVariable("itemId") itemId: Int
    ) = restaurantItemMapper.getRestaurantItem(storeId, itemId)

    @PostMapping("/api/restaurants/{storeId}/items")
    fun addRestaurantItem(
        @PathVariable("storeId") storeId: Int,
        @RequestBody item: Item
    ) {
        item.storeId = storeId
        return restaurantItemMapper.addRestaurantItem(item.storeId, item.name, item.price, item.imageUrl)
    }

    @PutMapping("/api/restaurants/{storeId}/items/{itemId}")
    fun updateRestaurant(
        @PathVariable("storeId") storeId: Int,
        @PathVariable("itemId") itemId: Int,
        @RequestBody item: Item
    ) {
        item.storeId = storeId
        item.id = itemId
        return restaurantItemMapper.updateRestaurantItem(item.storeId, item.id, item.name, item.price, item.imageUrl)
    }

    @DeleteMapping("/api/restaurants/{storeId}/items/{itemId}")
    fun removeRestaurantItem(
        @PathVariable("storeId") storeId: Int,
        @PathVariable("itemId") itemId: Int,
    ) = restaurantItemMapper.removeRestaurantItem(storeId, itemId)
}