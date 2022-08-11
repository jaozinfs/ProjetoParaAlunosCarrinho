package com.example.developersapp.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.developersapp.R
import com.example.developersapp.databinding.ItemProductBinding
import com.example.developersapp.domain.models.ProductItem
import com.example.developersapp.presentation.extensions.animateGone

class ShoppingItemsAdapter(
    val onIncreaseItem: (ProductItem) -> Unit,
    val onDecreaseItem: (ProductItem) -> Unit,
) :
    ListAdapter<ProductItem, ShoppingItemsAdapter.ProductItemsViewHolder>(ShoppingItemsDiffUtils) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemsViewHolder =
        ProductItemsViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ProductItemsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ProductItemsViewHolder(private val viewBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(productItem: ProductItem) = with(viewBinding) {
            productName.text = productItem.name
            productPrice.text = productItem.price.toString()
            productImage.load(productItem.image) {
                error(R.drawable.ic_launcher_background)
                memoryCacheKey(productItem.image)
            }
            cartIncreaseItem.setOnClickListener {
                onIncreaseItem.invoke(productItem)
            }
            cartDecreaseItem.run {
                if(productItem.quantity < 1)
                    animateGone()
                else
                    isVisible = true
                setOnClickListener {
                    onDecreaseItem.invoke(productItem)
                }
            }
            cartQuantity.run {
                isVisible = productItem.quantity >= 1
                text = productItem.quantity.toString()
            }
        }
    }
}

private object ShoppingItemsDiffUtils : DiffUtil.ItemCallback<ProductItem>() {
    override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
        oldItem == newItem
}
