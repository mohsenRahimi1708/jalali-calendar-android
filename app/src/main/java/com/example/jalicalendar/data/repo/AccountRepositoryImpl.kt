package com.example.jalicalendar.data.repo

import com.example.jalicalendar.data.local.dao.AccountDao
import com.example.jalicalendar.data.local.toDomain
import com.example.jalicalendar.data.local.toEntity
import com.example.jalicalendar.domain.model.Account
import com.example.jalicalendar.domain.repo.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
) : AccountRepository {

    override fun observeAccounts(): Flow<List<Account>> =
        accountDao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override suspend fun getAccount(id: String): Account? = accountDao.getById(id)?.toDomain()

    override suspend fun primaryAccount(): Account? = accountDao.primary()?.toDomain()

    override suspend fun upsert(account: Account) = accountDao.upsert(account.toEntity())

    override suspend fun remove(id: String) = accountDao.delete(id)
}
