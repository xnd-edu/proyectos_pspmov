package com.example.navigation.ui.jsonplaceholder.main

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
import com.example.navigation.databinding.FragmentPostsMainBinding
import com.example.navigation.domain.model.JsonPlaceholderPost
import com.example.navigation.ui.common.UiEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JsonPlaceholderMainFragment : Fragment() {

    private var _binding: FragmentPostsMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var postsAdapter: JsonPlaceholderAdapter
    private val viewModel: JsonPlaceholderMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events()
        configureRecyclerView()
        observarState()
        observeEvents()
    }

    private fun configureRecyclerView() {
        postsAdapter = JsonPlaceholderAdapter(
            actions = object : JsonPlaceholderAdapter.PostActions {
                override fun onItemClick(jsonPlaceholderPost: JsonPlaceholderPost) {
                    val action = JsonPlaceholderMainFragmentDirections.actionJsonPlaceholderMainFragmentToJsonPlaceholderEditFragment(
                        id = jsonPlaceholderPost.id
                    )
                    findNavController().navigate(action)
                }
            }
        )

        binding.listaPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observarState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    postsAdapter.submitList(state.posts)
                    binding.circularProgressIndicator.isVisible = state.isLoading
                    binding.listaPosts.isVisible = !state.isLoading
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackbar -> Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        is UiEvent.PopBackStack -> findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun events() {
        binding.buttonAnadir.setOnClickListener {
            val action = JsonPlaceholderMainFragmentDirections.actionJsonPlaceholderMainFragmentToJsonPlaceholderAddFragment()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleIntent(JsonPlaceholderMainIntent.LoadPosts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}