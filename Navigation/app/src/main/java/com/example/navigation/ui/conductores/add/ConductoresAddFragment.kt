package com.example.navigation.ui.conductores.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.navigation.R
import com.example.navigation.databinding.FragmentConductoresAddBinding
import com.example.navigation.domain.model.Conductor
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ConductoresAddFragment : Fragment() {
    private var _binding: FragmentConductoresAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConductoresAddViewModel by viewModels()

    @Inject
    lateinit var stringProvider: StringProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConductoresAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventos()
        observer()
    }
    private fun eventos() {
        with(binding) {
            hechoButton.setOnClickListener {
                val conductor = obtainConductorFromInput()
                viewModel.handleIntent(JsonPlaceholderAddIntent.AddConductor(conductor))
            }
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.conductor?.let { conductor ->
                        binding.tituloTextField.setText(conductor.title)
                        binding.usuarioTextField.setText(conductor.userId)
                        binding.cuerpoTextField.setText(conductor.body)
                    }
                    state.event?.let { event ->
                        if (event is UiEvent.PopBackStack) {
                            findNavController().navigateUp()
                        } else if (event is UiEvent.ShowSnackbar) {
                            Snackbar.make(binding.root, event.message, Toast.LENGTH_SHORT).show()
                        }
                        viewModel.handleIntent(JsonPlaceholderAddIntent.LimpiarMensaje)
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