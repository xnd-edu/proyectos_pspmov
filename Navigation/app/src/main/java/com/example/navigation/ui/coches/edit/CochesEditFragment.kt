package com.example.navigation.ui.coches.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.navigation.R
import com.example.navigation.databinding.FragmentCochesEditBinding
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class CochesEditFragment : Fragment() {

    private var _binding: FragmentCochesEditBinding? = null
    private val binding get() = _binding!!
    private val args: CochesEditFragmentArgs by navArgs()
    private val viewModel: CochesEditViewModel by viewModels()

    @Inject
    lateinit var stringProvider: StringProvider


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCochesEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val matricula = args.matricula
        viewModel.loadCoche(matricula)

        eventos()
        observer()
    }
    private fun eventos() {
        with(binding) {
            fechaMatriculaTextField.isFocusable = false
            fechaMatriculaTextField.isClickable = true

            fechaMatriculaTextField.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(stringProvider.getString(R.string.fecha_matricula))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.addOnPositiveButtonClickListener { selection ->
                    val date = Date(selection)
                    val sdf = SimpleDateFormat(
                        stringProvider.getString(R.string.date_format_iso),
                        Locale.getDefault()
                    )
                    val fecha = sdf.format(date)
                    fechaMatriculaTextField.setText(fecha)
                }

                datePicker.show(parentFragmentManager, stringProvider.getString(R.string.date_picker_tag))
            }

            eliminarButton.setOnClickListener {
                viewModel.state.value?.coche?.let { coche ->
                    viewModel.deleteCoche(coche)
                }
            }

            conductoresCard.setOnClickListener {
                val action = CochesEditFragmentDirections.actionCochesEditFragmentToCochesEditConductoresFragment(
                    matricula = args.matricula
                )
                findNavController().navigate(action)
            }

            hechoButton.setOnClickListener {
                val coche = obtainCocheFromInput()
                viewModel.saveCoche(coche)
            }
        }
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.coche?.let { coche ->
                binding.matriculaTextField.setText(coche.matricula)
                binding.electricoCheckbox.isChecked = coche.electrico == true
                binding.marcaTextField.setText(coche.marca)
                binding.modeloTextField.setText(coche.modelo)
                binding.fechaMatriculaTextField.setText(coche.fechaMatriculacion)
                binding.colorTextField.setText(coche.color)
                binding.comentariosTextField.setText(coche.comentarios)
                when (coche.tipo) {
                    stringProvider.getString(R.string.sedan) -> binding.tipoCocheRadioGroup.check(R.id.sedanRadio)
                    stringProvider.getString(R.string.suv) -> binding.tipoCocheRadioGroup.check(R.id.suvRadio)
                    stringProvider.getString(R.string.otro) -> binding.tipoCocheRadioGroup.check(R.id.otroRadio)
                    else -> binding.tipoCocheRadioGroup.clearCheck()
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


    private fun obtainCocheFromInput(): Coche {
        val tipoCoche = when (binding.tipoCocheRadioGroup.checkedRadioButtonId) {
            R.id.sedanRadio -> stringProvider.getString(R.string.sedan)
            R.id.suvRadio -> stringProvider.getString(R.string.suv)
            R.id.otroRadio -> stringProvider.getString(R.string.otro)
            else -> ""
        }

        return Coche(
            binding.matriculaTextField.text.toString(),
            binding.marcaTextField.text.toString(),
            binding.modeloTextField.text.toString(),
            binding.electricoCheckbox.isChecked,
            binding.fechaMatriculaTextField.text.toString(),
            binding.colorTextField.text.toString(),
            tipoCoche,
            binding.comentariosTextField.text.toString()
        )
    }
}