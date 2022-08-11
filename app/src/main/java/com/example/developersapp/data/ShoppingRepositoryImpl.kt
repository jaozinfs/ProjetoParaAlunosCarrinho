package com.example.developersapp.data

import android.database.sqlite.SQLiteConstraintException
import com.example.developersapp.data.api.ShoppingApi
import com.example.developersapp.data.database.dao.ShoppingCartDao
import com.example.developersapp.domain.ShoppingRepository
import com.example.developersapp.domain.models.ProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ShoppingRepositoryImpl(
    private val shoppingApi: ShoppingApi,
    private val shoppingCartDao: ShoppingCartDao
) : ShoppingRepository {


    override suspend fun favoriteItem(productItem: ProductItem) {}

    override suspend fun unFavoriteItem(productItem: ProductItem) {}

    override fun getItems(): Flow<List<ProductItem>> = flow {
        emit(shoppingApi.getProductsItems())
    }

    override suspend fun addToCart(productItem: ProductItem) {
        try {
            shoppingCartDao.addToCar(productItem.copy(quantity = 1))
        } catch (constraintException: SQLiteConstraintException) {
            val item = shoppingCartDao.getProduct(productItem.id)
            shoppingCartDao.updateItemInCar(item.quantity + 1, productItem.id)
        }
    }

    override suspend fun removeToCart(productItem: ProductItem) {
        val item = shoppingCartDao.getProduct(productItem.id)
        shoppingCartDao.updateItemInCar(item.quantity + 1, productItem.id)
        if(item.quantity <= 1)
            shoppingCartDao.removeFromCart(item)
        else
            shoppingCartDao.updateItemInCar(item.quantity - 1, productItem.id)
    }

    override fun itemsInCart(): Flow<List<ProductItem>> = shoppingCartDao.getItemsOnCart()

    override fun quantityInCart(): Flow<Int> = shoppingCartDao.getItemsOnCart().map {
        it.size
    }
}