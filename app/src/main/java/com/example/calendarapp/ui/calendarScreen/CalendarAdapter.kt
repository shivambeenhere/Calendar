package com.example.calendarapp.ui.calendarScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.data.rest.CalendarModal
import com.example.calendarapp.databinding.CalendarCellBinding

class CalendarAdapter(private val list : ArrayList<CalendarModal>, private val textMonthYear : String) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    var onDayClickListener: ((String) -> Unit)? = null

    class CalendarViewHolder(private val binding: CalendarCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(calendar: CalendarModal) {
            val day = calendar.day
            binding.apply {
                if (day > 0) tvDayNumb.text = "$day"
                binding.isSelected = calendar.isSelected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            if (list[position].day != 0)
            onDayClickListener?.invoke("${list[position].day} $textMonthYear")
        }
    }
}