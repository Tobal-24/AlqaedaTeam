package com.example.madericheck.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madericheck.R
import com.example.madericheck.repository.AttendanceRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var repository: AttendanceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = AttendanceRepository(application)

        if (repository.isLoggedIn()) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etUser = findViewById<TextInputEditText>(R.id.etUser)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val rutInput = etUser.text.toString().trim()
            val passInput = etPassword.text.toString().trim()

            if (rutInput.isEmpty()) {
                etUser.error = "Ingrese su RUT o Usuario"
                return@setOnClickListener
            }
            if (passInput.isEmpty()) {
                etPassword.error = "Ingrese su contraseña"
                return@setOnClickListener
            }

            val isValid = repository.validateCredentials(rutInput, passInput)

            if (isValid) {
                Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "RUT o contraseña incorrectos. Si no tiene cuenta, regístrese primero.",
                    Toast.LENGTH_LONG
                ).show()
                etPassword.error = "Credenciales no válidas"
            }
        }

        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Contacte al administrador de la planta", Toast.LENGTH_LONG).show()
        }

        tvRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}