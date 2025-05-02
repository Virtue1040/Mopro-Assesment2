package com.rafi607062330092.assesment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resep")
data class Resep(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val kategori: String,
    val bahan: ArrayList<String>,
    val langkah: ArrayList<String>,
    val tanggal: String
)