package com.ukolov.s4etovod.api

import com.ukolov.s4etovod.model.login.LogPass
import com.ukolov.s4etovod.model.Rooms
import com.ukolov.s4etovod.model.User
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//интерфейс описывает какие запросы можно делать к серверу
interface RetrofitService {

    @Headers("Content-Type: application/json")
    @POST("/users/login")
    fun getToken(
        @Body loginPassword : LogPass
    ): Single<User>

    //@Headers( "Authorization: Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWxleCIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWVpZGVudGlmaWVyIjoiMDhkYWRjMjctZDk2ZS00ZjUzLThiOWEtM2I1NjdjZGM2ZGIyIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiQWJvbmVudCIsImV4cCI6MTY3MjAwMzYwMywiaXNzIjoibWFjcjBzLXRlc3QtcHJvZCIsImF1ZCI6IlM0ZXRvdm9kX2NvbnN1bWVycyJ9.pcOIqF8aMWqsOnlhtLJNoqUAeW_m31YurO-hCrlAQyM" )
    @Headers("Content-Type: application/json")
    @GET("/counterTypes")
    fun getCounterType(
        //@Header("Authorization: Bearer ")  token :String
        @HeaderMap headers: Map<String, String>

    ):Single<Rooms>

    //@Headers("Content-Type: application/json")
    @GET("/counterTypes")
    fun getCounterType1(
        @Header("Authorization")  token :String //динамический параметр. токен пользователя
    ):Single<Rooms>

    @GET("/rooms")
    fun getRooms(
        @Header("Authorization")  token :String //динамический параметр. токен пользователя
    ):Single<Rooms>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api-test.s4vod.ru")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}