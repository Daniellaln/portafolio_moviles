package com.leon.tecsupfit.ui.components

import android.database.ContentObserver
import android.os.*
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

val LocalMovimientoReducido = staticCompositionLocalOf { false }

@Composable
fun recordarMovimientoReducido(): Boolean {
    val c = LocalContext.current
    fun leer() =
        Settings.Global.getFloat(c.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) ==
            0f
    var reducido by remember { mutableStateOf(leer()) }
    DisposableEffect(c) {
        val o =
            object : ContentObserver(Handler(Looper.getMainLooper())) {
                override fun onChange(selfChange: Boolean) {
                    reducido = leer()
                }
            }
        c.contentResolver.registerContentObserver(
            Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE),
            false,
            o,
        )
        onDispose { c.contentResolver.unregisterContentObserver(o) }
    }
    return reducido
}
