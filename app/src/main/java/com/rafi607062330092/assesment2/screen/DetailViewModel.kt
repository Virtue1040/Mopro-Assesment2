package com.rafi607062330092.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafi607062330092.assesment2.database.ResepDao
import com.rafi607062330092.assesment2.model.Resep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: ResepDao) : ViewModel() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(judul: String, kategori: String, bahan: List<String>, langkah: String) {
        val resep = Resep(
            judul = judul,
            kategori = kategori,
            bahan = bahan,
            langkah = langkah,
            tanggal = formatter.format(Date())
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(resep)
        }
    }

    fun update(id: Long, judul: String, kategori: String, bahan: List<String>, langkah: String) {
        val resep = Resep(
            id = id,
            judul = judul,
            kategori = kategori,
            bahan = bahan,
            langkah = langkah,
            tanggal = formatter.format(Date())
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