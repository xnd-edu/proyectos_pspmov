package com.example.navigation.ui.conductores.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.databinding.FragmentConductoresMainBinding
import com.example.navigation.domain.model.Conductor
import com.example.navigation.ui.common.StringProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConductoresMainFragment : Fragment() {

    private var _binding: FragmentConductoresMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var conductoresAdapter: ConductoresAdapter
    private val viewModel: ConductoresMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConductoresMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events()
        configureRecyclerView()
        observarState()
    }

    private fun configureRecyclerView() {
        conductoresAdapter = ConductoresAdapter(
            actions = object : ConductoresAdapter.ConductoresActions {
                override fun onItemClick(conductor: Conductor) {
                    val action = ConductoresMainFragmentDirections.actionConductoresMainFragmentToConductoresEditFragment(
                        dni = conductor.dni
                    )
                    findNavController().navigate(action)
                }
            },
            stringProvider = StringProvider(requireContext())
        )

        binding.listaConductores.apply {
            adapter = conductoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            conductoresAdapter.submitList(state.conductores)
        }
    }

    private fun events() {
        binding.buttonAnadir.setOnClickListener {
            val action = ConductoresMainFragmentDirections.actionConductoresMainFragmentToConductoresAddFragment()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadConductores()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}