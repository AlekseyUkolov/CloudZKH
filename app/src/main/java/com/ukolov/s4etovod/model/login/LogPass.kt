package com.ukolov.s4etovod.model.login

import com.google.gson.annotations.SerializedName

data class LogPass (
    @SerializedName("login") val login: String?,
    @SerializedName("password") val password: String?
)
