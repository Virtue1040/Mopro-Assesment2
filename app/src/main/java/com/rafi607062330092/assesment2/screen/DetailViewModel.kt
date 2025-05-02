package com.rafi607062330092.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafi607062330092.assesment2.database.ResepDao
import com.rafi607062330092.assesment2.model.Resep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: ResepDao) : ViewModel() {
    fun insert(nama: String, nim: String, kelas: String) {
        val resep = Resep(
            nama    = nama,
            nim     = nim,
            kelas   = kelas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(resep)
        }
    }

    fun update(id: Long, nama: String, nim: String, kelas: String) {
        val resep = Resep(
            id      = id,
            nama    = nama,
            nim     = nim,
            kelas   = kelas
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

    suspend fun getMahasiswa(id: Long): Resep? {
        return dao.getResepById(id)
    }
}