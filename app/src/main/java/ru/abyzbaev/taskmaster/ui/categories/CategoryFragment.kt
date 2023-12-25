package ru.abyzbaev.taskmaster.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.databinding.FragmentCategoryBinding
import javax.inject.Inject

class CategoryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: CategoryViewModelFactory

    private lateinit var viewModel: CategoryViewModel

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val adapter: CategoryAdapter by lazy {
        CategoryAdapter(requireActivity().supportFragmentManager)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as TaskMasterApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(layoutInflater)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.recyclerViewCategories.adapter = adapter
    }

    private fun initViewModel() {
        if (binding.recyclerViewCategories.adapter != null) {
            throw IllegalStateException("The viewModel should initialised first")
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)

        viewModel.subscribeToLiveData().observe(this.viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }
}