package com.example.developersapp.data.api

import com.example.developersapp.domain.models.ProductItem
import retrofit2.http.GET

interface ShoppingApi {

    @GET("/api/v1/products.json")
    suspend fun getProductsItems(): List<ProductItem>
}