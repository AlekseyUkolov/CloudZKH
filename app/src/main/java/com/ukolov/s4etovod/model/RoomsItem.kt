package com.ukolov.s4etovod.model

data class RoomsItem(
    val buildingId: String?,
    val counters: List<Counter?>?,
    val id: String?,
    val needToSumTariffs: Boolean?,
    val number: String?
)