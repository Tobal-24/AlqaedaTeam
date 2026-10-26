package com.example.madericheck.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.madericheck.R
import com.example.madericheck.repository.AttendanceRepository
import com.google.android.material.button.MaterialButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var repository: AttendanceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        repository = AttendanceRepository(application)

        val tvWelcomeName = findViewById<TextView>(R.id.tvWelcomeName)
        val tvUserRole = findViewById<TextView>(R.id.tvUserRole)
        val tvUserRut = findViewById<TextView>(R.id.tvUserRut)
        val btnGoToAttendance = findViewById<MaterialButton>(R.id.btnGoToAttendance)
        val btnViewHistory = findViewById<MaterialButton>(R.id.btnViewHistory)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        val user = repository.getSavedUser()
        if (user != null) {
            tvWelcomeName.text = "¡Bienvenido, ${user.fullName}!"
            tvUserRole.text = "Cargo: ${user.role}"
            tvUserRut.text = "RUT: ${user.rut}"
        }

        btnGoToAttendance.setOnClickListener {
            val intent = Intent(this, AttendanceActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnViewHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnLogout.setOnClickListener {
            repository.setLoggedIn(false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_left)
            finish()
        }
    }
}