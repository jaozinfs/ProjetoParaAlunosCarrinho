package com.example.developersapp.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.developersapp.databinding.FragmentCartItemsBinding
import com.example.developersapp.domain.models.ProductItem
import com.example.developersapp.presentation.list.ShoppingViewModel
import com.example.developersapp.presentation.list.adapter.ShoppingItemsAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {
    private val shoppingItemsAdapter = ShoppingItemsAdapter(::onIncreaseItem, ::onDecreaseItem)
    private val shoppingViewModel: ShoppingViewModel by viewModel()
    private lateinit var fragmentCartBinding: FragmentCartItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCartBinding = FragmentCartItemsBinding.inflate(inflater, container, false)
        return fragmentCartBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        observeData()
    }

    private fun setupList() {
        with(fragmentCartBinding.productItemsRecyclerView) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = shoppingItemsAdapter
        }
    }

    private fun observeData() {
        shoppingViewModel.itemsInCart.flowWithLifecycle(lifecycle).onEach {
            updateList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateList(list: List<ProductItem>) {
        shoppingItemsAdapter.submitList(list)
    }

    private fun onIncreaseItem(productItem: ProductItem) {
        shoppingViewModel.increaseItemOnCart(productItem)
    }

    private fun onDecreaseItem(productItem: ProductItem) {
        shoppingViewModel.decreaseItemFromCart(productItem)
    }
}