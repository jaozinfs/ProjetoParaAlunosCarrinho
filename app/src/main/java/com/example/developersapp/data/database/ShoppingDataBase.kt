package com.example.developersapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.developersapp.data.database.dao.ShoppingCartDao
import com.example.developersapp.domain.models.ProductItem

@Database(entities = [ProductItem::class], version = 1)
abstract class ShoppingDataBase : RoomDatabase() {
    abstract val shoppingDao: ShoppingCartDao

}