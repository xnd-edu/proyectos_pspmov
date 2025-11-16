package com.example.navigation.ui.conductores.coches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.R
import com.example.navigation.databinding.FragmentConductoresEditCochesBinding
import com.example.navigation.domain.model.Coche
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.ui.conductores.edit.ConductoresEditFragmentArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConductoresEditCochesFragment : Fragment() {
    private var _binding: FragmentConductoresEditCochesBinding? = null
    private val binding get() = _binding!!
    private lateinit var conductoresCochesAdapter: ConductoresCochesAdapter
    private val viewModel: ConductoresEditCochesViewModel by viewModels()
    private val args: ConductoresEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConductoresEditCochesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dni = args.dni
        viewModel.loadCoches(dni)

        events()
        configureRecyclerView()
        observarState()
    }

    private fun configureRecyclerView() {
        conductoresCochesAdapter = ConductoresCochesAdapter(
            actions = object : ConductoresCochesAdapter.ConductoresCochesActions {

                override fun onDeleteClick(coche: Coche) {
                    mostrarDialogoConfirmacionEliminar(coche)
                }
            },
            stringProvider = StringProvider(requireContext())
        )

        binding.listaCoches.apply {
            adapter = conductoresCochesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            conductoresCochesAdapter.submitList(state.coches)
        }
    }

    private fun events() {
        binding.buttonAnadir.setOnClickListener {
            mostrarSelectorCoches()
        }
    }

    private fun mostrarSelectorCoches() {
        val dni = args.dni
        val stringProvider = StringProvider(requireContext())

        viewModel.getCochesDisponibles(dni) { cochesDisponibles ->
            if (cochesDisponibles.isEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(stringProvider.getString(R.string.sin_coches_disponibles))
                    .setMessage(stringProvider.getString(R.string.todos_los_coches_asignados))
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
                return@getCochesDisponibles
            }

            val items = cochesDisponibles.map {
                stringProvider.getString(
                    R.string.coche_nombre,
                    it.marca ?: "",
                    it.modelo ?: ""
                ) + " (${it.matricula})"
            }.toTypedArray()

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(stringProvider.getString(R.string.seleccionar_coche))
                .setItems(items) { _, which ->
                    val cocheSeleccionado = cochesDisponibles[which]
                    viewModel.asignarCoche(dni, cocheSeleccionado.matricula)
                }
                .setNegativeButton(stringProvider.getString(R.string.cancelar), null)
                .show()
        }
    }

    private fun mostrarDialogoConfirmacionEliminar(coche: Coche) {
        val dni = args.dni
        val stringProvider = StringProvider(requireContext())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(stringProvider.getString(R.string.desasignar_coche))
            .setMessage(
                stringProvider.getString(
                    R.string.confirmar_desasignar_coche,
                    coche.marca ?: "",
                    coche.modelo ?: ""
                )
            )
            .setPositiveButton(stringProvider.getString(R.string.desasignar)) { _, _ ->
                viewModel.desasignarCoche(dni, coche.matricula)
            }
            .setNegativeButton(stringProvider.getString(R.string.cancelar), null)
            .show()
    }

    override fun onResume() {
        val dni = args.dni
        super.onResume()
        viewModel.loadCoches(dni)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

