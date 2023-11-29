package ru.abyzbaev.taskmaster.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.data.model.TaskEntity
import ru.abyzbaev.taskmaster.databinding.FragmentTaskBinding
import javax.inject.Inject

class TaskFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TaskViewModelFactory

    private lateinit var viewModel: TaskViewModel

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val adapter: TaskAdapter by lazy {
        TaskAdapter { task ->
            navigateToTaskDetailFragment(task.id)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as? TaskMasterApplication)?.appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        binding.recyclerViewTasks.adapter = adapter

    }

    private fun initViewModel() {
        if (binding.recyclerViewTasks.adapter != null) {
            throw IllegalStateException("The viewModel should initialised first")
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        viewModel.subscribeToLiveData().observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun navigateToTaskDetailFragment(taskId: Long) {
        val action = TaskFragmentDirections.actionTaskFragmentToTaskDetailFragment(taskId)
        findNavController().navigate(action)
    }
}