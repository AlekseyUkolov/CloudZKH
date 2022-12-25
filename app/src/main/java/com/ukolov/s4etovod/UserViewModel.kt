package com.ukolov.s4etovod

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class UserViewModel(application: Application) : AndroidViewModel(application) {
    var token = MutableLiveData<String>()
//        init{
//            token.value="111"
//        }
}