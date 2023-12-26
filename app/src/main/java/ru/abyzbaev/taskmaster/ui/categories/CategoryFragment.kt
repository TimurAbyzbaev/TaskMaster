package ru.abyzbaev.taskmaster.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.databinding.FragmentCategoryBinding
import ru.abyzbaev.taskmaster.ui.tasks.TaskFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_task -> {
                val action = CategoryFragmentDirections.actionCategoryFragmentToTaskDetailFragment(0L)
                findNavController().navigate(action)
//                childFragmentManager.beginTransaction()
//                    .replace(R.id.categoryFragment, TaskFragment.newInstance(0L))
//                    .addToBackStack(null)
//                    .commit()
                Toast.makeText(requireContext(), "add new task", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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