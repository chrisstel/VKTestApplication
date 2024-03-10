package com.example.vktestapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vktestapplication.R
import com.example.vktestapplication.databinding.FragmentMainBinding
import com.example.vktestapplication.ui.MainActivity
import com.example.vktestapplication.ui.adapters.ProductAdapter
import com.example.vktestapplication.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.vktestapplication.util.Resource
import com.example.vktestapplication.viewmodel.ProductViewModel

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    lateinit var viewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        productAdapter.setOnItemClickListener { product ->
            val bundle = Bundle().apply {
                putSerializable("product", product)
            }

            findNavController().navigate(
                R.id.action_mainFragment_to_productDetailsFragment,
                bundle
            )
        }

        viewModel.getLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { productResponse ->
                        productAdapter.differ.submitList(productResponse.products.toList())
                        val totalPages = productResponse.total / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.skip == totalPages

                        if (isLastPage) {
                            binding.recyclerViewProducts.setPadding(0, 0,0, 0)
                        }
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Error -> {
                    hideProgressBar()

                    response.message?.let { message ->
                        binding.recyclerViewProducts.visibility = View.INVISIBLE

                        binding.errorMessage.apply {
                            text = (context as MainActivity).getString(R.string.error_occurred, message)
                            visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage
                    && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible
                    && isScrolling

            if (shouldPaginate) {
                viewModel.getProducts()
                isScrolling = false
            }
        }


        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(activity as MainActivity)

        binding.recyclerViewProducts.apply {
            layoutManager = GridLayoutManager(activity as MainActivity, 2)
            adapter = productAdapter
            addOnScrollListener(this@MainFragment.scrollListener)
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }
}