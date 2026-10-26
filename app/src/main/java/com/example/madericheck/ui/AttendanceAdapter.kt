package com.example.madericheck.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madericheck.R
import com.example.madericheck.model.AttendanceRecord

class AttendanceAdapter(private val records: List<AttendanceRecord>) :
    RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmployeeId: TextView = view.findViewById(R.id.tvRecordEmployeeId)
        val tvTimestamp: TextView = view.findViewById(R.id.tvRecordTimestamp)
        val tvType: TextView = view.findViewById(R.id.tvRecordType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.tvEmployeeId.text = "RUT / ID: ${record.employeeId}"
        holder.tvTimestamp.text = record.timestamp
        holder.tvType.text = record.type

        if (record.type == "ENTRADA") {
            holder.tvType.setBackgroundColor(Color.parseColor("#2E7D32")) // Verde Entrada
        } else {
            holder.tvType.setBackgroundColor(Color.parseColor("#C62828")) // Rojo Salida
        }
    }

    override fun getItemCount(): Int = records.size
}