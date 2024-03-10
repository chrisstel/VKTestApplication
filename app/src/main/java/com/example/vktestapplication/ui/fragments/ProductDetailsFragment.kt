package com.example.vktestapplication.ui.fragments

import android.animation.AnimatorInflater
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vktestapplication.R
import com.example.vktestapplication.databinding.FragmentProductDetailsBinding
import com.example.vktestapplication.ui.MainActivity
import com.example.vktestapplication.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class ProductDetailsFragment : Fragment() {
    private lateinit var viewModel: ProductViewModel
    private lateinit var binding: FragmentProductDetailsBinding
    val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val product = args.product

        binding.apply {
            Picasso.get().load(product.thumbnail).into(thumbnail)
            title.text = product.title
            price.text = getString(R.string.price, product.price)
            description.text = product.description
        }
    }
}