package com.example.developersapp.data.database.dao

import androidx.room.*
import com.example.developersapp.domain.models.ProductItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {
    @Insert
    suspend fun addToCar(productItem: ProductItem)

    @Query("UPDATE productitem SET quantity = :newQuantity WHERE id = :id")
    suspend fun updateItemInCar(newQuantity: Int, id: Int)

    @Delete
    suspend fun removeFromCart(productItem: ProductItem)

    @Query("SELECT * from productitem")
    fun getItemsOnCart(): Flow<List<ProductItem>>

    @Query("SELECT * from productitem WHERE id = :productItemId")
    suspend fun getProduct(productItemId: Int): ProductItem
}