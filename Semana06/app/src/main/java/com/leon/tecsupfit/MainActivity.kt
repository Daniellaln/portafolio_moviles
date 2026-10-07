package com.leon.tecsupfit

import android.os.Bundle
<<<<<<< HEAD
import androidx.activity.*
import androidx.activity.compose.setContent
=======
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
>>>>>>> sinia
import com.leon.tecsupfit.navigation.TecsupFitNavGraph
import com.leon.tecsupfit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ),
            navigationBarStyle =
                SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ),
        )
        setContent { TecsupFitTheme { TecsupFitNavGraph() } }
=======
        enableEdgeToEdge()
        setContent {
            TecsupFitTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    TecsupFitNavGraph(navController = navController)
                }
            }
        }
>>>>>>> sinia
    }
}
