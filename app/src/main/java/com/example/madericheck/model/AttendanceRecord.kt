package com.example.madericheck.model

data class AttendanceRecord(
    val employeeId: String,
    val type: String,
    val timestamp: String,
    val location: String = "Planta Principal - Fábrica"
)