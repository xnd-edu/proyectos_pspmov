package com.example.navigation.ui.jsonplaceholder.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentPostsAddBinding
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JsonPlaceholderAddFragment : Fragment() {
    private var _binding: FragmentPostsAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JsonPlaceholderAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventos()
        observarState()
        observeEvents()
    }
    private fun eventos() {
        with(binding) {
            hechoButton.setOnClickListener {
                val conductor = obtainConductorFromInput()
                viewModel.handleIntent(JsonPlaceholderAddIntent.AddConductor(conductor))
            }
        }
    }

    private fun observarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.conductor?.let { conductor ->
                        binding.tituloTextField.setText(conductor.title)
                        binding.usuarioTextField.setText(conductor.userId)
                        binding.cuerpoTextField.setText(conductor.body)
                    }
                    binding.circularProgressIndicator.isVisible = state.isLoading
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiEvent.PopBackStack -> findNavController().navigateUp()
                        is UiEvent.ShowSnackbar -> Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun obtainConductorFromInput(): JsonPlaceholderPost {
        return JsonPlaceholderPost(
            binding.cuerpoTextField.text.toString(),
            0,
            binding.tituloTextField.text.toString(),
            binding.usuarioTextField.text.toString().toInt()
        )
    }
}