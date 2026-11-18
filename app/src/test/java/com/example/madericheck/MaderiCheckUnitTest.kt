package com.example.madericheck

import com.example.madericheck.model.XanoAttendance
import com.example.madericheck.model.XanoUser
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class MaderiCheckUnitTest {

    private fun validateUserForm(rut: String, email: String, password: String): Boolean {
        if (rut.isBlank() || email.isBlank() || password.isBlank()) {
            return false
        }
        return email.contains("@") && email.contains(".")
    }

    @Test
    fun formularioConCamposVaciosDebeFallar() {
        val isValid = validateUserForm("", "trabajador@madericheck.cl", "123456")
        assertFalse(isValid)
    }

    @Test
    fun formularioConCorreoInvalidoDebeFallar() {
        val isValid = validateUserForm("19.123.456-7", "correosinformatocorrecto", "123456")
        assertFalse(isValid)
    }

    @Test
    fun formularioConDatosValidosDebeAprobar() {
        val isValid = validateUserForm("19.123.456-7", "trabajador@madericheck.cl", "123456")
        assertTrue(isValid)
    }

    @Test
    fun modeloAsistenciaXanoGuardaDatosCorrectamente() {
        val attendance = XanoAttendance(
            id = 1,
            userRut = "19.123.456-7",
            formattedTime = "18/11/2026 08:00",
            type = "ENTRADA"
        )
        assertEquals("ENTRADA", attendance.type)
        assertEquals("19.123.456-7", attendance.userRut)
    }

    @Test
    fun modeloUsuarioXanoGuardaDatosCorrectamente() {
        val user = XanoUser(
            id = 1,
            rut = "19.123.456-7",
            nombre = "Cristobal Abarca",
            email = "cristobal@madericheck.cl",
            password = "clave123"
        )
        assertEquals("Cristobal Abarca", user.nombre)
    }
}