package com.leon.tecsupfit

import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
import com.leon.tecsupfit.navigation.TecsupFitNavGraph
import com.leon.tecsupfit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}
