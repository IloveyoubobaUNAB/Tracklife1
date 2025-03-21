package com.jpalomino502.vivebien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jpalomino502.vivebien.navigation.NavGraph
import com.jpalomino502.vivebien.ui.theme.VivebienTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VivebienTheme {
                NavGraph()
            }
        }
    }
}
