package com.example.navigation.ui.conductores.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.navigation.R
import com.example.navigation.databinding.FragmentConductoresAddBinding
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
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
            fechaNacimientoTextField.isFocusable = false
            fechaNacimientoTextField.isClickable = true

            fechaNacimientoTextField.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(stringProvider.getString(R.string.fecha_de_nacimiento))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.addOnPositiveButtonClickListener { selection ->
                    val date = Date(selection)
                    val sdf = SimpleDateFormat(
                        stringProvider.getString(R.string.date_format_iso),
                        Locale.getDefault()
                    )
                    val fecha = sdf.format(date)
                    fechaNacimientoTextField.setText(fecha)
                }

                datePicker.show(parentFragmentManager, stringProvider.getString(R.string.date_picker_tag))
            }

            hechoButton.setOnClickListener {
                val conductor = obtainConductorFromInput()
                viewModel.addConductor(conductor)
            }
        }
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.conductor?.let { conductor ->
                binding.dniTextField.setText(conductor.dni)
                binding.nombreTextField.setText(conductor.nombre)
                binding.apellidosTextField.setText(conductor.apellidos)
                binding.telefonoTextField.setText(conductor.telefono)
                binding.fechaNacimientoTextField.setText(conductor.fechaNacimiento)
                when (conductor.genero) {
                    stringProvider.getString(R.string.masculino) -> binding.generoRadioGroup.check(R.id.masculinoRadio)
                    stringProvider.getString(R.string.femenino) -> binding.generoRadioGroup.check(R.id.femeninoRadio)
                    stringProvider.getString(R.string.otro) -> binding.generoRadioGroup.check(R.id.otroRadio)
                    else -> binding.generoRadioGroup.clearCheck()
                }
            }
            state.event?.let { event ->
                if (event is UiEvent.PopBackStack) {
                    findNavController().navigateUp()
                } else if (event is UiEvent.ShowSnackbar) {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                        .show()
                }
                viewModel.limpiarMensaje()
            }
        }
    }


    private fun obtainConductorFromInput(): Conductor {
        val genero = when (binding.generoRadioGroup.checkedRadioButtonId) {
            R.id.masculinoRadio -> stringProvider.getString(R.string.masculino)
            R.id.femeninoRadio -> stringProvider.getString(R.string.femenino)
            R.id.otroRadio -> stringProvider.getString(R.string.otro)
            else -> ""
        }

        return Conductor(
            binding.dniTextField.text.toString(),
            binding.nombreTextField.text.toString(),
            binding.apellidosTextField.text.toString(),
            binding.telefonoTextField.text.toString(),
            binding.fechaNacimientoTextField.text.toString(),
            genero
        )
    }
}