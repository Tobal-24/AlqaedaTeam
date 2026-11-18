package com.example.madericheck.model

import com.google.gson.annotations.SerializedName

data class XanoUser(
    val id: Int? = null,
    val rut: String,
    val nombre: String,
    val email: String,
    val password: String
)

data class XanoAttendance(
    val id: Int? = null,
    @SerializedName("user_rut") val userRut: String,
    @SerializedName("formatted_time") val formattedTime: String,
    val type: String
)