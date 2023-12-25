package ru.abyzbaev.taskmaster.ui.tasks

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.databinding.FragmentTaskBinding
import ru.abyzbaev.taskmaster.ui.categories.CategoryFragmentDirections
import javax.inject.Inject

class TaskFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: TaskViewModelFactory

    private lateinit var viewModel: TaskViewModel

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val adapter: TaskRecyclerViewAdapter by lazy {
        TaskRecyclerViewAdapter { task ->
            navigateToTaskDetailFragment(task.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TaskFragment $this", "onCreate")
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
        Log.d("TaskFragment $this", "onCreateView")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        binding.recyclerViewTasks.adapter = adapter
        Log.d("####", adapter.toString())
        Log.d("TaskFragment $this", "onViewCreated")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TaskFragment $this", "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TaskFragment $this", "onDestroyView")
    }

    private fun initViewModel() {
        if (binding.recyclerViewTasks.adapter != null) {
            throw IllegalStateException("The viewModel should initialised first")
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        val categoryId: Long? = arguments?.getLong(ARG_CATEGORY_ID)
        Log.d("####", "lifecycleOwner ${this.viewLifecycleOwner}")
        viewModel.subscribeToLiveData(categoryId).observe(this.viewLifecycleOwner, Observer {
            activity?.runOnUiThread {
                adapter.setData(it)
                Log.d("####", "Adapter $adapter setData $it")
            }
        })
    }

    private fun navigateToTaskDetailFragment(taskId: Long) {
        //val action = TaskFragmentDirections.actionTaskFragmentToTaskDetailFragment(taskId)
        val action = CategoryFragmentDirections.actionCategoryFragmentToTaskDetailFragment(taskId)
        findNavController().navigate(action)
    }

    companion object {
        private const val ARG_CATEGORY_ID = "CATEGORY_ID"

        fun newInstance(categoryId: Long): TaskFragment {
            val fragment = TaskFragment()
            val args = Bundle()
            args.putLong(ARG_CATEGORY_ID, categoryId)
            fragment.arguments = args
            return fragment
        }
    }
}