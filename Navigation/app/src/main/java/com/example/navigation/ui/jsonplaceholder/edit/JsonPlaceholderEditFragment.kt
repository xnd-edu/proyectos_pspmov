package com.example.navigation.ui.jsonplaceholder.edit

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
import androidx.navigation.fragment.navArgs
import com.example.navigation.common.JsonPlaceholderConstants
import com.example.navigation.databinding.FragmentPostsEditBinding
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class JsonPlaceholderEditFragment : Fragment() {
    private var _binding: FragmentPostsEditBinding? = null
    private val binding get() = _binding!!
    private val args: JsonPlaceholderEditFragmentArgs by navArgs()
    private val viewModel: JsonPlaceholderEditViewModel by viewModels()

    @Inject
    lateinit var stringProvider: StringProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.id
        viewModel.handleIntent(JsonPlaceholderEditIntent.LoadPost(id))

        eventos()
        observarState()
        observeEvents()
    }

    private fun eventos() {
        with(binding) {
            eliminarButton.setOnClickListener {
                viewModel.state.value.conductor?.let { conductor ->
                    viewModel.handleIntent(JsonPlaceholderEditIntent.DeletePost(conductor))
                }
            }

            hechoButton.setOnClickListener {
                val conductor = obtainConductorFromInput()
                viewModel.handleIntent(JsonPlaceholderEditIntent.UpdatePost(conductor))
            }
        }
    }

    private fun observarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.conductor?.let { conductor ->
                        binding.tituloTextField.setText(conductor.title)
                        binding.usuarioTextField.setText(conductor.userId.toString())
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
        val id = args.id
        return JsonPlaceholderPost(
            binding.cuerpoTextField.text.toString(),
            id,
            binding.tituloTextField.text.toString(),
            binding.usuarioTextField.text.toString().toIntOrNull() ?: 0
        )
    }
}