package ru.abyzbaev.taskmaster.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import javax.inject.Inject

class CategoryFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: CategoryViewModelFactory

    private lateinit var viewModel: CategoryViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TaskMasterApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init viewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_categories)
        val adapter = CategoryAdapter(emptyList()) // сюда передать начальный список категорий
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.categories

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories)
        }

    }
}