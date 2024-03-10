package com.example.vktestapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vktestapplication.R
import com.example.vktestapplication.databinding.ItemProductBinding
import com.example.vktestapplication.model.data.Product
import com.example.vktestapplication.model.data.ProductResponse
import com.squareup.picasso.Picasso

class ProductAdapter(
    private val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)

        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(product.thumbnail).into(thumbnail)
            title.text = product.title
            price.text = context.getString(R.string.price, product.price)
            root.setOnClickListener {
                onItemClickListener?.let { listener ->
                    listener(product)
                }
            }
        }


    }

    inner class ProductViewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root)
}