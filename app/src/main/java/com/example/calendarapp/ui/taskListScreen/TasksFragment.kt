package com.example.calendarapp.ui.taskListScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.ui.MainActivity
import com.example.calendarapp.databinding.FragmentTasksBinding
import com.example.calendarapp.ui.addTaskScreen.AddTaskFragment
import com.example.calendarapp.ui.taskDetailScreen.TaskDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var viewModel: TaskListVM
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskListAdapter: TaskListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TaskListVM::class.java)

        initUI()
        initObservers()
    }

    private fun initObservers() {
        viewModel.taskData.observe(viewLifecycleOwner, Observer {
            it?.let {
                taskListAdapter.setTaskList(it)
            }
        })
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initUI() {
        recyclerView = binding.rvTaskList

        val layoutManager = LinearLayoutManager(requireContext())
        taskListAdapter = TaskListAdapter()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = taskListAdapter

        viewModel.fetchTasks()

        binding.floatingActionButton.setOnClickListener {
            (activity as MainActivity).addFragment(AddTaskFragment())
            (activity as MainActivity).toggleBottomNavVisibility(false)
        }

        taskListAdapter.onItemClickListener = {
            (activity as MainActivity).addFragment(TaskDetailFragment.newInstance(it.task_detail.title, it.task_detail.description, it.task_detail.created_date, it.task_id))
            (activity as MainActivity).toggleBottomNavVisibility(false)
        }
    }
}