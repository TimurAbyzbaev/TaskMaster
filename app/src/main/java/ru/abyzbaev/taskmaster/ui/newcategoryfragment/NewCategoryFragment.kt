package ru.abyzbaev.taskmaster.ui.newcategoryfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.abyzbaev.taskmaster.databinding.NewCategoryFragmentBinding

class NewCategoryFragment : BottomSheetDialogFragment() {
    private var _binding: NewCategoryFragmentBinding? = null
    private val binding get() = _binding!!
    private var onAddClickListener: OnAddClickListener? = null

    private val textWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            binding.addButtonTextview.isEnabled =
                binding.searchEditText.text != null && !binding.searchEditText.text.toString()
                    .isEmpty()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {}
    }

    private val onAddButtonClickListener =
        View.OnClickListener {
            onAddClickListener?.onClick(binding.searchEditText.text.toString())
            dismiss()
        }

    internal fun setOnAddClickListener(listener: OnAddClickListener) {
        onAddClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewCategoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButtonTextview.setOnClickListener(onAddButtonClickListener)
        binding.searchEditText.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        onAddClickListener = null
        super.onDestroyView()
    }

    interface OnAddClickListener {
        fun onClick(categoryName: String)
    }

    companion object {
        fun newInstance(): NewCategoryFragment {
            return NewCategoryFragment()
        }
    }
}