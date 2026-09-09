<div align="center">

<img src="assets/s3-banner.gif" width="100%" alt="Registro de Notas - Laboratorio 03. Promedio ponderado de 4 cursos con Jetpack Compose. Daniella Leon Andres, Tecsup." />

<img src="assets/s3-p1.svg" height="34" alt="4 casos de prueba OK" />
<img src="assets/s3-p2.svg" height="34" alt="Kotlin" />
<img src="assets/s3-p3.svg" height="34" alt="Jetpack Compose" />
<img src="assets/s3-p4.svg" height="34" alt="Material 3" />

<br/><br/>

<img src="assets/s3-nota.svg" width="94%" alt="App que calcula el promedio ponderado de mis 4 cursos del ciclo. Cada curso pesa distinto, se puede redondear el resultado con un switch, y segun la nota final sale una observacion con su color." />

<br/><br/>

<img src="assets/s3-capturas.svg" width="88%" alt="Pruebas y capturas" />

<br/>

Los 4 casos de prueba del enunciado, corriendo en el emulador.

</div>

| Redondear ON — Aprobado | Redondear OFF — En Recuperación |
|:--:|:--:|
| <img src="./capturas/app.png" width="260" alt="Notas 15, 13, 16, 14 con redondeo activado: promedio 14.55, final 15, APROBADO" /> | <img src="./capturas/app2.png" width="260" alt="Notas 12, 10, 11, 9 sin redondeo: promedio 10.45, EN RECUPERACION" /> |
| **Redondear ON — Excelente** | **Redondear OFF — Desaprobado** |
| <img src="./capturas/app3.png" width="260" alt="Notas 18, 17, 19, 18 con redondeo activado: promedio 18.05, final 18, EXCELENTE" /> | <img src="./capturas/app4.png" width="260" alt="Notas 8, 9, 7, 10 sin redondeo: promedio 8.45, DESAPROBADO" /> |

<div align="center">

<br/>

<img src="assets/s3-pesos.svg" width="88%" alt="Pesos de los cursos" />

<br/>

<img src="assets/s3-dona.svg" width="94%" alt="Pesos: Fundamentos de Programacion 20 por ciento, Programacion Orientada a Objetos 25 por ciento, Programacion en Moviles 30 por ciento, Base de Datos 25 por ciento" />

</div>

**Promedio ponderado** = nota₁×0.20 + nota₂×0.25 + nota₃×0.30 + nota₄×0.25, con 2 decimales.

Si el switch está prendido, el promedio final se redondea con `roundToInt()`. Si no, se queda con los 2 decimales.

<div align="center">

<br/>

<img src="assets/s3-casos.svg" width="88%" alt="Casos de prueba" />

</div>

La observación sale del promedio **final**, no del ponderado:

| Promedio final | Observación | Color del chip |
|:--|:--|:--|
| 17 a 20 | EXCELENTE | verde oscuro |
| 13 a 16.99 | APROBADO | verde |
| 10 a 12.99 | EN RECUPERACIÓN | ámbar |
| menos de 10 | DESAPROBADO | rojo |

Los 4 casos están automatizados en `CalculadoraNotasTest.kt` y comparan el texto exacto que se ve en pantalla:

| Notas | Redondear | Ponderado | Final | Observación |
|:--|:--:|--:|--:|:--|
| 15, 13, 16, 14 | ON | 14.55 | 15 | APROBADO |
| 12, 10, 11, 9 | OFF | 10.45 | 10.45 | EN RECUPERACIÓN |
| 18, 17, 19, 18 | ON | 18.05 | 18 | EXCELENTE |
| 8, 9, 7, 10 | OFF | 8.45 | 8.45 | DESAPROBADO |

> **Detalle de precisión:** los pesos se guardan como enteros (20, 25, 30, 25) y recién al final se
> divide entre 100. Con decimales (`0.20`, `0.25`…) el último caso da `8.4499999…` y se mostraría
> `8.44` en vez de `8.45`.

<div align="center">

<br/>

<img src="assets/s3-controles.svg" width="88%" alt="Los controles nuevos" />

<br/>

Todos usan el mismo patrón del `TextField`: un valor y un callback.

<br/>

<img src="assets/s3-c-slider.svg" width="45%" alt="Slider: value / onValueChange con Float" />
<img src="assets/s3-c-switch.svg" width="45%" alt="Switch: checked / onCheckedChange" />

<img src="assets/s3-c-check.svg" width="45%" alt="Checkbox: checked / onCheckedChange" />
<img src="assets/s3-c-button.svg" width="45%" alt="Button: enabled = confirmado" />

</div>

El `Slider` usa `valueRange = 0f..20f` y `steps = 19` para que las notas sean enteras, y cada curso
tiene **su propia** variable de estado (cuatro `remember`).

<div align="center">

<br/>

<img src="assets/s3-reto.svg" width="88%" alt="Reto opcional" />

<img src="assets/s3-reto-nota.svg" width="94%" alt="Boton LIMPIAR: devuelve las 4 notas a 0, apaga el switch, desmarca el checkbox y esconde la tarjeta de resultados." />

</div>

<br/>

### Para correrlo

Abrir la carpeta `RegistroNotas` en Android Studio, esperar el Gradle sync y darle a Run.
Necesita Android 7.0 (API 24) o más. Las pruebas: `./gradlew test`
