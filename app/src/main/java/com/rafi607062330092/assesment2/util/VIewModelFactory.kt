package com.rafi0092.mobpro1.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafi0092.mobpro1.database.MahasiswaDao
import com.rafi0092.mobpro1.screen.DetailViewModel
import com.rafi0092.mobpro1.screen.MainViewModel

class VIewModelFactory(private val dao: MahasiswaDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}