package ru.abyzbaev.taskmaster.ui.tasks

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.databinding.FragmentTaskDetailBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class TaskDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TaskViewModelFactory

    private lateinit var viewModel: TaskViewModel
    private var taskId: Long? = null

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: TaskEntity

    private var selectedDate: Long = 1L

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                navigateToCategoryFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )

        (requireActivity().application as? TaskMasterApplication)?.appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        arguments?.let {
            taskId = it.getLong("taskId", -1)
            if(taskId == 0L) {
                //adding new task
                val id = Random.nextLong()
                val name = "New Task"
                val dueDate = System.currentTimeMillis() + 86400000
                val categoryId = 0L
                val newTask = TaskEntity(id, name, "Description", dueDate, categoryId)
                task = newTask
                initView()
            }
            else if (taskId != -1L) {
                viewModel.viewModelScope.launch {
                    task = viewModel.getTask(taskId!!)!!
                    initView()

                }
            } else {
                throw IllegalStateException("No attached task")
            }
        }
    }

    private fun initView() {
        binding.buttonSave.setOnClickListener {
            saveTask()
        }
        binding.dueDate.setOnClickListener {
            showDatePicker()
        }
        selectedDate = task.dueDate
        binding.dueDate.text = formatDateFromLong(task.dueDate)
        binding.titleTask.setText(task.title)
        binding.categoryTask.setText(task.categoryId.toString())
        binding.descriptionTask.setText(task.description)
    }

    private fun saveTask() {
        val title: String = binding.titleTask.text.toString()
        val description: String = binding.descriptionTask.text.toString()
        val dueDate: Long = selectedDate
        val category: Long = try {
            binding.categoryTask.text.toString().toLong()
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Not valid category number", Toast.LENGTH_SHORT).show()
            1L
        }

        if (taskId != null && taskId != -1L) {
            val editedTask = TaskEntity(taskId!!, title, description, dueDate, category)
            viewModel.updateTaskInDB(editedTask)
        } else {
            val newTask = TaskEntity(
                title = title,
                description = description,
                categoryId = category,
                dueDate = 0L
            )
        }
        navigateToCategoryFragment()
    }

    private fun navigateToCategoryFragment() {
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToCategoryFragment()
        findNavController().navigate(action)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            this.selectedDate = selectedDate.timeInMillis
            updateDateTextView(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun updateDateTextView(selectedDate: Calendar) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)
        binding.dueDate.text = formattedDate
    }

    fun formatDateFromLong(dateInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = Date(dateInMillis)
        return dateFormat.format(date)
    }
}