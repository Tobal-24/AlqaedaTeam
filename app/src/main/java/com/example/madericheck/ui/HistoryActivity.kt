package com.example.madericheck.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madericheck.R
import com.example.madericheck.repository.AttendanceRepository

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        val tvEmptyHistory = findViewById<TextView>(R.id.tvEmptyHistory)

        val repository = AttendanceRepository(application)
        val records = repository.getAttendanceRecords()

        if (records.isEmpty()) {
            tvEmptyHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.GONE
        } else {
            tvEmptyHistory.visibility = View.GONE
            rvHistory.visibility = View.VISIBLE
            rvHistory.layoutManager = LinearLayoutManager(this)
            rvHistory.adapter = AttendanceAdapter(records)
        }
    }
}