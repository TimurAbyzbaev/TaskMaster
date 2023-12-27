package ru.abyzbaev.taskmaster.ui.tasks

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.data.model.CategoryEntity
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.databinding.FragmentTaskDetailBinding
import ru.abyzbaev.taskmaster.ui.NewCategoryFragment
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class TaskDetailFragment : Fragment() {

    @Inject
    lateinit var taskViewModelFactory: TaskViewModelFactory

    private lateinit var taskViewModel: TaskViewModel

    private var taskId: Long? = null

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: TaskEntity

    private var selectedDate: Long = 1L
    private var categoriesList: ArrayList<CategoryEntity> = arrayListOf()
    private lateinit var adapter: ArrayAdapter<String>

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

        taskViewModel = ViewModelProvider(this, taskViewModelFactory).get(TaskViewModel::class.java)

        arguments?.let {
            taskId = it.getLong("taskId", -1)
            if (taskId == 0L) {
                //adding new task
                val id = Random.nextLong()
                val name = "New Task"
                val dueDate = System.currentTimeMillis() + 86400000
                val categoryId = 0L
                val newTask = TaskEntity(id, name, "Description", dueDate, categoryId)
                task = newTask
                initView()
            } else if (taskId != -1L) {
                taskViewModel.viewModelScope.launch {
                    task = taskViewModel.getTask(taskId!!)!!
                    initView()

                }
            } else {
                throw IllegalStateException("No attached task")
            }
        }

        taskViewModel.subscribeToCategoriesList()
            .observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
                categoriesList.clear()
                categoriesList.addAll(it)
                initSpinner(categoriesList)
            })


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
        //binding.categoryTask.setText(task.categoryId.toString())
        binding.descriptionTask.setText(task.description)
        binding.addCategory.setOnClickListener {
            val newCategoryFragment = NewCategoryFragment.newInstance()
            newCategoryFragment.setOnAddClickListener(object :
                NewCategoryFragment.OnAddClickListener {
                override fun onClick(categoryName: String) {
                    addNewCategory(categoryName)
                }
            })
            newCategoryFragment.show(childFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }

    }

    private fun addNewCategory(categoryName: String){
        taskViewModel.addCategory(categoryName)
        initSpinner(categoriesList)
        adapter.notifyDataSetChanged()
    }

    private fun initSpinner(categories: List<CategoryEntity>) {
        val items: ArrayList<String> = arrayListOf()
        for (category in categories) {
            items.add(category.name)
        }
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryTask.adapter = adapter

        binding.categoryTask.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Выбран элемент списка
                val selectedItem = items
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Ничего не выбрано
            }
        }
    }


//    private fun initSpinner(categories: List<CategoryEntity>) {
//        val items: ArrayList<String> = arrayListOf()
//        for (category in categories) {
//            items.add(category.name)
//        }
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.categoryTask.adapter = adapter
//
//        binding.categoryTask.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parentView: AdapterView<*>?,
//                selectedItemView: View?,
//                position: Int,
//                id: Long
//            ) {
//                // Выбран элемент списка
//                val selectedItem = items
//                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                // Ничего не выбрано
//            }
//        }
//    }

    private fun saveTask() {
        val title: String = binding.titleTask.text.toString()
        val description: String = binding.descriptionTask.text.toString()
        val dueDate: Long = selectedDate
        val categoryName = binding.categoryTask.selectedItem
        val category = categoriesList.find { it.name == categoryName }!!.id

        if (taskId == 0L) {
            taskId = Random.nextLong()
        }

        if (taskId != null && taskId != -1L) {
            val editedTask = TaskEntity(taskId!!, title, description, dueDate, category)
            taskViewModel.updateTaskInDB(editedTask)
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

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
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

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}