package com.jin.store.controller

import com.jin.store.model.User
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class UserController() {
    @PostMapping("/api/user")
    fun checkUser(@RequestBody user: User): Boolean {
        val map = mutableMapOf<String, Boolean>()
        return (user.id == "admin" && user.password == "am123!@#").also {
            map["login"] = it
            map
        }
    }
}