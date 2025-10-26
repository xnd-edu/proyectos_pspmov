package com.example.myapplication.ui.pantallamain

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.ui.pantallamain.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.modelo.Coche
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventos()
        observer()
    }

    private fun eventos() {
        binding.fechaMatriculaTextField.isFocusable = false
        binding.fechaMatriculaTextField.isClickable = true

        binding.fechaMatriculaTextField.setOnClickListener {
            val calendario = Calendar.getInstance()
            val ano = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val fecha =
                        String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    binding.fechaMatriculaTextField.setText(fecha)
                },
                ano, mes, dia
            )

            datePickerDialog.show()
        }

        binding.buttonLimpiar.setOnClickListener {
            viewModel.limpiarCoche()
        }

        binding.buttonActualizar.setOnClickListener {
            val pagina = binding.indiceTexto.text.toString()[0]
            if (pagina.code != 0) {
                val coche = obtainCocheFromInput()
                viewModel.actualizarCoche(coche)
            }
        }

        binding.buttonEliminar.setOnClickListener {
            val pagina = binding.indiceTexto.text.toString()[0]
            if (pagina.code != 0)
                viewModel.deleteCoche()
        }

        binding.buttonGuardar.setOnClickListener {
            val coche = obtainCocheFromInput()
            viewModel.saveCoche(coche)
        }

        binding.anteriorComentario.setOnClickListener {
            viewModel.irCocheAnterior()
        }

        binding.siguienteComentario.setOnClickListener {
            viewModel.irCocheSiguiente()
        }
    }

    private fun observer() {
        viewModel.state.observe(this) { state ->
            binding.matriculaTextField.setText(state.coche.matricula)
            binding.electricoCheckbox.isChecked = state.coche.electrico == true
            binding.marcaTextField.setText(state.coche.marca)
            binding.modeloTextField.setText(state.coche.modelo)
            binding.fechaMatriculaTextField.setText(state.coche.fechaMatriculacion)
            binding.colorTextField.setText(state.coche.color)
            when (state.coche.tipo) {
                "Sedán" -> binding.sedanRadio.isChecked = true
                "SUV" -> binding.suvRadio.isChecked = true
                "otro" -> binding.otroRadio.isChecked = true
                else -> binding.tipoCocheRadioGroup.clearCheck()
            }
            binding.comentariosTextField.setText(state.coche.comentarios)
            val indice = viewModel.state.value?.indiceCoche ?: 0
            val size = viewModel.state.value?.sizeList ?: 0
            val string = "$indice/$size"
            binding.indiceTexto.text = string
            state.mensaje?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
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