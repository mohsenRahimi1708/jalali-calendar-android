package com.example.jalicalendar.domain.repo

import com.example.jalicalendar.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccounts(): Flow<List<Account>>
    suspend fun getAccount(id: String): Account?
    suspend fun primaryAccount(): Account?
    suspend fun upsert(account: Account)
    suspend fun remove(id: String)
}
