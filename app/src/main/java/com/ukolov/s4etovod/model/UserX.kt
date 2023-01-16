package com.ukolov.s4etovod.model

data class UserX(
    val id: String?,
    val buildings: List<Any?>?,
    val rooms: List<Any?>?,
    val email: String?,
    val login: String?,
    val isLocked: Boolean?,
    val roles: List<String?>?
)