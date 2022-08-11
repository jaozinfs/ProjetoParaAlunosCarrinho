package com.example.developersapp.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.developersapp.databinding.FragmentShoppingItemsBinding
import com.example.developersapp.domain.models.ProductItem
import com.example.developersapp.presentation.list.adapter.ShoppingItemsAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingItemsFragment : Fragment() {
    private lateinit var fragmentSHoppingBinding: FragmentShoppingItemsBinding
    private val shoppingItemsAdapter: ShoppingItemsAdapter =
        ShoppingItemsAdapter(::onIncreaseItem, ::onDecreaseItem)
    private val shoppingViewModel: ShoppingViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSHoppingBinding = FragmentShoppingItemsBinding.inflate(inflater, container, false)
        return fragmentSHoppingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupListener()
        observeData()
    }

    private fun setupListener() {
        fragmentSHoppingBinding.floatingActionButton.setOnClickListener {
            findNavController().navigate(ShoppingItemsFragmentDirections.actionShoppingItemsFragmentToCartFragment())
        }
    }

    private fun setupList() {
        with(fragmentSHoppingBinding.productItemsRecyclerView) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = shoppingItemsAdapter
        }
    }

    private fun observeData() {
//        shoppingViewModel.items.flowWithLifecycle(lifecycle)
//            .onStart {
//                fragmentSHoppingBinding.shimmerEffect.run {
//                    isVisible = true
//                    startShimmer()
//                }
//            }.onEach {
//                updateList(it)
//                fragmentSHoppingBinding.shimmerEffect.run {
//                    isVisible = false
//                    stopShimmer()
//                }
//            }.launchIn(viewLifecycleOwner.lifecycleScope)
//
//        shoppingViewModel.itemsQuantity.flowWithLifecycle(lifecycle).onEach {
//            updateQuantityInCart(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)

        shoppingViewModel.state.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                ShoppingViewModel.ShoppingState.IDLE -> return@onEach
                ShoppingViewModel.ShoppingState.Loading -> {
                    fragmentSHoppingBinding.shimmerEffect.run {
                        isVisible = true
                        startShimmer()
                    }
                }
                is ShoppingViewModel.ShoppingState.Success -> {
                    fragmentSHoppingBinding.shimmerEffect.run {
                        isVisible = false
                        stopShimmer()
                    }
                    updateList(it.list)
                }
                is ShoppingViewModel.ShoppingState.UpdateQuantityInCart -> updateQuantityInCart(it.quantity)
            }
        }.launchIn(lifecycleScope)
    }

    private fun updateQuantityInCart(newQuantity: Int) {
        fragmentSHoppingBinding.cartQuantity.text = newQuantity.toString()
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