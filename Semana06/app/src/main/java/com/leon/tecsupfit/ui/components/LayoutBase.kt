package com.leon.tecsupfit.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.R
import com.leon.tecsupfit.ui.theme.*

@Composable
fun Page(
    title: String,
    subtitle: String? = null,
    foto: String?,
    onProfile: () -> Unit,
    onBack: (() -> Unit)? = null,
    profile: Boolean = false,
    home: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 12.dp, bottom = 26.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (onBack != null)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                CircleControl(R.drawable.icon_back, "Volver", onClick = onBack)
                Text(
                    title,
                    Modifier.weight(1f).padding(horizontal = 14.dp),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                if (!profile) Avatar(foto, onProfile)
            }
        else
            Column {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    if (home)
                        Text(
                            "TECSUP  FIT",
                            Modifier.weight(1f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.3.sp,
                        )
                    else
                        Text(
                            title,
                            Modifier.weight(1f),
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    if (!profile) Avatar(foto, onProfile)
                }
                if (home) {
                    Spacer(Modifier.height(5.dp))
                    Text(title, style = MaterialTheme.typography.headlineLarge)
                }
                if (subtitle != null) {
                    Spacer(Modifier.height(9.dp))
                    Text(subtitle, color = TextoSecundario, fontSize = if (home) 11.sp else 14.sp)
                }
            }
        content()
    }
}
