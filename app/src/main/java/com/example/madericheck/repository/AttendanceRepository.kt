package com.example.madericheck.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.madericheck.model.AttendanceRecord
import com.example.madericheck.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AttendanceRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("madericheck_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveAttendanceRecord(record: AttendanceRecord) {
        val records = getAttendanceRecords().toMutableList()
        records.add(0, record)
        val json = gson.toJson(records)
        sharedPreferences.edit().putString("attendance_history", json).apply()
    }

    fun getAttendanceRecords(): List<AttendanceRecord> {
        val json = sharedPreferences.getString("attendance_history", null) ?: return emptyList()
        val type = object : TypeToken<List<AttendanceRecord>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveUser(user: User) {
        sharedPreferences.edit()
            .putString("registered_rut", user.rut)
            .putString("registered_pass", user.password)
            .putString("registered_name", user.fullName)
            .putString("registered_role", user.role)
            .apply()
    }

    fun getSavedUser(): User? {
        val rut = sharedPreferences.getString("registered_rut", null)
        val pass = sharedPreferences.getString("registered_pass", null)
        val name = sharedPreferences.getString("registered_name", null)
        val role = sharedPreferences.getString("registered_role", null) ?: "Operario"

        return if (rut != null && pass != null && name != null) {
            User(fullName = name, rut = rut, role = role, password = pass)
        } else null
    }

    fun validateCredentials(rut: String, pass: String): Boolean {
        val savedUser = getSavedUser()
        val isValid = savedUser != null && savedUser.rut.equals(rut, ignoreCase = true) && savedUser.password == pass
        if (isValid) {
            setLoggedIn(true)
        }
        return isValid
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
}