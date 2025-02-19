# 🐾 MyFavouritesPets

**MyFavouritesPets** es una aplicación móvil en **Kotlin** que permite gestionar una lista de mascotas favoritas, almacenando información como su nombre, categoría, imagen y nivel de "amorosidad". Además, ofrece funcionalidades de persistencia con **SQLite**, ordenación, filtrado de favoritos y edición de datos.

---

## ✨ **Características principales**
✔ **CRUD Completo**: Permite **añadir, editar, eliminar y visualizar mascotas**.  
✔ **Persistencia de Datos**: Usa **SQLite** para guardar y recuperar mascotas incluso después de cerrar la app.  
✔ **Filtrado y Ordenación**:  
   - Ordenar por **nombre (A-Z, Z-A)**.  
   - Ordenar por **nivel de amorosidad** (de 1 a 5 estrellas).  
   - **Mostrar solo mascotas favoritas** o **todas**.  
✔ **Gestión de Imágenes**:  
   - Seleccionar imagen desde la **galería**.  
   - Tomar foto con la **cámara**.  
✔ **Enlace a Wikipedia**: Cada mascota puede tener un enlace para consultar más información.  
✔ **Confirmación al eliminar**: Se muestra un **AlertDialog** antes de eliminar una mascota.  

---

## 📷 **Capturas de pantalla (ejemplo)**
*(Puedes añadir capturas reales de la app aquí)*  

---

## 🛠 **Tecnologías utilizadas**
- **Kotlin**
- **Android Jetpack**
- **SQLite**
- **RecyclerView**
- **Material Design**
- **FileProvider** (para gestionar imágenes tomadas con la cámara)

---

## 🚀 **Cómo ejecutar el proyecto**
1. **Clona el repositorio** en Android Studio.
   ```bash
   git clone https://github.com/alvaro944/MyFavouritesPets.git
   ```
2. **Abre el proyecto en Android Studio**.
3. **Ejecuta el emulador o conecta un dispositivo físico**.
4. ¡Listo! 🎉 Ahora puedes **añadir, editar y gestionar tus mascotas favoritas**.

---

## 🔄 **Funcionamiento detallado**
### 📌 **Pantalla Principal**
- Muestra una lista de mascotas en un **RecyclerView**.
- Se puede **ordenar y filtrar** usando el menú superior.
- Permite **añadir una nueva mascota** con el botón flotante.

### ✏ **Añadir / Editar Mascota**
- Introducir **nombre, tipo de pelaje, categoría y nivel de amorosidad**.
- Elegir una imagen desde la **galería** o **tomar una foto**.
- Guardar la mascota en la base de datos **SQLite**.

### ⭐ **Favoritos**
- Se puede **marcar/desmarcar** una mascota como favorita.
- Filtrar la lista para **ver solo favoritas**.

### 🗑 **Eliminar Mascota**
- Se muestra un **diálogo de confirmación** antes de eliminar.
- La mascota se elimina de la **lista y de SQLite**.

---

## 📌 **Requisitos del Proyecto (Cumplidos ✅)**
✅ **Widgets utilizados**: `EditText`, `RecyclerView`, `Button`, `ImageView`, `RatingBar`.  
✅ **Uso de SQLite para almacenamiento local**.  
✅ **Intents implícitos y explícitos** (para imágenes y enlaces).  
✅ **Gestión de permisos** (Cámara y almacenamiento).  
✅ **Implementación de menús** (`Toolbar` con opciones de ordenación).  
✅ **Confirmación antes de eliminar (`AlertDialog`)**.  
✅ **Persistencia de datos**: los datos se guardan incluso después de cerrar la app.  

---

## 🎯 **Posibles Mejoras Futuras**
🔹 **Mejorar la UI con Material Design (colores, fuentes, animaciones).**  
🔹 **Subir imágenes a Firebase Storage y sincronizar con Firebase Database.**  
🔹 **Integrar Google Maps para guardar la ubicación de cada mascota.**  
🔹 **Modo oscuro y personalización de colores.**  

---

## 📄 **Licencia**
Este proyecto es de código abierto y puedes modificarlo libremente.  

---

## 💡 **Créditos**
Desarrollado por **Álvaro Cervantes** como parte del proyecto de **PMDM en DAM**. 🎓  
