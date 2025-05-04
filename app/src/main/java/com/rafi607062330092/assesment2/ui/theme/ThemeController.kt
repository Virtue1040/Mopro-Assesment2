package com.rafi607062330092.assesment2.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.rafi607062330092.assesment2.ui.theme.normal.DefaultTheme
import com.rafi607062330092.assesment2.ui.theme.red.RedTheme
import com.rafi607062330092.assesment2.util.SettingsDataStore

@Composable
fun ThemeController(
    content: @Composable () -> Unit
) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val theme by dataStore.themeFlow.collectAsState(true)

    when (theme) {
        true -> {
            DefaultTheme {
                content()
            }
        }
        false -> {
            RedTheme {
                content()
            }
        }
    }
}