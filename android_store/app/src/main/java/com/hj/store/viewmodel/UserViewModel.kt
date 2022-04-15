package com.hj.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var _user = MutableLiveData<Int>()
    val user: LiveData<Int> get() = _user

    fun loginUserStatus(loginValue: Int) {
        _user.value = loginValue
    }
}