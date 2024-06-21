package com.example.calendarapp.ui.addTaskScreen

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calendarapp.data.rest.request.CreateTaskRequest
import com.example.calendarapp.data.rest.response.TaskDetail
import com.example.calendarapp.databinding.FragmentAddTaskBinding
import com.example.calendarapp.ui.MainActivity
import com.example.calendarapp.utils.Consts
import com.example.calendarapp.utils.getMonth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: AddTaskVM
    private var selectedDate : String? = null
    private var isDateSelected : Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        arguments?.let {
            selectedDate = it.getString(ARG_DATE)
            isDateSelected = it.getBoolean(ARG_FLAG_DATE_PRESENT)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddTaskVM::class.java)
        binding.viewModel = viewModel

        isDateSelected?.let {
            if (it) binding.tvSelectedDate.text = selectedDate
        }

        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(requireContext() , DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.tvSelectedDate.text = "$dayOfMonth ${getMonth(month + 1)} $year"
            }, year, month, day)
            dialog.show()
        }

        binding.btnAddTask.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val selectedDate = binding.tvSelectedDate.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && selectedDate.isNotEmpty()) {
                val taskDetail = TaskDetail(title, description, selectedDate)
                val createTaskRequest = CreateTaskRequest(Consts.USER_ID, taskDetail)
                viewModel.addTask(createTaskRequest)
            }
            else {
                Toast.makeText(context, "Please Input All Fields", Toast.LENGTH_SHORT).show()
            }
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.addStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        })

        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).toggleBottomNavVisibility(true)
    }

    companion object {
        private const val ARG_DATE : String = "arg_date"
        private const val ARG_FLAG_DATE_PRESENT : String = "arg_date_present"

        fun newInstance(date : String, isDateSelected : Boolean) : AddTaskFragment {
            val fragment = AddTaskFragment()
            val args = Bundle()
            args.putString(ARG_DATE, date)
            args.putBoolean(ARG_FLAG_DATE_PRESENT, isDateSelected)
            fragment.arguments = args
            return fragment
        }
    }
}