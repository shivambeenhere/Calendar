package com.example.calendarapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.calendarapp.ui.calendarScreen.CalendarFragment
import com.example.calendarapp.R
import com.example.calendarapp.databinding.ActivityMainBinding
import com.example.calendarapp.ui.taskListScreen.TasksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(TasksFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menuHome -> replaceFragment(CalendarFragment())
                R.id.menuTasks -> replaceFragment(TasksFragment())
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    fun addFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun toggleBottomNavVisibility(shouldShow : Boolean) {
        if (shouldShow)
            binding.bottomNavigationView.visibility = View.VISIBLE
        else binding.bottomNavigationView.visibility = View.GONE
    }
}