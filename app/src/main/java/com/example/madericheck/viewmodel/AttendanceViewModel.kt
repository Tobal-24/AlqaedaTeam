package com.example.madericheck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madericheck.model.AttendanceRecord
import com.example.madericheck.repository.AttendanceRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AttendanceRepository(application)

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun registerAttendance(employeeId: String, type: String) {
        if (employeeId.isBlank()) {
            _error.value = "Debe ingresar RUT / ID de empleado"
            return
        }

        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val record = AttendanceRecord(
            employeeId = employeeId,
            type = type,
            timestamp = timestamp
        )

        // Guardar el registro en SharedPreferences usando el repositorio
        repository.saveAttendanceRecord(record)

        _message.value = "Marcaje de $type registrado correctamente"
    }
}