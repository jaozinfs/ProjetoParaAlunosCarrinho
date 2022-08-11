package com.example.developersapp.domain

import com.example.developersapp.domain.models.ProductItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    suspend fun favoriteItem(productItem: ProductItem)
    suspend fun unFavoriteItem(productItem: ProductItem)
    fun getItems(): Flow<List<ProductItem>>

    suspend fun addToCart(productItem: ProductItem)
    suspend fun removeToCart(productItem: ProductItem)
    fun itemsInCart(): Flow<List<ProductItem>>
    fun quantityInCart(): Flow<Int>
}