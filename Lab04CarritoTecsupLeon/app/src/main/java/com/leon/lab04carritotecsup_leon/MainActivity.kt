package com.leon.lab04carritotecsup_leon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.leon.lab04carritotecsup_leon.ui.screens.PantallaCarrito
import com.leon.lab04carritotecsup_leon.ui.theme.Lab04CarritoTecsupLeonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Iconos blancos en la barra de estado (fondo morado)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            Lab04CarritoTecsupLeonTheme {
                PantallaCarrito()
            }
        }
    }
}
