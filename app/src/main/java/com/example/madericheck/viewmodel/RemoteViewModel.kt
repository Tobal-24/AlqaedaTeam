package com.example.madericheck.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madericheck.model.XanoAttendance
import com.example.madericheck.model.XanoUser
import com.example.madericheck.repository.RetrofitClient
import kotlinx.coroutines.launch

class RemoteViewModel : ViewModel() {

    private val _remoteRecords = MutableLiveData<List<XanoAttendance>>()
    val remoteRecords: LiveData<List<XanoAttendance>> get() = _remoteRecords

    private val _weatherStatus = MutableLiveData<String>()
    val weatherStatus: LiveData<String> get() = _weatherStatus

    private val _syncSuccess = MutableLiveData<Boolean>()
    val syncSuccess: LiveData<Boolean> get() = _syncSuccess

    fun registerUserInXano(rut: String, nombre: String, email: String, pass: String) {
        viewModelScope.launch {
            try {
                val newUser = XanoUser(rut = rut, nombre = nombre, email = email, password = pass)
                val response = RetrofitClient.apiService.registerUser(newUser)
                _syncSuccess.value = response.isSuccessful
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    fun sendAttendanceToXano(rut: String, time: String, type: String) {
        viewModelScope.launch {
            try {
                val record = XanoAttendance(userRut = rut, formattedTime = time, type = type)
                val response = RetrofitClient.apiService.createAttendance(record)
                _syncSuccess.value = response.isSuccessful
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    fun loadAttendanceFromXano() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAllAttendance()
                if (response.isSuccessful) {
                    _remoteRecords.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                _remoteRecords.value = emptyList()
            }
        }
    }

    fun loadPlantWeather() {
        viewModelScope.launch {
            try {
                val url = "https://api.open-meteo.com/v1/forecast?latitude=-33.49&longitude=-70.61&current_weather=true"
                val response = RetrofitClient.apiService.getExternalWeather(url)
                if (response.isSuccessful) {
                    _weatherStatus.value = "Clima en planta sincronizado correctamente"
                }
            } catch (e: Exception) {
                _weatherStatus.value = "Clima no disponible sin conexión"
            }
        }
    }
}