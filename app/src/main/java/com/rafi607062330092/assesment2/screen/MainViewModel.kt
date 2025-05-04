package com.rafi607062330092.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafi607062330092.assesment2.database.ResepDao
import com.rafi607062330092.assesment2.model.Resep
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: ResepDao) : ViewModel() {
    val data: StateFlow<List<Resep>> = dao.getResep().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
    val dataDeleted: StateFlow<List<Resep>> = dao.getResepDeleted().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}