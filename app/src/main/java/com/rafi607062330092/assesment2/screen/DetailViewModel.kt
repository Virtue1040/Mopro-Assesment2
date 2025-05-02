package com.rafi607062330092.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafi607062330092.assesment2.database.ResepDao
import com.rafi607062330092.assesment2.model.Resep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: ResepDao) : ViewModel() {
    fun insert(judul: String, kategori: String, bahan: ArrayList<String>, langkah: ArrayList<String>, tanggal: String) {
        val resep = Resep(
            judul       = judul,
            kategori    = kategori,
            bahan       = bahan,
            langkah     = langkah,
            tanggal     = tanggal
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(resep)
        }
    }

    fun update(id: Long, judul: String, kategori: String, bahan: ArrayList<String>, langkah: ArrayList<String>, tanggal: String) {
        val resep = Resep(
            id      = id,
            judul       = judul,
            kategori    = kategori,
            bahan       = bahan,
            langkah     = langkah,
            tanggal     = tanggal
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(resep)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    suspend fun getResep(id: Long): Resep? {
        return dao.getResepById(id)
    }
}