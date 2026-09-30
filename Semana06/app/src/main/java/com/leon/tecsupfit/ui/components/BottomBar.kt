package com.leon.tecsupfit.ui.components

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
        }
    }
}
