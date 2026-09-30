package com.leon.tecsupfit.data

import android.content.Context
import android.graphics.*
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.File
import kotlinx.coroutines.*

class UsuarioManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)

    fun foto(): String? = prefs.getString("foto_uri", null)

    suspend fun guardarFoto(uri: Uri): String =
        withContext(Dispatchers.IO) {
            val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri).use {
                requireNotNull(it)
                BitmapFactory.decodeStream(it, null, limites)
            }
            require(limites.outWidth > 0 && limites.outHeight > 0) { "No se pudo leer la imagen." }
            var muestra = 1
            while (maxOf(limites.outWidth, limites.outHeight) / muestra > 1600) muestra *= 2
            val bitmap =
                context.contentResolver.openInputStream(uri).use {
                    requireNotNull(
                        BitmapFactory.decodeStream(
                            it,
                            null,
                            BitmapFactory.Options().apply { inSampleSize = muestra },
                        )
                    )
                }
            val orientacion =
                context.contentResolver.openInputStream(uri).use {
                    if (it != null)
                        ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                    else 1
                }
            val matrix =
                Matrix().apply {
                    when (orientacion) {
                        2 -> postScale(-1f, 1f)
                        3 -> postRotate(180f)
                        4 -> postScale(1f, -1f)
                        5 -> {
                            postRotate(90f)
                            postScale(-1f, 1f)
                        }
                        6 -> postRotate(90f)
                        7 -> {
                            postRotate(270f)
                            postScale(-1f, 1f)
                        }
                        8 -> postRotate(270f)
                    }
                }
            val corregida =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            val file = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")
            try {
                file.outputStream().use {
                    require(corregida.compress(Bitmap.CompressFormat.JPEG, 92, it))
                }
            } catch (e: Exception) {
                file.delete()
                throw e
            } finally {
                if (corregida != bitmap) corregida.recycle()
                bitmap.recycle()
            }
            val anterior = foto()
            val path = Uri.fromFile(file).toString()
            require(prefs.edit().putString("foto_uri", path).commit()) {
                "No se pudo guardar la foto."
            }
            anterior?.let {
                runCatching {
                    val f = File(Uri.parse(it).path ?: "")
                    if (f.parentFile == context.filesDir && f != file) f.delete()
                }
            }
            path
        }
}
