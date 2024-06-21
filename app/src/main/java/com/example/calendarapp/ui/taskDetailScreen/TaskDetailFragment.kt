package com.example.calendarapp.ui.taskDetailScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calendarapp.data.rest.request.DeleteTaskRequest
import com.example.calendarapp.databinding.FragmentTaskDetailBinding
import com.example.calendarapp.ui.MainActivity
import com.example.calendarapp.utils.Consts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailBinding
    private lateinit var viewModel: TaskDetailVM
    private var title : String? = null
    private var description : String? = null
    private var date : String? = null
    private var taskId : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            title = it.getString(ARG_TITLE)
            description = it.getString(ARG_DESCRIPTION)
            date = it.getString(ARG_DATE)
            taskId = it.getInt(ARG_ID)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TaskDetailVM::class.java)

        initUI()
        initObservers()
    }

    private fun initObservers() {
        viewModel.deleteStatus.observe(viewLifecycleOwner, Observer {
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

    private fun initUI() {
        viewModel.initData(title, description, date, taskId)
        viewModel.title?.let {
            binding.tvTitle.text = it
        }
        viewModel.description?.let {
            binding.tvDescription.text = it
        }
        viewModel.date?.let {
            binding.tvDate.text = it
        }

        binding.btnDelete.setOnClickListener {
            if (taskId != null) {
                val deleteTaskRequest = DeleteTaskRequest(Consts.USER_ID, taskId!!)
                viewModel.deleteTask(deleteTaskRequest)
            }
            else {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).toggleBottomNavVisibility(true)
    }

    companion object {
        private const val ARG_TITLE : String = "arg_title"
        private const val ARG_DESCRIPTION : String = "arg_description"
        private const val ARG_DATE : String = "arg_date"
        private const val ARG_ID : String = "arg_task_id"

        fun newInstance(title : String, description : String, date : String, taskId : Int) : TaskDetailFragment {
            val fragment = TaskDetailFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            args.putString(ARG_DATE, date)
            args.putInt(ARG_ID, taskId)
            fragment.arguments = args
            return fragment
        }
    }
}