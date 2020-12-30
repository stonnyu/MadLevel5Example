package com.example.madlevel5example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5example.model.Reminder
import com.example.madlevel5example.model.ReminderViewModel
import kotlinx.android.synthetic.main.fragment_reminders.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RemindersFragment : Fragment() {

    private var reminders: ArrayList<Reminder> = arrayListOf()

    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRv()
        observeAddReminderResult()
    }


    private fun initRv() {

        reminderAdapter = ReminderAdapter(reminders)
        viewManager = LinearLayoutManager(activity)

        createItemTouchHelper().attachToRecyclerView(rvReminders)
        rvReminders.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = reminderAdapter
        }
    }

    private fun observeAddReminderResult() {
        viewModel.reminders.observe(viewLifecycleOwner, Observer { reminders ->
            this@RemindersFragment.reminders.clear()
            this@RemindersFragment.reminders.addAll(reminders)
            reminderAdapter.notifyDataSetChanged()
        })
    }

        /**
         * Create a touch helper to recognize when a user swipes an item from a recycler view.
         * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         * and uses callbacks to signal when a user is performing these actions.
         */
        private fun createItemTouchHelper(): ItemTouchHelper {

            // Callback which is used to create the ItemTouch helper. Only enables left swipe.
            // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
            val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                // Enables or Disables the ability to move items up and down.
                override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // Callback triggered when a user swiped an item.
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val reminderToDelete = reminders[position]

                    viewModel.deleteReminder(reminderToDelete)
                }
            }
            return ItemTouchHelper(callback)
        }

    }