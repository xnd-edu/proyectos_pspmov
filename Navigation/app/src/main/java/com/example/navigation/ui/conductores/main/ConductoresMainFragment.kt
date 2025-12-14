package com.example.navigation.ui.conductores.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.databinding.FragmentConductoresMainBinding
import com.example.navigation.domain.model.JsonPlaceholderPost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                override fun onItemClick(jsonPlaceholderPost: JsonPlaceholderPost) {
                    val action = ConductoresMainFragmentDirections.actionConductoresMainFragmentToConductoresEditFragment(
                        dni = jsonPlaceholderPost.id.toString()
                    )
                    findNavController().navigate(action)
                }
            }
        )

        binding.listaConductores.apply {
            adapter = conductoresAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    conductoresAdapter.submitList(state.conductores)
                }
            }
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
        viewModel.handleIntent(JsonPlaceholderMainIntent.LoadConductores)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}