# Lab 04 – Mi Carrito TECSUP

**Autor:** Daniella León
**Curso:** Desarrollo de Aplicaciones Móviles – Tecsup

## Descripción
App en Jetpack Compose que agrega productos a un carrito, los muestra en una lista
desplazable (LazyColumn) con tarjetas, permite eliminarlos y calcula subtotal,
IGV (18%) y total con 2 decimales en un panel fijo inferior.

## Capturas
| Carrito vacío | Carrito con productos |
|---|---|
| ![vacio](capturas/vacio.png) | ![productos](capturas/productos.png) |

## Estructura
```
data/Producto.kt              -> modelo
util/Formato.kt               -> montos con 2 decimales
ui/screens/PantallaCarrito.kt -> pantalla y estados
ui/components/                -> BarraSuperior, FormularioProducto, TarjetaProducto,
                                 CarritoVacio, PanelTotales, DialogoEliminar
ui/theme/                     -> colores y tema
```

## Respuestas conceptuales

**(a) ¿Por qué mutableStateListOf y no una MutableList normal?**
`mutableStateListOf` es una lista observable: Compose registra quién la lee y, cuando
se agrega o elimina un elemento, vuelve a dibujar (recomposición) la LazyColumn y los
totales. Una `MutableList` normal sí cambia en memoria, pero Compose no se entera y
la pantalla no se actualiza.

**(b) ¿Por qué la lista es `val` si le agregamos elementos?**
`val` solo impide reasignar la referencia (apuntar a otra lista). El objeto al que
apunta es mutable, así que `add()` y `remove()` modifican su contenido sin cambiar
la referencia.

**(c) ¿Qué hace `weight(1f)` en la LazyColumn?**
Reparte el espacio sobrante de la Column: la lista ocupa todo el alto disponible
después de medir el formulario y el panel de totales, por eso el panel queda fijo
abajo y la lista se desplaza cuando hay muchos productos.

## Retos opcionales
- Confirmación de borrado con AlertDialog.
- Descuento con `when`: 5% si total > 3000, 10% si total > 5000.
