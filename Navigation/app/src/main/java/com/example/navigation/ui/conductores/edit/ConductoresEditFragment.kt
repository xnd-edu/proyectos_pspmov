package com.example.navigation.ui.conductores.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.navigation.R
import com.example.navigation.databinding.FragmentConductoresEditBinding
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConductoresEditFragment : Fragment() {
    private var _binding: FragmentConductoresEditBinding? = null
    private val binding get() = _binding!!
    private val args: ConductoresEditFragmentArgs by navArgs()
    private val viewModel: ConductoresEditViewModel by viewModels()

    @Inject
    lateinit var stringProvider: StringProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConductoresEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.dni.toIntOrNull() ?: 0
        viewModel.handleIntent(JsonPlaceholderEditIntent.LoadPost(id))

        eventos()
        observer()
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

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.conductor?.let { conductor ->
                        binding.tituloTextField.setText(conductor.title)
                        binding.usuarioTextField.setText(conductor.userId.toString())
                        binding.cuerpoTextField.setText(conductor.body)
                    }
                    state.event?.let { event ->
                        if (event is UiEvent.PopBackStack) {
                            findNavController().navigateUp()
                        } else if (event is UiEvent.ShowSnackbar) {
                            Snackbar.make(binding.root, event.message, Toast.LENGTH_SHORT).show()
                        }
                        viewModel.handleIntent(JsonPlaceholderEditIntent.LimpiarMensaje)
                    }
                }
            }
        }
    }

    private fun obtainConductorFromInput(): JsonPlaceholderPost {
        val id = args.dni.toIntOrNull() ?: 0
        return JsonPlaceholderPost(
            binding.cuerpoTextField.text.toString(),
            id,
            binding.tituloTextField.text.toString(),
            binding.usuarioTextField.text.toString().toIntOrNull() ?: 0
        )
    }
}