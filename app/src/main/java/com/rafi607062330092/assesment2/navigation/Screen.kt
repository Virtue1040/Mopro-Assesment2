package com.rafi607062330092.assesment2.navigation

import com.rafi607062330092.assesment2.screen.KEY_ID_RESEP

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_RESEP}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}