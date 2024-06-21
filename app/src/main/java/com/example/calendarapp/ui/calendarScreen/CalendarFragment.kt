package com.example.calendarapp.ui.calendarScreen

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendarapp.data.rest.CalendarModal
import com.example.calendarapp.databinding.FragmentCalendarBinding
import com.example.calendarapp.ui.MainActivity
import com.example.calendarapp.ui.addTaskScreen.AddTaskFragment
import com.example.calendarapp.utils.createDate
import com.example.calendarapp.utils.getMonth
import com.example.calendarapp.utils.isToday
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime
import java.util.Calendar

@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private var curDate = DateTime()
    private var date: DateTime? = null
    private var dateList: MutableList<CalendarModal>? = null
    private lateinit var adapter: CalendarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMonthText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(requireContext() , DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.tvMonthText.text = "$dayOfMonth / $month / $year"
            }, year, month, day)
            dialog.show()
        }

        binding.apply {
            rvCalendar.layoutManager = GridLayoutManager(context, 7)
            rvCalendar.setHasFixedSize(true)
            
            
            
            ivPrevMonth.setOnClickListener { 
                curDate = curDate.minusMonths(1)
                setCalendar(curDate)
            }
            ivNextMonth.setOnClickListener { 
                curDate = curDate.plusMonths(1)
                setCalendar(curDate)
            }
            floatingActionButton.setOnClickListener {
                val dateString = tvSelectedDate.text.toString()
                if (dateString.isNotEmpty()) {
                    val date: String = tvSelectedDate.text.toString().substring(16)
                    (activity as MainActivity).addFragment(AddTaskFragment.newInstance(date, true))
                    (activity as MainActivity).toggleBottomNavVisibility(false)
                }
                else {
                    Toast.makeText(context, "Please Select a Date!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        setCalendar(curDate)
    }

    private fun setCalendar(curDate: DateTime) {
        binding.tvMonthText.text = "${getMonth(curDate.monthOfYear)} ${curDate.year}"
        date = curDate.withTime(0, 0, 0, 0)
        val numDays = date!!.dayOfMonth().maximumValue
        date = date!!.minusDays(date!!.dayOfMonth().get() - 1)
        var dayOfWeek = date!!.dayOfWeek

        if (dayOfWeek == 0) dayOfWeek = 0
        if (dateList == null) dateList = ArrayList()
        else dateList!!.clear()


        for (i in 1..(numDays + dayOfWeek)) {

            val model: CalendarModal = if (i <= dayOfWeek) {
                CalendarModal(0, "", false)
            } else {
                val dateTemp = createDate(i - dayOfWeek, curDate.monthOfYear, curDate.year)
                if (isToday(dateTemp)) {
                    if (binding.tvSelectedDate.text.toString().isEmpty())
                        binding.tvSelectedDate.text = "Selected Date : ${i - dayOfWeek} ${binding.tvMonthText.text}"
                    CalendarModal(i - dayOfWeek, dateTemp.toLocalDate().toString(), true)
                }
                else {
                    CalendarModal(i - dayOfWeek, dateTemp.toLocalDate().toString(), false)
                }
            }
            dateList!!.add(model)
        }

        adapter = CalendarAdapter(dateList as ArrayList<CalendarModal>, binding.tvMonthText.text.toString())
        binding.rvCalendar.adapter = adapter

        adapter.onDayClickListener = {
            binding.tvSelectedDate.text = "Selected Date : $it"
            Toast.makeText(context, "Click the Plus Icon to add task", Toast.LENGTH_SHORT).show()
        }
    }
}