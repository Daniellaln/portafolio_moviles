package com.leon.tecsupfit.ui.components

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.layer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.leon.tecsupfit.R
import com.leon.tecsupfit.ui.theme.*

val LocalFondoCristal = staticCompositionLocalOf<GraphicsLayer?> { null }
val LocalOrigenCristal = staticCompositionLocalOf { Offset.Zero }

/** Fondo independiente desenfocado; contenido y tipografía permanecen nítidos. */
@Composable
fun Glass(
    modifier: Modifier = Modifier,
    radius: Dp = 24.dp,
    tint: Color = Color.White,
    opacity: Float = .68f,
    elevation: Dp = 9.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val fondo = LocalFondoCristal.current
    val origen = LocalOrigenCristal.current
    var pos by remember { mutableStateOf(Offset.Zero) }
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by
        animateFloatAsState(
            if (pressed && !LocalMovimientoReducido.current) .977f else 1f,
            spring(dampingRatio = .7f, stiffness = 500f),
            label = "Cristal",
        )
    val shape = RoundedCornerShape(radius)
    Box(
        modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation,
                shape,
                ambientColor = AzulProfundo.copy(alpha = .17f),
                spotColor = AzulProfundo.copy(alpha = .22f),
            )
            .clip(shape)
            .onGloballyPositioned { pos = it.positionInRoot() }
            .then(
                if (onClick != null)
                    Modifier.clickable(source, null, role = Role.Button, onClick = onClick)
                else Modifier
            )
    ) {
        if (fondo != null)
            Canvas(
                Modifier.matchParentSize().graphicsLayer {
                    if (Build.VERSION.SDK_INT >= 31)
                        renderEffect = BlurEffect(18f, 18f, TileMode.Clamp)
                }
            ) {
                translate(origen.x - pos.x, origen.y - pos.y) { drawLayer(fondo) }
            }
        Box(
            Modifier.matchParentSize()
                .background(
                    tint.copy(
                        alpha = if (Build.VERSION.SDK_INT >= 31) opacity else maxOf(opacity, .84f)
                    )
                )
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = .40f),
                            Color.White.copy(alpha = .01f),
                            ReflejoLila.copy(alpha = .10f),
                        )
                    )
                )
        )
        Canvas(Modifier.matchParentSize()) {
            drawOval(
                LuzAqua.copy(alpha = .25f),
                Offset(-size.width * .16f, -size.height * .72f),
                Size(size.width * 1.27f, size.height * 1.6f),
                style = Stroke(1.dp.toPx()),
            )
            drawOval(
                ReflejoLila.copy(alpha = .24f),
                Offset(size.width * .22f, -size.height * .1f),
                Size(size.width, size.height * 1.12f),
                style = Stroke(.8.dp.toPx()),
            )
        }
        Box(
            Modifier.matchParentSize()
                .border(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(Color.White, Color.White.copy(alpha = .35f), Color.White)
                    ),
                    shape,
                )
        )
        content()
    }
}

@Composable
fun AssetIcon(
    id: Int,
    description: String? = null,
    modifier: Modifier = Modifier.size(22.dp),
    color: Color = TintaMarina,
) {
    Image(
        painterResource(id),
        description,
        modifier.graphicsLayer {
            scaleX = 1.35f
            scaleY = 1.35f
        },
        colorFilter = ColorFilter.tint(color),
    )
}

@Composable
fun CircleControl(id: Int, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Glass(
        modifier.size(52.dp).semantics { contentDescription = label },
        radius = 50.dp,
        elevation = 7.dp,
        onClick = onClick,
    ) {
        AssetIcon(id, modifier = Modifier.size(22.dp).align(Alignment.Center))
    }
}

@Composable
fun Avatar(foto: String?, onClick: () -> Unit) {
    Glass(
        Modifier.size(48.dp).semantics { contentDescription = "Abrir mi perfil" },
        radius = 50.dp,
        elevation = 5.dp,
        onClick = onClick,
    ) {
        AsyncImage(
            foto ?: R.drawable.perfil,
            null,
            Modifier.padding(5.dp).fillMaxSize().clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.perfil),
            error = painterResource(R.drawable.perfil),
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    busy: Boolean = false,
    icon: Int = R.drawable.icon_arrow,
    onClick: () -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by
        animateFloatAsState(
            if (pressed && !LocalMovimientoReducido.current) .97f else 1f,
            spring(dampingRatio = .65f, stiffness = 500f),
            label = "Botón",
        )
    Button(
        onClick,
        Modifier.fillMaxWidth()
            .heightIn(min = 62.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                if (enabled) 12.dp else 0.dp,
                RoundedCornerShape(28.dp),
                spotColor = AzulProfundo.copy(alpha = .3f),
            ),
        enabled = enabled && !busy,
        shape = RoundedCornerShape(28.dp),
        interactionSource = source,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = AzulProfundo,
                disabledContainerColor = AzulProfundo.copy(alpha = .45f),
            ),
        contentPadding = PaddingValues(start = 22.dp, end = 11.dp, top = 10.dp, bottom = 10.dp),
    ) {
        Text(text, Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        if (busy)
            CircularProgressIndicator(Modifier.size(36.dp), color = Color.White, strokeWidth = 2.dp)
        else
            Box(
                Modifier.size(40.dp).background(LuzAqua, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AssetIcon(icon)
            }
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit) {
    Glass(
        Modifier.fillMaxWidth().heightIn(min = 52.dp),
        radius = 25.dp,
        elevation = 4.dp,
        onClick = onClick,
    ) {
        Text(
            text,
            Modifier.align(Alignment.Center).padding(16.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun SectionTitle(title: String, action: String? = null, onClick: () -> Unit = {}) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
        if (action != null)
            TextButton(onClick, Modifier.heightIn(min = 48.dp)) {
                Text(action, color = Color(0xFF226D80), fontSize = 12.sp)
            }
    }
}

@Composable
fun InfoCard(title: String, text: String? = null, aqua: Boolean = false) {
    Glass(
        Modifier.fillMaxWidth(),
        radius = 22.dp,
        tint = if (aqua) Color(0xFFE1F6F9) else Color.White,
        elevation = 4.dp,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            if (text != null)
                Text(text, color = TextoSecundario, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}

@Composable
fun Segments(options: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Glass(Modifier.fillMaxWidth(), radius = 26.dp, elevation = 5.dp) {
        Row(Modifier.padding(5.dp).selectableGroup()) {
            options.forEachIndexed { i, label ->
                val bg by
                    animateColorAsState(
                        if (i == selected) AzulProfundo else Color.Transparent,
                        label = "Selección",
                    )
                Box(
                    Modifier.weight(1f)
                        .heightIn(min = 48.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(bg)
                        .selectable(i == selected, role = Role.Tab, onClick = { onSelect(i) })
                        .padding(vertical = 12.dp, horizontal = 3.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (i == selected) Color.White else TextoSecundario,
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoHero(
    nombre: String,
    imageKey: String? = null,
    kicker: String,
    subtitle: String,
    detail: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val height = if (detail) 340.dp else 290.dp
    val layer = rememberGraphicsLayer()
    var origin by remember { mutableStateOf(Offset.Zero) }
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val scale by
        animateFloatAsState(
            if (pressed && !LocalMovimientoReducido.current) .985f else 1f,
            spring(dampingRatio = .75f),
            label = "Foto",
        )
    Box(
        Modifier.fillMaxWidth()
            .heightIn(min = height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (onClick != null)
                    Modifier.clickable(interactions, null, role = Role.Button, onClick = onClick)
                else Modifier
            )
    ) {
        Image(
            painterResource(ImageUtils.imagen(nombre, imageKey)),
            null,
            Modifier.fillMaxWidth()
                .height(height - 8.dp)
                .onGloballyPositioned { origin = it.positionInRoot() }
                .drawWithContent {
                    layer.record { this@drawWithContent.drawContent() }
                    drawLayer(layer)
                }
                .shadow(10.dp, RoundedCornerShape(30.dp))
                .clip(RoundedCornerShape(30.dp)),
            contentScale = ContentScale.Crop,
        )
        CompositionLocalProvider(
            LocalFondoCristal provides layer,
            LocalOrigenCristal provides origin,
        ) {
            Glass(
                Modifier.padding(horizontal = 14.dp)
                    .padding(top = if (detail) 254.dp else 174.dp)
                    .fillMaxWidth(),
                radius = 25.dp,
                elevation = 13.dp,
                opacity = .80f,
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text(
                            kicker.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF17697C),
                            letterSpacing = .4.sp,
                        )
                        Text(
                            nombre,
                            fontSize = 23.sp,
                            lineHeight = 27.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        if (subtitle.isNotEmpty())
                            Text(
                                subtitle,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = TextoSecundario,
                            )
                    }
                    if (onClick != null) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            Modifier.size(42.dp)
                                .background(Color.White.copy(alpha = .65f), CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            AssetIcon(R.drawable.icon_arrow)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoRow(
    nombre: String,
    subtitle: String,
    tag: String? = null,
    imageKey: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Glass(Modifier.fillMaxWidth(), radius = 23.dp, elevation = 5.dp, onClick = onClick) {
        Row(Modifier.padding(9.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(ImageUtils.imagen(nombre, imageKey)),
                null,
                Modifier.size(68.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
            )
            Column(
                Modifier.weight(1f).padding(horizontal = 13.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(nombre, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp)
                Text(subtitle, fontSize = 12.sp, color = TextoSecundario)
                if (tag != null)
                    Text(
                        tag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF216B7D),
                    )
            }
            if (onClick != null)
                AssetIcon(
                    R.drawable.icon_arrow,
                    modifier = Modifier.padding(end = 7.dp).size(16.dp),
                )
        }
    }
}
