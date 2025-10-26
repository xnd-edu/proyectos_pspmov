package com.example.myapplication.ui.pantalladetalle

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDetalleBinding
import com.example.myapplication.domain.modelo.Coche
import com.example.myapplication.ui.common.StringProvider
import com.example.myapplication.domain.usecases.coches.AddCocheUseCase
import com.example.myapplication.domain.usecases.coches.UpdateCocheUseCase
import com.example.myapplication.domain.usecases.coches.DeleteCocheUseCase
import com.example.myapplication.domain.usecases.coches.GetCoches
import com.example.myapplication.ui.common.UiEvent
import java.util.Calendar
import java.util.Locale

class DetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleBinding
    private val viewModel: DetalleViewModel by viewModels {
        DetalleViewModelFactory(
            StringProvider.instance(this),
            AddCocheUseCase(),
            UpdateCocheUseCase(),
            DeleteCocheUseCase(),
            GetCoches()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val matricula = intent.getStringExtra("matricula")
        if (!matricula.isNullOrEmpty()) {
            viewModel.getCoches(matricula)
        }

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
                    this@DetalleActivity,
                    { _, year, month, dayOfMonth ->
                        val fecha =
                            String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year)
                        fechaMatriculaTextField.setText(fecha)
                    },
                    ano, mes, dia
                )

                datePickerDialog.show()
            }

            eliminarButton.setOnClickListener {
                viewModel.state.value?.coche?.let { coche ->
                    viewModel.deleteCoche(coche)
                }
            }

            hechoButton.setOnClickListener {
                val coche = obtainCocheFromInput()
                viewModel.saveCoche(coche)
            }
        }
    }

    private fun observer() {
        viewModel.state.observe(this@DetalleActivity) { state ->
            binding.matriculaTextField.setText(state.coche.matricula)
            binding.electricoCheckbox.isChecked = state.coche.electrico == true
            binding.marcaTextField.setText(state.coche.marca)
            binding.modeloTextField.setText(state.coche.modelo)
            binding.fechaMatriculaTextField.setText(state.coche.fechaMatriculacion)
            binding.colorTextField.setText(state.coche.color)
            binding.comentariosTextField.setText(state.coche.comentarios)
            when (state.coche.tipo) {
                "Sedán" -> binding.tipoCocheRadioGroup.check(R.id.sedanRadio)
                "SUV" -> binding.tipoCocheRadioGroup.check(R.id.suvRadio)
                "otro" -> binding.tipoCocheRadioGroup.check(R.id.otroRadio)
                else -> binding.tipoCocheRadioGroup.clearCheck()
            }
            state.event?.let { event ->
                if (event is UiEvent.PopBackStack) {
                    this@DetalleActivity.finish()
                } else if (event is UiEvent.ShowSnackbar) {
                    Toast.makeText(this@DetalleActivity, event.message, Toast.LENGTH_SHORT)
                        .show()
                }
                viewModel.limpiarMensaje()
            }
        }
    }


    private fun obtainCocheFromInput(): Coche {
        val tipoCoche = when (binding.tipoCocheRadioGroup.checkedRadioButtonId) {
            R.id.sedanRadio -> "Sedán"
            R.id.suvRadio -> "SUV"
            R.id.otroRadio -> "otro"
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