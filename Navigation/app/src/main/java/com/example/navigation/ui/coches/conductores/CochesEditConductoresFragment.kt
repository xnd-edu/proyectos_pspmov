package com.example.navigation.ui.coches.conductores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.R
import com.example.navigation.databinding.FragmentCochesEditConductoresBinding
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.coches.edit.CochesEditFragmentArgs
import com.example.navigation.ui.common.StringProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CochesEditConductoresFragment : Fragment() {
    private var _binding: FragmentCochesEditConductoresBinding? = null
    private val binding get() = _binding!!
    private lateinit var cochesConductoresAdapter: CochesConductoresAdapter
    private val viewModel: CochesEditConductoresViewModel by viewModels()
    private val args: CochesEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCochesEditConductoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val matricula = args.matricula
        viewModel.loadConductores(matricula)

        events()
        configureRecyclerView()
        observarState()
    }

    private fun configureRecyclerView() {
        cochesConductoresAdapter = CochesConductoresAdapter(
            actions = object : CochesConductoresAdapter.CochesConductoresActions {

                override fun onDeleteClick(conductor: Conductor) {
                    mostrarDialogoConfirmacionEliminar(conductor)
                }
            },
            stringProvider = StringProvider(requireContext())
        )

        binding.listaConductores.apply {
            adapter = cochesConductoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cochesConductoresAdapter.submitList(state.conductores)
        }
    }

    private fun events() {
        binding.buttonAnadir.setOnClickListener {
            mostrarSelectorConductores()
        }
    }

    private fun mostrarSelectorConductores() {
        val matricula = args.matricula
        val stringProvider = StringProvider(requireContext())

        viewModel.getConductoresDisponibles(matricula) { conductoresDisponibles ->
            if (conductoresDisponibles.isEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(stringProvider.getString(R.string.sin_conductores_disponibles))
                    .setMessage(stringProvider.getString(R.string.todos_los_conductores_asignados))
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
                return@getConductoresDisponibles
            }

            val items = conductoresDisponibles.map {
                stringProvider.getString(
                    R.string.conductor_nombre,
                    it.apellidos ?: "",
                    it.nombre ?: ""
                ) + " (${it.dni})"
            }.toTypedArray()

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(stringProvider.getString(R.string.seleccionar_conductor))
                .setItems(items) { dialog, which ->
                    val conductorSeleccionado = conductoresDisponibles[which]
                    viewModel.asignarConductor(matricula, conductorSeleccionado.dni)
                }
                .setNegativeButton(stringProvider.getString(R.string.cancelar), null)
                .show()
        }
    }

    private fun mostrarDialogoConfirmacionEliminar(conductor: Conductor) {
        val matricula = args.matricula
        val stringProvider = StringProvider(requireContext())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(stringProvider.getString(R.string.desasignar_conductor))
            .setMessage(
                stringProvider.getString(
                    R.string.confirmar_desasignar_conductor,
                    conductor.nombre ?: "",
                    conductor.apellidos ?: ""
                )
            )
            .setPositiveButton(stringProvider.getString(R.string.desasignar)) { dialog, which ->
                viewModel.desasignarConductor(matricula, conductor.dni)
            }
            .setNegativeButton(stringProvider.getString(R.string.cancelar), null)
            .show()
    }

    override fun onResume() {
        val matricula = args.matricula
        super.onResume()
        viewModel.loadConductores(matricula)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}