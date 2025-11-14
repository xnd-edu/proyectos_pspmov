package com.example.navigation.ui.coches.cochesmain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.databinding.FragmentCochesMainBinding
import com.example.navigation.ui.common.StringProvider
import com.example.navigation.domain.model.Coche
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CochesMainFragment : Fragment() {

    private var _binding: FragmentCochesMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var cochesAdapter: CochesAdapter
    private val viewModel: CochesMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCochesMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events()
        configureRecyclerView()
        observarState()
    }

    private fun configureRecyclerView() {
        cochesAdapter = CochesAdapter(
            actions = object : CochesAdapter.CochesActions {
                override fun onItemClick(coche: Coche) {
//                    navigateToDetail(coche.matricula ?: "")
                }
            },
            stringProvider = StringProvider(requireContext())
        )

        binding.listaCoches.apply {
            adapter = cochesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            cochesAdapter.submitList(state.coches)
        }
    }

    private fun events() {
        binding.buttonAnadir.setOnClickListener {
//            navigateToDetail("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

