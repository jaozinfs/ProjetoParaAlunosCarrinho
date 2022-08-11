package com.example.developersapp.presentation.di

import androidx.room.Room
import com.example.developersapp.data.RepositoryConstants.BASE_URL
import com.example.developersapp.data.ShoppingRepositoryImpl
import com.example.developersapp.data.api.ShoppingApi
import com.example.developersapp.data.database.ShoppingDataBase
import com.example.developersapp.data.retrofit.RetrofitBuilder
import com.example.developersapp.domain.ShoppingRepository
import com.example.developersapp.presentation.list.ShoppingViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val allModules = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ShoppingDataBase::class.java,
            "ShoppingDataBase"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<ShoppingDataBase>().shoppingDao }
    single { RetrofitBuilder.build(BASE_URL, ShoppingApi::class.java) }
    single<ShoppingRepository> { ShoppingRepositoryImpl(get(), get()) }
    viewModel<ShoppingViewModel>()
}