package com.example.navigation.ui.games.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigation.databinding.FragmentGamesMainBinding
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GamesMainFragment : Fragment() {

    private var _binding: FragmentGamesMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var gamesAdapter: GamesAdapter
    private val viewModel: GamesMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchBar()
        configureRecyclerView()
        observarState()
        observeEvents()
    }

    private fun setupSearchBar() {
        binding.searchEditText.setOnEditorActionListener { textView, actionId, _ ->
            when (actionId) {
                android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH -> {
                    textView.text.toString().let { query ->
                        onSearchQuery(query)
                        textView.clearFocus()
                        hideKeyboard(textView)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun configureRecyclerView() {
        gamesAdapter = GamesAdapter()

        binding.listaGames.apply {
            adapter = gamesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    gamesAdapter.submitList(state.games)
                    binding.circularProgressIndicator.isVisible = state.isLoading
                    binding.listaGames.isVisible = !state.isLoading
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiEvent.PopBackStack -> findNavController().navigateUp()
                        is UiEvent.ShowSnackbar -> Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModel.handleIntent(GamesMainIntent.SearchGames(query))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

