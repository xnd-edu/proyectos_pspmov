package com.example.navigation.ui.coches.cochesnew

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.navigation.R
import com.example.navigation.databinding.FragmentCochesNewBinding
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.common.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class CochesNewFragment : Fragment() {

    private var _binding: FragmentCochesNewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CochesNewViewModel by viewModels()

    @Inject
    lateinit var stringProvider: StringProvider


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCochesNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventos()
        observer()
    }
    private fun eventos() {
        with(binding) {
            fechaMatriculaTextField.isFocusable = false
            fechaMatriculaTextField.isClickable = true

            fechaMatriculaTextField.setOnClickListener {
                val calendario = Calendar.getInstance()
                val ano = calendario.get(Calendar.YEAR)
                val mes = calendario.get(Calendar.MONTH)
                val dia = calendario.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        val fecha = stringProvider.getString(
                            R.string.date_format,
                            dayOfMonth,
                            month + 1,
                            year
                        )
                        fechaMatriculaTextField.setText(fecha)
                    },
                    ano, mes, dia
                )

                datePickerDialog.show()
            }

            hechoButton.setOnClickListener {
                val coche = obtainCocheFromInput()
                viewModel.addCoche(coche)
            }
        }
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.matriculaTextField.setText(state.coche.matricula)
            binding.electricoCheckbox.isChecked = state.coche.electrico == true
            binding.marcaTextField.setText(state.coche.marca)
            binding.modeloTextField.setText(state.coche.modelo)
            binding.fechaMatriculaTextField.setText(state.coche.fechaMatriculacion)
            binding.colorTextField.setText(state.coche.color)
            binding.comentariosTextField.setText(state.coche.comentarios)
            when (state.coche.tipo) {
                stringProvider.getString(R.string.sedan) -> binding.tipoCocheRadioGroup.check(R.id.sedanRadio)
                stringProvider.getString(R.string.suv) -> binding.tipoCocheRadioGroup.check(R.id.suvRadio)
                stringProvider.getString(R.string.otro) -> binding.tipoCocheRadioGroup.check(R.id.otroRadio)
                else -> binding.tipoCocheRadioGroup.clearCheck()
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