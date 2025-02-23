### 🐾 **MyFavouritesPets**  

**MyFavouritesPets** es una aplicación móvil en **Kotlin** que permite gestionar una lista de mascotas favoritas. Los datos se almacenan de forma persistente en **SQLite** y se pueden respaldar/restaurar con **Firebase**. La app incluye funcionalidades avanzadas como **gestión de imágenes, ordenación, filtrado, edición de datos y ubicación en Google Maps**.  

---

## ✨ **Características principales**  

✔ **CRUD Completo**: Permite **añadir, editar, eliminar y visualizar mascotas**.  
✔ **Persistencia de Datos**:  
   - **SQLite** para guardar y recuperar mascotas incluso después de cerrar la app.  
   - **Firebase** para realizar **backup y restauración** de los datos.  
✔ **Filtrado y Ordenación**:  
   - Ordenar por **nombre (A-Z, Z-A)**.  
   - Ordenar por **nivel de amorosidad** (de 1 a 5 estrellas).  
   - **Mostrar solo mascotas favoritas** o **todas**.  
✔ **Gestión de Imágenes**:  
   - Seleccionar imagen desde la **galería**.  
   - Tomar foto con la **cámara**.  
✔ **Backup y Restauración en Firebase** 📲:  
   - Permite **guardar todas las mascotas en Firebase** como copia de seguridad.  
   - **Recuperar mascotas eliminadas** desde Firebase.  
✔ **Ubicación y Mapas** 🗺️:  
   - Cada mascota puede tener una ubicación guardada (**latitud/longitud**).  
   - **Abrir Google Maps** para ver la ubicación de la mascota.  
✔ **Enlace a Wikipedia**: Cada mascota puede tener un enlace para consultar más información.  
✔ **Confirmación al eliminar**: Se muestra un **AlertDialog** antes de eliminar una mascota.  

---

## 📷 **Capturas de pantalla (Ejemplo)**  
*Pendiente*  

---

## 🛠 **Tecnologías utilizadas**  
- **Kotlin**  
- **Android Jetpack**  
- **SQLite** (Persistencia local)  
- **RecyclerView**  
- **Material Design**  
- **Google Maps (opcional, solo para abrir ubicaciones)**  
- **Firebase Firestore (Backup/Restore)**  
- **FileProvider** (para gestionar imágenes tomadas con la cámara)  

---

## 🚀 **Cómo ejecutar el proyecto**  
1️⃣ **Clona el repositorio** en Android Studio.  
   ```bash
   git clone https://github.com/alvaro944/MyFavouritesPets.git
   ```
2️⃣ **Abre el proyecto en Android Studio**.  
3️⃣ **Ejecuta el emulador o conecta un dispositivo físico**.  
4️⃣ ¡Listo! 🎉 Ahora puedes **añadir, editar y gestionar tus mascotas favoritas**.  

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

### 🔄 **Backup y Restauración en Firebase**  
- **Backup:** Guarda todas las mascotas en **Firebase Firestore**.  
- **Restaurar:** Recupera mascotas eliminadas desde Firebase.  
- Evita **duplicados** y solo restaura los que no están en la base de datos.  

### 📍 **Ubicación en Google Maps**  
- Cada mascota puede tener una ubicación guardada.  
- **Abrir Google Maps** con la ubicación de la mascota.  

---

## 📌 **Estructura del Proyecto**  
```
📂 edu.alvarocervantes.myfavoritepet
 ┣ 📂 adapters          → Contiene los adaptadores (`PetAdapter.kt`)
 ┣ 📂 data              → Base de datos (`PetDatabaseHelper.kt`)
 ┣ 📂 model             → Modelos de datos (`Pet.kt`)
 ┣ 📂 utils             → Clases auxiliares (`FirebaseBackupHelper.kt`)
 ┣ 📂 views             → Vistas y Activities (`MainActivity.kt`, `AddEditPetActivity.kt`)
```

---

## 📌 **Requisitos del Proyecto (Cumplidos ✅)**  
✅ **Widgets utilizados**: `EditText`, `RecyclerView`, `Button`, `ImageView`, `RatingBar`.  
✅ **Uso de SQLite para almacenamiento local**.  
✅ **Uso de Firebase Firestore para backup y restauración**.  
✅ **Intents implícitos y explícitos** (para imágenes y enlaces).  
✅ **Gestión de permisos (Cámara, almacenamiento, ubicación)**.  
✅ **Implementación de menús (`Toolbar` con opciones de ordenación)**.  
✅ **Confirmación antes de eliminar (`AlertDialog`)**.  
✅ **Persistencia de datos: los datos se guardan incluso después de cerrar la app**.  

---

## 🎯 **Posibles Mejoras Futuras**  
🔹 **Mejorar la UI con Material Design (colores, fuentes, animaciones).**  
🔹 **Subir imágenes a Firebase Storage y sincronizar con Firebase Database.**  
🔹 **Integrar Google Maps dentro de la app en lugar de solo abrirlo.**  
🔹 **Modo oscuro y personalización de colores.**  

---

## 📄 **Licencia**  
Este proyecto es de código abierto y puedes modificarlo libremente.  

---

## 💡 **Créditos**  
Desarrollado por **Álvaro Cervantes** como parte del proyecto de **PMDM en DAM**. 🎓  

