package com.ukolov.s4etovod

import com.google.gson.annotations.SerializedName

data class LogPass (
    @SerializedName("login") val login: String?,
    @SerializedName("password") val password: String?
)
