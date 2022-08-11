package com.example.developersapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ProductItem(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val brand: String,
    val name: String,
    @SerializedName("image_link")
    val image: String?,
    val price: Double,
    val quantity: Int = 0
)