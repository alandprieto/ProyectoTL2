# Proyecto 2 - Plataforma de Streaming (GUI + OMDb)

## Descripción General

Este proyecto corresponde al Entregable 2 del Taller de Lenguajes II (2025). Es una plataforma de streaming interactiva con interfaz gráfica (Java Swing) que integra una base de datos SQLite y la API OMDb. Permite a los usuarios navegar películas, ver detalles de la sinopsis desde OMDb, calificar películas, buscar contenido, ordenar por género/título y gestionar sus reseñas. La arquitectura implementa el patrón MVC (Modelo-Vista-Controlador), DAO para acceso a datos y soporta concurrencia en operaciones asincrónicas (cargas de imágenes, consultas OMDb).

## Aclaraciones sobre Interpretaciones y Diseño

Durante el desarrollo, se tomaron ciertas interpretaciones y decisiones de diseño basadas en los requisitos:

1. **Arquitectura GUI (MVC):**
   - **Vista:** Clases en `src/vista/` (`VistaPrincipal`, `VistaLogin`, `VistaRegistro`, `VistaDetallesPelicula`, `VistaResultadosBusqueda`) responsables de la interfaz gráfica usando Java Swing.
   - **Controlador:** Clases en `src/controlador/` (`ControladorPrincipal`, `ControladorLogin`, `ControladorRegistro`) que manejan eventos de la UI y coordinan con la lógica de negocio.
   - **Modelo:** Clases en `src/modelo/` (`Usuario`, `Cliente`, `Administrador`, `Pelicula`, `Contenido`, `Reseña`, `Staff`) que representan las entidades del sistema.
   - **DAO:** Patrón DAO implementado en `src/dao/` para acceso centralizado a la base de datos.

2. **Integración con OMDb:**
   - Se implementó un servicio centralizado `ConsultaPeliculasOMDb` en `src/servicio/` que consulta la API OMDb de forma asincrónica.
   - Las búsquedas y visualización de detalles (título, año, sinopsis, rating) se obtienen en tiempo real desde OMDb.
   - Se utiliza un diálogo de carga para mejorar la experiencia del usuario durante las consultas a la API.

3. **Concurrencia:**
   - Cargas de imágenes (posters) se realizan en hilos separados para no bloquear la UI.
   - Consultas a OMDb se ejecutan en threads independientes con actualización del UI mediante `SwingUtilities.invokeLater()`.

4. **Orden y Filtrado:**
   - Se implementó una clase `Comparador` para ordenar películas por género (`ComparadorPeliculaGenero`) y por título (`ComparadorPeliculaTitulo`).
   - La pantalla principal muestra un Top 10 de películas (aleatorio o por defecto) con géneros visibles y opciones de ordenamiento.

5. **Base de Datos:**
   - SQLite con estructura mejorada para soportar ratings, géneros y sincronización con OMDb.
   - Precarga automática de películas desde un archivo CSV (`movies_database.csv`) al iniciar si la BD está vacía.

6. **Otros:**
   - La fecha y hora (`FECHA_HORA`) en `RESENIA` se almacena como texto en formato ISO.
   - Los listados se mantienen ordenados usando `Comparator` de Java.
   - Excepciones propias: `DatoInvalidoException`, `CredencialesInvalidasException`, `UsuarioYaExisteException`.


## Estructura del Proyecto

El proyecto sigue una estructura modular con separación clara de responsabilidades (MVC + DAO):

```
src/
├── comparador/              # Clases para ordenamiento de películas y usuarios
│   ├── ComparadorPeliculaGenero.java
│   ├── ComparadorPeliculaTitulo.java
│   ├── ComparadorPeliculaDuracion.java
│   ├── ComparadorUsuarioEmail.java
│   └── ComparadorUsuarioNombre.java
├── controlador/             # Controladores (MVC)
│   ├── AppGUI.java          # Punto de entrada de la aplicación
│   ├── ControladorPrincipal.java
│   ├── ControladorLogin.java
│   └── ControladorRegistro.java
├── dao/                     # Interfaces y implementaciones DAO
│   ├── PeliculaDAO.java
│   ├── PeliculaDAOimple.java
│   ├── UsuarioDAO.java
│   ├── UsuarioDAOimple.java
│   ├── ReseñaDAO.java
│   └── ReseñaDAOimple.java
├── database/                # Gestión de base de datos
│   ├── ConexionBD.java
│   ├── SetupBD.java
│   ├── AutoCargaPeliculas.java
│   └── movies_database.csv
├── enums/                   # Enumerativos
│   └── GeneroPelicula.java
├── excepciones/             # Excepciones propias
│   ├── DatoInvalidoException.java
│   ├── CredencialesInvalidasException.java
│   └── UsuarioYaExisteException.java
├── modelo/                  # Clases de modelo
│   ├── Usuario.java
│   ├── Cliente.java
│   ├── Administrador.java
│   ├── Pelicula.java
│   ├── Contenido.java
│   ├── Reseña.java
│   └── Staff.java
├── servicio/                # Lógica de negocio
│   ├── AppImple.java
│   └── ConsultaPeliculasOMDb.java
└── vista/                   # Vistas (MVC - Java Swing)
    ├── VistaLogin.java
    ├── VistaPrincipal.java
    ├── VistaRegistro.java
    ├── VistaDetallesPelicula.java
    ├── VistaResultadosBusqueda.java
    └── ...
bin/                         # Archivos compilados (.class)
lib/                         # Librerías externas (JSON, SQLite JDBC)
doc/                         # Documentación Javadoc generada
streaming.db                 # Base de datos SQLite (generada en tiempo de ejecución)
compile.bat                  # Script de compilación
```

## Funcionalidades Principales

- **Autenticación:** Login y registro de usuarios (Cliente/Admin) con validación de credenciales.
- **Navegación de Películas:** Visualización de Top 10, exploración aleatoria y búsqueda por título.
- **Integración OMDb:** Consulta de detalles de películas (sinopsis, año, rating) desde OMDb.
- **Calificación:** Los usuarios pueden calificar películas (1-5 estrellas) y ver promedios.
- **Ordenamiento:** Opción de ordenar películas mostradas por género o título.
- **Género Visible:** Cada película muestra su género en la tarjeta de presentación.
- **Interfaz Responsiva:** Carga asincrónica de imágenes y consultas OMDb sin bloquear la UI.
- **Base de Datos:** Persistencia con SQLite, precarga automática de películas desde CSV.

## Cómo Ejecutar

1. **Compilar el proyecto:**
   ```bash
   javac -encoding UTF-8 -cp "lib/*;src" -d "bin" src/**/*.java
   ```

2. **Ejecutar la aplicación:**
   ```bash
   java -cp "bin;lib/*" controlador.AppGUI
   ```

3. **Credenciales de Prueba:**
   - Usuario: `admin1@streaming.com` / Contraseña: `admin123` (Admin)
   - Usuario: `usuario@gmail.com` / Contraseña: `user123` (Cliente)

## Cambios Realizados en Esta Versión

- Migración de consola a interfaz gráfica (Java Swing).
- Implementación del patrón MVC con separación clara de vista, controlador y modelo.
- Integración con la API OMDb para obtener detalles dinámicos de películas.
- Soporte de concurrencia con hilos para cargas asincrónicas (imágenes, consultas OMDb).
- Visualización de géneros en tarjetas de películas.
- Funcionalidad de ordenamiento por género y título.
- Reutilización de `VistaDetallesPelicula` para evitar duplicación de código UI.
- Eliminación del botón "Panel Admin" de la pantalla principal.
- Compilación separada: archivos `.java` en `src/`, archivos `.class` en `bin/`.