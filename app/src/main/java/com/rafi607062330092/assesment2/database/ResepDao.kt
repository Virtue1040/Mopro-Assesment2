package com.rafi607062330092.assesment2.database

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao
import com.rafi607062330092.assesment2.model.Resep
import kotlinx.coroutines.flow.Flow

@Dao
interface ResepDao {

    @Insert
    suspend fun insert(resep: Resep)

    @Update
    suspend fun update(resep: Resep)

    @Query("SELECT * FROM resep WHERE isDelete = 0 ORDER BY kategori ASC")
    fun getResep(): Flow<List<Resep>>

    @Query("SELECT * FROM resep WHERE isDelete = 1 ORDER BY kategori ASC")
    fun getResepDeleted(): Flow<List<Resep>>

    @Query("SELECT DISTINCT kategori FROM resep ORDER BY kategori ASC")
    fun getCategory(): Flow<List<String>>

    @Query("SELECT *  FROM resep WHERE id = :id")
    suspend fun getResepById(id: Long): Resep?

    @Query("UPDATE resep SET isDelete = 1 WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM resep WHERE isDelete = 1")
    suspend fun hardDeleteAll()

    @Query("DELETE FROM resep WHERE id = :id AND isDelete = 1")
    suspend fun hardDeleteById(id: Long)

    @Query("UPDATE resep SET isDelete = 0 WHERE id = :id")
    suspend fun undoById(id: Long)

    @Query("UPDATE resep Set isDelete = 0")
    suspend fun undoAll()
}