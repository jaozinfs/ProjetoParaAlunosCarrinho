package com.example.developersapp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.developersapp.domain.ShoppingRepository
import com.example.developersapp.domain.models.ProductItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repositoryImpl: ShoppingRepository) : ViewModel() {


    val itemsQuantity = repositoryImpl.quantityInCart()
    val itemsInCart = repositoryImpl.itemsInCart()
    val state = MutableStateFlow<ShoppingState>(ShoppingState.IDLE)
    val items: Flow<List<ProductItem>> =
        itemsInCart.combine(repositoryImpl.getItems()) { cart, network ->
            val newList = mutableListOf<ProductItem>()
            network.forEach { networkProduct ->
                val itemFinded = cart.firstOrNull { it.id == networkProduct.id }
                if (itemFinded != null)
                    newList.add(itemFinded)
                else newList.add(networkProduct)
            }
            newList
        }.map {
            it.filter { productItem ->
                productItem.image.isNullOrEmpty().not()
            }
        }

    init {
        state.update {
            ShoppingState.Loading
        }

        viewModelScope.launch {
            items.collectLatest { data ->
                state.update {
                    ShoppingState.Success(data)
                }
            }
        }
        viewModelScope.launch {
            itemsQuantity.collectLatest { data ->
                state.update {
                    ShoppingState.UpdateQuantityInCart(data)
                }
            }
        }
    }

    fun increaseItemOnCart(productItem: ProductItem) = viewModelScope.launch {
        repositoryImpl.addToCart(productItem)
    }

    fun decreaseItemFromCart(productItem: ProductItem) = viewModelScope.launch {
        repositoryImpl.removeToCart(productItem)
    }

    sealed class ShoppingState {
        object IDLE : ShoppingState()
        object Loading : ShoppingState()
        data class Success(val list: List<ProductItem>) : ShoppingState()
        data class UpdateQuantityInCart(val quantity: Int) : ShoppingState()
    }
}
