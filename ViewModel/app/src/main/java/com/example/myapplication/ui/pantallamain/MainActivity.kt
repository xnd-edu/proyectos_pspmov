package com.example.myapplication.ui.pantallamain

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R

import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.modelo.Coche
import com.example.myapplication.domain.usecases.coches.GetCoches
import com.example.myapplication.ui.common.MarginItemDecoration
import com.example.myapplication.ui.pantalladetalle.DetalleActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CocheAdapter
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            GetCoches(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        events()
        configureRecyclerView()
        observarState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCoches()
    }

    private fun observarState() {
        viewModel.state.observe(this@MainActivity) { state ->
            adapter.submitList(state.coches)
        }
    }

    private fun configureRecyclerView() {

        adapter = CocheAdapter(actions = object : CocheAdapter.CochesActions {
                override fun onItemClick(coche: Coche) {
                    navigateToDetail(coche.matricula ?: "")
                }

            })

        binding.listaCoches.layoutManager = LinearLayoutManager(this)

        binding.listaCoches.adapter = adapter

        binding.listaCoches.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.small_margin
                )
            )
        )
    }

    private fun navigateToDetail(matricula: String) {
        val intent = Intent(this, DetalleActivity::class.java)
        intent.putExtra("matricula", matricula)

        startActivity(intent)
    }

    private fun events() {

        binding.buttonAnadir.setOnClickListener {
            navigateToDetail("")

        }
    }
}