package com.codinginflow.mvvmtodo.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentAddEditTaskBinding
import com.codinginflow.mvvmtodo.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel :AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            etTaskName.setText(viewModel.taskName)
            cbImportant.isChecked=viewModel.taskImportance
            cbImportant.jumpDrawablesToCurrentState()
            tvDateCreated.isVisible=viewModel.task != null
            tvDateCreated.text="Created: ${viewModel.task?.createdDateFormatted}"

            etTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }
            cbImportant.setOnCheckedChangeListener{ _ , isChecked ->
                viewModel.taskImportance= isChecked
            }
            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when(event){
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.etTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}