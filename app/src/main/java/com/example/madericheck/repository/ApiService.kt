package com.example.madericheck.repository

import com.example.madericheck.model.XanoAttendance
import com.example.madericheck.model.XanoUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET("usuario")
    suspend fun getAllUsers(): Response<List<XanoUser>>

    @POST("usuario")
    suspend fun registerUser(@Body user: XanoUser): Response<XanoUser>

    @GET("usuario/{usuario_id}")
    suspend fun getUserById(@Path("usuario_id") id: Int): Response<XanoUser>

    @DELETE("usuario/{usuario_id}")
    suspend fun deleteUser(@Path("usuario_id") id: Int): Response<Unit>

    @GET("asistencia")
    suspend fun getAllAttendance(): Response<List<XanoAttendance>>

    @POST("asistencia")
    suspend fun createAttendance(@Body record: XanoAttendance): Response<XanoAttendance>

    @PATCH("asistencia/{asistencia_id}")
    suspend fun updateAttendance(@Path("asistencia_id") id: Int, @Body record: XanoAttendance): Response<XanoAttendance>

    @DELETE("asistencia/{asistencia_id}")
    suspend fun deleteAttendance(@Path("asistencia_id") id: Int): Response<Unit>

    @GET
    suspend fun getExternalWeather(@Url fullUrl: String): Response<Any>
}