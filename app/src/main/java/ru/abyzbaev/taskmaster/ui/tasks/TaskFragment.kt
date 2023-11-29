package ru.abyzbaev.taskmaster.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import javax.inject.Inject

class TaskFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: TaskViewModelFactory

    private lateinit var viewModel: TaskViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TaskMasterApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //инициализация viewmodel
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_tasks)
        val adapter = TaskAdapter(emptyList()) // сюда передать начальный список задач
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }

    }
}