# Registro de Notas — Semana 3

App de Jetpack Compose que calcula el **promedio ponderado** de cuatro cursos de
programación, permite **redondear** el resultado y devuelve una **observación**
con un chip de color según el rango obtenido.

Laboratorio 03 — Programación en Móviles (Tecsup).

---

## Captura de la app

> **Pendiente:** ejecuta la app en el emulador, toma la captura y guárdala en
> `capturas/app.png`. Luego descomenta la línea de abajo.

<!-- ![Pantalla principal](capturas/app.png) -->

---

## Cómo ejecutarlo

1. Abre Android Studio → **File ▸ Open** y selecciona la carpeta `RegistroNotas`.
2. Espera a que termine el *Gradle sync* (descarga las dependencias la primera vez).
3. Elige un emulador o un dispositivo con **Android 7.0 (API 24)** o superior.
4. Pulsa **Run ▸ Run 'app'**.

Para correr las pruebas unitarias:

```bash
./gradlew test
```

---

## Antes de entregar

Abre `app/src/main/java/pe/edu/tecsup/registronotas/ui/screens/RegistroNotasScreen.kt`
y cambia la constante del pie de página:

```kotlin
private const val NOMBRE_DESARROLLADOR = "Daniella Leon Andres"
```

---

## Reglas de negocio

### Pesos de los cursos (fijos, suman 100%)

| Curso                            | Peso |
|----------------------------------|------|
| Fundamentos de Programación      | 20%  |
| Programación Orientada a Objetos | 25%  |
| Programación en Móviles          | 30%  |
| Base de Datos                    | 25%  |

**Promedio ponderado** = nota₁×0.20 + nota₂×0.25 + nota₃×0.30 + nota₄×0.25,
mostrado siempre con 2 decimales.

**Promedio final**: si el Switch está ON se redondea al entero más cercano con
`roundToInt()` y se añade la aclaración *(redondeado)*; si está OFF se muestra el
mismo valor con 2 decimales.

### Observación (según el promedio FINAL)

| Promedio final | Observación     | Color del chip |
|----------------|-----------------|----------------|
| 17 a 20        | EXCELENTE       | Verde oscuro   |
| 13 a 16.99     | APROBADO        | Verde          |
| 10 a 12.99     | EN RECUPERACIÓN | Ámbar          |
| Menor a 10     | DESAPROBADO     | Rojo           |

---

## Casos de prueba

Los cuatro casos del enunciado están automatizados en
`app/src/test/java/pe/edu/tecsup/registronotas/CalculadoraNotasTest.kt` y se
comparan contra el **texto exacto** que la app muestra en pantalla.

| Notas (F, POO, M, BD) | Redondear | Prom. ponderado | Prom. final | Observación     |
|-----------------------|-----------|-----------------|-------------|-----------------|
| 15, 13, 16, 14        | ON        | 14.55           | 15          | APROBADO        |
| 12, 10, 11, 9         | OFF       | 10.45           | 10.45       | EN RECUPERACIÓN |
| 18, 17, 19, 18        | ON        | 18.05           | 18          | EXCELENTE       |
| 8, 9, 7, 10           | OFF       | 8.45            | 8.45        | DESAPROBADO     |

**Nota sobre precisión:** los pesos se guardan como enteros (20, 25, 30, 25) y la
suma se hace en enteros antes de dividir entre 100. Si se usaran decimales
(`0.20`, `0.25`…) el caso `8, 9, 7, 10` daría `8.4499999…` y se mostraría como
`8.44` en lugar de `8.45`.

---

## Los controles nuevos

Los tres controles de esta semana usan el mismo patrón **estado + dúo** del
`OutlinedTextField`: un valor que se lee y un callback que avisa del cambio.

| Control            | Dúo de estado                        | Dónde está en el código   |
|--------------------|--------------------------------------|---------------------------|
| `Slider`           | `value` / `onValueChange` (Float)    | `ui/components/FilaCurso.kt` |
| `Switch`           | `checked` / `onCheckedChange` (Bool) | `ui/screens/RegistroNotasScreen.kt` |
| `Checkbox`         | `checked` / `onCheckedChange` (Bool) | `ui/screens/RegistroNotasScreen.kt` |
| `Button (enabled)` | `enabled = confirmado`               | `ui/screens/RegistroNotasScreen.kt` |

El `Slider` usa `valueRange = 0f..20f` y `steps = 19` (21 posiciones: los dos
extremos más 19 intermedias), y se muestra con `.toInt()` para que las notas sean
enteras. Cada curso tiene **su propia** variable de estado (cuatro `remember`).

---

## Reto opcional implementado

- **Botón LIMPIAR** — devuelve las 4 notas a 0, apaga el Switch, desmarca el
  Checkbox y oculta la tarjeta de resultados.

---

## Estructura del proyecto

```
RegistroNotas/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/pe/edu/tecsup/registronotas/
│       │   │   ├── MainActivity.kt
│       │   │   ├── model/
│       │   │   │   └── Curso.kt                  # cursos y pesos
│       │   │   ├── logic/
│       │   │   │   └── CalculadoraNotas.kt       # toda la matemática
│       │   │   └── ui/
│       │   │       ├── theme/                    # colores, tipografía, tema
│       │   │       ├── components/
│       │   │       │   ├── FilaCurso.kt          # Slider + badge en vivo
│       │   │       │   ├── ChipObservacion.kt    # chip de color
│       │   │       │   └── TarjetaResultado.kt   # tarjeta de la Figura 2
│       │   │       └── screens/
│       │   │           └── RegistroNotasScreen.kt
│       │   ├── res/
│       │   └── AndroidManifest.xml
│       └── test/                                 # pruebas de los 4 casos
├── capturas/
├── gradle/
└── build.gradle.kts
```

La lógica de negocio está separada de la UI, así que se puede probar con JUnit
sin levantar un emulador.

---

## Decisiones de diseño

- **Espaciado consistente:** 20 dp de margen lateral y una escala de 8/12/16 dp
  entre bloques, de modo que las cuatro filas de curso tengan el mismo ritmo.
- **Jerarquía clara:** título de la barra 24 sp en negrita, nombres de curso en
  negrita 16 sp y textos de apoyo en 12 sp gris, para que la vista se lea de
  arriba hacia abajo sin esfuerzo.
- **Contraste sobre el degradado:** el fondo va de lavanda muy claro a casi
  blanco, y los textos usan morado oscuro o gris medio; la tarjeta de resultados
  es blanca sólida para que nunca compita con el degradado.
- **Alineación:** todos los elementos comparten el mismo margen izquierdo y los
  badges de nota quedan alineados a la derecha en una columna imaginaria.
