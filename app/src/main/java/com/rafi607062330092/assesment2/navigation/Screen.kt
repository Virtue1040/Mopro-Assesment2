package com.rafi607062330092.assesment2.navigation

import com.rafi607062330092.assesment2.screen.KEY_ID_RESEP

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("editScreen")
    data object FormUbah: Screen("editScreen/{$KEY_ID_RESEP}") {
        fun withId(id: Long) = "editScreen/$id"
    }
    data object FormDetail: Screen("detailScreen/{$KEY_ID_RESEP}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}