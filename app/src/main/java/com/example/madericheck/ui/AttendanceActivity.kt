package com.example.madericheck.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.madericheck.R
import com.example.madericheck.viewmodel.AttendanceViewModel
import com.example.madericheck.viewmodel.RemoteViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AttendanceActivity : AppCompatActivity() {

    private val viewModel: AttendanceViewModel by viewModels()
    private val remoteViewModel: RemoteViewModel by viewModels()
    private lateinit var tvClock: TextView
    private lateinit var tvDate: TextView
    private var lastRut = ""
    private var lastType = ""

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            triggerVibration(100)
            Toast.makeText(this, "Credencial/QR escaneado con éxito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Lectura de cámara cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(this, "Se requiere permiso de cámara para escanear", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    private val handler = Handler(Looper.getMainLooper())
    private val clockRunnable = object : Runnable {
        override fun run() {
            val calendar = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale("es", "CL"))
            val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "CL"))

            tvClock.text = timeFormat.format(calendar.time).uppercase()
            tvDate.text = dateFormat.format(calendar.time).replaceFirstChar { it.uppercase() }

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_in)
        requestNotificationPermissionIfNeeded()

        tvClock = findViewById(R.id.tvClock)
        tvDate = findViewById(R.id.tvDate)

        val etEmployeeId = findViewById<TextInputEditText>(R.id.etEmployeeId)
        val btnScanQr = findViewById<MaterialButton>(R.id.btnScanQr)
        val btnCheckIn = findViewById<MaterialButton>(R.id.btnCheckIn)
        val btnCheckOut = findViewById<MaterialButton>(R.id.btnCheckOut)

        val pulseAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)

        viewModel.message.observe(this) { successMsg ->
            triggerVibration(200)
            val currentTime = tvClock.text.toString()
            val fullDateTime = "${tvDate.text}$currentTime"
            remoteViewModel.sendAttendanceToXano(lastRut, fullDateTime, lastType)
            showAttendanceNotification(lastType, currentTime)
            Toast.makeText(this, successMsg, Toast.LENGTH_LONG).show()
            etEmployeeId.text?.clear()
            etEmployeeId.error = null
        }

        viewModel.error.observe(this) { errorMsg ->
            triggerVibration(400)
            etEmployeeId.error = errorMsg
        }

        btnCheckIn.setOnClickListener {
            it.startAnimation(pulseAnimation)
            lastRut = etEmployeeId.text.toString().trim()
            lastType = "ENTRADA"
            viewModel.registerAttendance(lastRut, lastType)
        }

        btnCheckOut.setOnClickListener {
            it.startAnimation(pulseAnimation)
            lastRut = etEmployeeId.text.toString().trim()
            lastType = "SALIDA"
            viewModel.registerAttendance(lastRut, lastType)
        }

        btnScanQr.setOnClickListener {
            it.startAnimation(pulseAnimation)
            checkCameraPermissionAndOpen()
        }

        handler.post(clockRunnable)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showAttendanceNotification(type: String, time: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        val helper = NotificationHelper(this)
        helper.showNotification(type, time)
    }

    private fun triggerVibration(milliseconds: Long) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }

    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(clockRunnable)
    }
}