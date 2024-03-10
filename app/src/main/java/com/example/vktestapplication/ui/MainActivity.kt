package com.example.vktestapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vktestapplication.R
import com.example.vktestapplication.databinding.ActivityMainBinding
import com.example.vktestapplication.model.repository.ProductRepository
import com.example.vktestapplication.ui.adapters.ProductAdapter
import com.example.vktestapplication.util.Resource
import com.example.vktestapplication.viewmodel.ProductViewModel
import com.example.vktestapplication.viewmodel.ProductViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ProductViewModelProviderFactory(application, ProductRepository()).create(ProductViewModel::class.java)
    }
}