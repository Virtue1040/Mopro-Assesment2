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

    @Query("SELECT * FROM resep ORDER BY tanggal ASC")
    fun getResep(): Flow<List<Resep>>

    @Query("SELECT *  FROM resep WHERE id = :id")
    suspend fun getResepById(id: Long): Resep?

    @Query("DELETE FROM resep WHERE id = :id")
    suspend fun deleteById(id: Long)
}