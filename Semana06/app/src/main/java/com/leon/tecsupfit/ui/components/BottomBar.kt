package com.leon.tecsupfit.ui.components

<<<<<<< HEAD
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.R
import com.leon.tecsupfit.ui.theme.*

@Composable
fun BottomBar(active: String, navigate: (String) -> Unit) {
    val routes = listOf("inicio", "reservas", "rutinas", "perfil")
    val names = listOf("Inicio", "Reservas", "Rutinas", "Perfil")
    val icons =
        listOf(
            R.drawable.icon_home,
            R.drawable.icon_calendar,
            R.drawable.icon_activity,
            R.drawable.icon_user,
        )
    val index = routes.indexOf(active).coerceAtLeast(0)
    Glass(
        Modifier.padding(horizontal = 17.dp, vertical = 9.dp).fillMaxWidth(),
        radius = 30.dp,
        elevation = 18.dp,
        opacity = .59f,
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth().padding(horizontal = 7.dp, vertical = 7.dp)) {
            val slot = maxWidth / 4
            val pos by
                animateDpAsState(
                    slot * index,
                    if (LocalMovimientoReducido.current) snap()
                    else spring(dampingRatio = .8f, stiffness = 380f),
                    label = "Dock",
                )
            Box(
                Modifier.offset(x = pos).width(slot).height(47.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    Modifier.width(57.dp)
                        .height(46.dp)
                        .background(AzulProfundo, RoundedCornerShape(22.dp))
                        .border(1.dp, Color(0xFF63889E), RoundedCornerShape(22.dp))
                ) {
                    Box(
                        Modifier.align(Alignment.TopEnd)
                            .padding(7.dp)
                            .size(5.dp)
                            .background(LuzAqua, RoundedCornerShape(5.dp))
                    )
                }
            }
            Row(Modifier.fillMaxWidth().selectableGroup()) {
                routes.forEachIndexed { i, r ->
                    Column(
                        Modifier.weight(1f)
                            .heightIn(min = 63.dp)
                            .selectable(i == index, role = Role.Tab, onClick = { navigate(r) })
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AssetIcon(
                            icons[i],
                            modifier = Modifier.size(21.dp),
                            color = if (i == index) Color.White else TextoSecundario,
                        )
                        Text(
                            names[i],
                            fontSize = 10.sp,
                            fontWeight = if (i == index) FontWeight.Bold else FontWeight.Normal,
                            color = TintaMarina,
                        )
                    }
                }
            }
=======
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.navigation.Pantallas
import com.leon.tecsupfit.ui.theme.VerdeTecsup

data class ItemBottomBar(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

private val itemsBottomBar = listOf(
    ItemBottomBar(Pantallas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBottomBar(Pantallas.RESERVAS, "Reservas", Icons.Filled.List),
    ItemBottomBar(Pantallas.RUTINAS, "Rutinas", Icons.Filled.FitnessCenter),
    ItemBottomBar(Pantallas.PERFIL, "Perfil", Icons.Filled.Person)
)

/**
 * Barra inferior con las 4 pestañas. Resalta el ícono de la pantalla actual.
 */
@Composable
fun TecsupFitBottomBar(rutaActual: String, onNavegar: (String) -> Unit) {
    NavigationBar {
        itemsBottomBar.forEach { item ->
            val seleccionado = rutaActual == item.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { onNavegar(item.ruta) },
                icon = {
                    Icon(
                        imageVector = item.icono,
                        contentDescription = item.etiqueta,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeTecsup,
                    selectedTextColor = VerdeTecsup,
                    indicatorColor = Color(0xFFDCEFE4)
                )
            )
>>>>>>> sinia
        }
    }
}
