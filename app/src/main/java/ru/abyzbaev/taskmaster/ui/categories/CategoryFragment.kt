package ru.abyzbaev.taskmaster.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.abyzbaev.taskmaster.R
import ru.abyzbaev.taskmaster.app.TaskMasterApplication
import ru.abyzbaev.taskmaster.data.model.CategoryEntity
import ru.abyzbaev.taskmaster.databinding.FragmentCategoryBinding
import ru.abyzbaev.taskmaster.ui.emptycategorylistener.OnEmptyCategoryListener
import javax.inject.Inject
import kotlin.random.Random

class CategoryFragment : Fragment(), OnEmptyCategoryListener {

    @Inject
    lateinit var viewModelFactory: CategoryViewModelFactory

    private lateinit var viewModel: CategoryViewModel

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val adapter: CategoryAdapter by lazy {
        CategoryAdapter(requireActivity().supportFragmentManager, this)
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
                if (adapter.itemCount == 0) {
                    viewModel.addCategory("Indefinite category", Random.nextLong())
                }

                val action =
                    CategoryFragmentDirections.actionCategoryFragmentToTaskDetailFragment(0L)
                findNavController().navigate(action)
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

    override fun onEmptyCategory(category: CategoryEntity) {
        viewModel.deleteCategoryFromDB(category)
    }

    override fun onEmptyCategoryList() {
        binding.emptyTasksTextview.visibility = View.VISIBLE
    }
}