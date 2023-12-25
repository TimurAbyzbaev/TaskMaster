package ru.abyzbaev.taskmaster.ui.tasks

import android.content.Context
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
import javax.inject.Inject

class TaskDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TaskViewModelFactory

    private lateinit var viewModel: TaskViewModel
    private var taskId: Long? = null

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: TaskEntity

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
            if (taskId != -1L) {
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
        binding.titleTask.setText(task.title)
        binding.categoryTask.setText(task.categoryId.toString())
        binding.descriptionTask.setText(task.description)
    }

    private fun saveTask() {
        val title: String = view?.findViewById<EditText>(R.id.title_task)?.text.toString()
        val description: String =
            view?.findViewById<EditText>(R.id.description_task)?.text.toString()
        val category: Long = try {
            view?.findViewById<EditText>(R.id.category_task)?.text.toString().toLong()
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Not valid category number", Toast.LENGTH_SHORT).show()
            1L
        }

        if (taskId != null && taskId != -1L) {
            val editedTask = TaskEntity(taskId!!, title, description, 0L, category)
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
}