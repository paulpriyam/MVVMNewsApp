package com.androiddevs.mvvmnewsapp.data.local.dao

import androidx.room.*
import com.androiddevs.mvvmnewsapp.model.ArticleRemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<ArticleRemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE id=:id")
    fun getAllRemoteKey(id: Int): ArticleRemoteKeys

    @Query("DELETE FROM RemoteKeys")
    suspend fun deleteAllRemoteKeys()
}