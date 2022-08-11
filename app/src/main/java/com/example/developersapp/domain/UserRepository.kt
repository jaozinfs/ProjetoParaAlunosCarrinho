package com.example.developersapp.domain

interface UserRepository {
    suspend fun saveUser()
}