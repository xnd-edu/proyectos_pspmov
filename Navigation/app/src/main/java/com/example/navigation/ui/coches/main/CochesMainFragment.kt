package com.example.navigation.ui.coches.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.databinding.FragmentCochesMainBinding
import com.example.navigation.ui.SearchableFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CochesMainFragment : Fragment(), SearchableFragment {

    private var _binding: FragmentCochesMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var gamesAdapter: GamesAdapter
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

        configureRecyclerView()
        observarState()
    }

    private fun configureRecyclerView() {
        gamesAdapter = GamesAdapter()

        binding.listaCoches.apply {
            adapter = gamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    gamesAdapter.submitList(state.coches)
                }
            }
        }
    }

    override fun onSearchQuery(query: String) {
        viewModel.handleIntent(GameMainIntent.SearchGames(query))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

