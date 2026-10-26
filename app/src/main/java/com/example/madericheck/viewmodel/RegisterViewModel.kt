package com.example.madericheck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.madericheck.model.User
import com.example.madericheck.repository.AttendanceRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AttendanceRepository(application)

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _validationErrors = MutableLiveData<Map<String, String>>()
    val validationErrors: LiveData<Map<String, String>> get() = _validationErrors

    fun registerUser(
        fullName: String,
        rut: String,
        role: String,
        pass: String,
        confirmPass: String
    ) {
        val errors = mutableMapOf<String, String>()

        if (fullName.isBlank()) errors["fullName"] = "Ingrese el nombre completo"
        if (rut.isBlank()) errors["rut"] = "Ingrese el RUT o código de operario"
        if (role.isBlank()) errors["role"] = "Ingrese el cargo u ocupación"
        if (pass.length < 4) errors["password"] = "La contraseña debe tener al menos 4 caracteres"
        if (pass != confirmPass) errors["confirmPassword"] = "Las contraseñas no coinciden"

        if (errors.isNotEmpty()) {
            _validationErrors.value = errors
            return
        }

        val newUser = User(
            fullName = fullName.trim(),
            rut = rut.trim(),
            role = role.trim(),
            password = pass
        )

        repository.saveUser(newUser)
        _registerSuccess.value = true
    }
}