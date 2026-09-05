package pe.edu.tecsup.registronotas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.tecsup.registronotas.logic.CalculadoraNotas
import pe.edu.tecsup.registronotas.logic.ResultadoNotas
import pe.edu.tecsup.registronotas.model.CURSOS
import pe.edu.tecsup.registronotas.ui.components.FilaCurso
import pe.edu.tecsup.registronotas.ui.components.TarjetaResultado
import pe.edu.tecsup.registronotas.ui.theme.FondoDegradadoBottom
import pe.edu.tecsup.registronotas.ui.theme.FondoDegradadoTop
import pe.edu.tecsup.registronotas.ui.theme.MoradoDeshabilitado
import pe.edu.tecsup.registronotas.ui.theme.MoradoPrimario
import pe.edu.tecsup.registronotas.ui.theme.RegistroNotasTheme
import pe.edu.tecsup.registronotas.ui.theme.TextoDeshabilitado
import pe.edu.tecsup.registronotas.ui.theme.TextoPrincipal
import pe.edu.tecsup.registronotas.ui.theme.TextoSecundario
import pe.edu.tecsup.registronotas.ui.theme.VerdeConfirmacion

/**
 * >>> CAMBIA AQUI TU NOMBRE COMPLETO ANTES DE ENTREGAR <<<
 * Es el texto que aparece en el pie de la pantalla.
 */
private const val NOMBRE_DESARROLLADOR = "(tu nombre completo)"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen() {

    // --- ESTADO ---------------------------------------------------------
    // Cada curso tiene SU propia variable de estado (cuatro remember).
    // El Slider guarda Float, por eso se inicializan en 0f.
    var notaFundamentos by remember { mutableStateOf(0f) }
    var notaPoo by remember { mutableStateOf(0f) }
    var notaMoviles by remember { mutableStateOf(0f) }
    var notaBaseDatos by remember { mutableStateOf(0f) }

    // Switch y Checkbox usan el mismo duo (checked / onCheckedChange) pero
    // con Boolean en lugar de Float.
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    // Mientras sea null se muestra el mensaje gris; al calcular guarda la
    // tarjeta de resultados.
    var resultado by remember { mutableStateOf<ResultadoNotas?>(null) }

    // Si el usuario cambia una nota o el Switch despues de calcular, el
    // resultado mostrado quedaria desactualizado: se oculta y hay que volver
    // a pulsar CALCULAR PROMEDIO.
    fun invalidarResultado() {
        resultado = null
    }

    /** Reto opcional: LIMPIAR deja la pantalla como en la Figura 1. */
    fun limpiar() {
        notaFundamentos = 0f
        notaPoo = 0f
        notaMoviles = 0f
        notaBaseDatos = 0f
        redondear = false
        confirmado = false
        resultado = null
    }

    // --- UI -------------------------------------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registro de Notas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoradoPrimario
                )
            )
        },
        bottomBar = {
            // Pie fijo abajo, siempre visible
            Text(
                text = "Desarrollado por: $NOMBRE_DESARROLLADOR",
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoDegradadoBottom)
                    .padding(vertical = 12.dp)
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FondoDegradadoTop, FondoDegradadoBottom)
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {

                // --- Encabezado de la seccion ---
                Text(
                    text = "Notas del ciclo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Text(
                    text = "Desliza para asignar cada nota (0 a 20)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )

                Spacer(Modifier.height(12.dp))

                // --- Las cuatro filas de curso ---
                FilaCurso(
                    curso = CURSOS[0],
                    nota = notaFundamentos,
                    onNotaChange = { notaFundamentos = it; invalidarResultado() }
                )
                Spacer(Modifier.height(8.dp))

                FilaCurso(
                    curso = CURSOS[1],
                    nota = notaPoo,
                    onNotaChange = { notaPoo = it; invalidarResultado() }
                )
                Spacer(Modifier.height(8.dp))

                FilaCurso(
                    curso = CURSOS[2],
                    nota = notaMoviles,
                    onNotaChange = { notaMoviles = it; invalidarResultado() }
                )
                Spacer(Modifier.height(8.dp))

                FilaCurso(
                    curso = CURSOS[3],
                    nota = notaBaseDatos,
                    onNotaChange = { notaBaseDatos = it; invalidarResultado() }
                )

                Spacer(Modifier.height(16.dp))

                // --- Switch: redondear el promedio final ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Redondear promedio final",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoPrincipal,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = redondear,
                        onCheckedChange = { redondear = it; invalidarResultado() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MoradoPrimario,
                            checkedBorderColor = MoradoPrimario,
                            uncheckedThumbColor = MoradoDeshabilitado,
                            uncheckedTrackColor = Color.White,
                            uncheckedBorderColor = MoradoDeshabilitado
                        )
                    )
                }

                Spacer(Modifier.height(8.dp))

                // --- Checkbox: habilita el boton de calcular ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = confirmado,
                        onCheckedChange = {
                            confirmado = it
                            // Al desmarcar se oculta la tarjeta: el resultado
                            // deja de estar confirmado.
                            if (!it) invalidarResultado()
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MoradoPrimario,
                            uncheckedColor = MoradoDeshabilitado,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Confirmo que las notas son correctas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoPrincipal
                    )
                }

                Spacer(Modifier.height(12.dp))

                // --- Boton CALCULAR: gris mientras el checkbox no este marcado ---
                Button(
                    onClick = {
                        val notas = listOf(
                            notaFundamentos.toInt(),
                            notaPoo.toInt(),
                            notaMoviles.toInt(),
                            notaBaseDatos.toInt()
                        )
                        resultado = CalculadoraNotas.calcular(
                            notas = notas,
                            cursos = CURSOS,
                            redondear = redondear
                        )
                    },
                    enabled = confirmado,
                    shape = RoundedCornerShape(percent = 50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoradoPrimario,
                        contentColor = Color.White,
                        disabledContainerColor = MoradoDeshabilitado,
                        disabledContentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "CALCULAR PROMEDIO",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(Modifier.height(10.dp))

                // --- Reto opcional: boton LIMPIAR ---
                OutlinedButton(
                    onClick = { limpiar() },
                    shape = RoundedCornerShape(percent = 50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MoradoPrimario,
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = MoradoPrimario
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "LIMPIAR",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(Modifier.height(16.dp))

                // --- Mensaje gris o tarjeta de resultados ---
                val resultadoActual = resultado
                if (resultadoActual == null) {
                    Text(
                        text = "Asigna las notas y confirma para calcular",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoDeshabilitado
                    )
                } else {
                    TarjetaResultado(resultado = resultadoActual)

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "\u2713 Promedio calculado correctamente",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = VerdeConfirmacion,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegistroNotasScreenPreview() {
    RegistroNotasTheme {
        RegistroNotasScreen()
    }
}
