package com.example.madericheck.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.madericheck.R
import com.example.madericheck.model.XanoUser
import com.example.madericheck.repository.RetrofitClient
import com.example.madericheck.viewmodel.RegisterViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tilFullName = findViewById<TextInputLayout>(R.id.tilFullName)
        val tilRut = findViewById<TextInputLayout>(R.id.tilRut)
        val tilRole = findViewById<TextInputLayout>(R.id.tilRole)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val tilConfirmPassword = findViewById<TextInputLayout>(R.id.tilConfirmPassword)

        val etFullName = findViewById<TextInputEditText>(R.id.etFullName)
        val etRut = findViewById<TextInputEditText>(R.id.etRut)
        val etRole = findViewById<TextInputEditText>(R.id.etRole)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)

        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        viewModel.validationErrors.observe(this) { errors ->
            tilFullName.error = errors["fullName"]
            tilRut.error = errors["rut"]
            tilRole.error = errors["role"]
            tilPassword.error = errors["password"]
            tilConfirmPassword.error = errors["confirmPassword"]
        }

        viewModel.registerSuccess.observe(this) { success ->
            if (success) {
                btnRegister.isEnabled = false
                val rutVal = etRut.text.toString().trim()
                val nombreVal = etFullName.text.toString().trim()
                val roleVal = etRole.text.toString().trim()
                val passVal = etPassword.text.toString().trim()
                val cleanRut = rutVal.replace(".", "").replace("-", "")
                val emailVal = if (roleVal.contains("@")) roleVal else "$cleanRut@madericheck.cl"

                lifecycleScope.launch {
                    try {
                        val nuevoUsuario = XanoUser(
                            rut = rutVal,
                            nombre = nombreVal,
                            email = emailVal,
                            password = passVal
                        )
                        RetrofitClient.apiService.registerUser(nuevoUsuario)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        Toast.makeText(this@RegisterActivity, "¡Registro de operario exitoso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            tilFullName.error = null
            tilRut.error = null
            tilRole.error = null
            tilPassword.error = null
            tilConfirmPassword.error = null

            viewModel.registerUser(
                fullName = etFullName.text.toString(),
                rut = etRut.text.toString(),
                role = etRole.text.toString(),
                pass = etPassword.text.toString(),
                confirmPass = etConfirmPassword.text.toString()
            )
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}