package com.rafi607062330092.assesment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rafi607062330092.assesment2.navigation.SetupNavGraph
import com.rafi607062330092.assesment2.ui.theme.ThemeController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThemeController{
                SetupNavGraph()
            }
        }
    }
}