# Proyecto 2 - Plataforma de Streaming (Persistencia)

## Descripción General

Este proyecto corresponde al Entregable 2 del Taller de Lenguajes II (2025). Es una prueba de concepto (POC) enfocada en la persistencia de objetos en una base de datos SQLite. Implementa una sección del modelo de clases de una plataforma de streaming, permitiendo gestionar Usuarios (Clientes y Administradores), Películas y Reseñas a través de una aplicación de consola. Se utiliza el patrón DAO para el acceso a datos.

## Aclaraciones sobre Interpretaciones y Diseño

Durante el desarrollo, se tomaron ciertas interpretaciones y decisiones de diseño basadas en los requisitos y las dudas surgidas:

1.  **Modelo de Base de Datos:**
    * Se optó por **simplificar el esquema** propuesto en el diagrama Entidad-Relación del PDF. La tabla `DATOS_PERSONALES` fue **integrada directamente en la tabla `USUARIO`**, incluyendo las columnas `DNI`, `NOMBRE` y `APELLIDO`. Esto difiere del diagrama y los primeros codigos SQL del PDF.
    * La tabla `USUARIO` **no incluye** el campo `NOMBRE_USUARIO` sugerido en el PDF, utilizando en su lugar `NOMBRE` y `APELLIDO`. Se añadió una columna `ROL` ("CLIENTE" o "ADMIN") para diferenciar los tipos de usuario.
    * La tabla `PELICULA` utiliza `GENERO TEXT(50)` y `DURACION INTEGER` (representando minutos) en lugar de `TEXT(1)` y `REAL` respectivamente, como se veía en los codigos del PDF. El campo `RESUMEN`, aunque opcional según el PDF, se omitió completamente en la tabla y la clase `Pelicula`.
    * La tabla `RESENIA` establece el valor por defecto del campo `APROBADO` en `0` (no aprobado), a diferencia del `1` en el codigo del PDF. Las claves foráneas `ID_USUARIO` y `ID_PELICULA` se implementaron correctamente.

2.  **Registro de Usuarios y Datos Personales:**
    * Debido a la fusión de `DATOS_PERSONALES` en `USUARIO`, el flujo de registro descripto en el PDF (primero datos personales, luego usuario) se **combinó en un solo paso** para cada tipo de usuario (`registrarCliente` y `registrarAdmin`).
    * **No se implementó** la funcionalidad o el atributo `Domicilio` para la clase `Cliente` (mencionado como código comentado en `Cliente.java` y como duda en `Dudas Entregable2.txt`). Por lo tanto, ni Clientes ni Administradores manejan información de domicilio en esta implementación.

3.  **Validación de Administrador:**
    * Aunque el PDF no lo especificaba explícitamente, se implementó una **validación mediante un token** (`TokenAdm`) para el registro de nuevos Administradores como medida de seguridad.

4.  **Clases del Modelo:**
    * La clase `Staff` se mantuvo simple, conteniendo solo `nombre` y `rol`, utilizada principalmente para el `director` de la `Pelicula`. La lista `elenco` fue comentada en `Contenido.java`.
    * Se utilizó un enumerativo `GeneroPelicula` para el campo `GENERO` de `Pelicula`, tal como se requería.

5.  **Validaciones Específicas:**
    * Las validaciones de datos (nombres sin números, formato de email, DNI único) se implementaron en la capa de servicio (`AppImple.java`) usando expresiones regulares simples y chequeos directos. La unicidad del DNI también está asegurada por la restricción `UNIQUE` en la base de datos. La validación de email se limita a verificar dominios comunes en lugar de una validación de formato completa.

6.  **Otros:**
    * La fecha y hora (`FECHA_HORA`) en `RESENIA` se almacena como texto en formato ISO.
    * Los listados ordenados solicitados se implementaron usando `Comparator` de Java.

7.  **Precarga de Datos:**
    * Para facilitar las pruebas y la demostración, la aplicación **precarga un conjunto de datos iniciales** la primera vez que se ejecuta y detecta que la base de datos está vacía.
    * **Datos precargados:**
        * **Usuarios:** Se cargan 5 usuarios: 2 con rol de `ADMIN` y 3 con rol de `CLIENTE`.
            * **Admin 1:** `admin1@streaming.com` / `admin123`
            * **Cliente 1:** `carlos@gmail.com` / `pass123`
        * **Películas:** Se cargan 5 películas de diferentes géneros, como "Inception", "The Dark Knight" y "The Matrix".
        * **Reseñas:** Se cargan 5 reseñas de ejemplo, algunas ya aprobadas y otras pendientes de aprobación, para permitir probar ambas funcionalidades.


## Estructura del Proyecto

El proyecto sigue una estructura modular básica:

* `src/Control`: Contiene la clase principal `App` con el menú de consola.
* `src/Catalogo`: Clases del modelo relacionadas con el contenido (Pelicula, Contenido, Staff, Reseña).
* `src/Usuario`: Clases del modelo para usuarios (Usuario, Cliente, Administrador).
* `src/ENUM`: Enumerativos como `GeneroPelicula`.
* `src/DAO`: Interfaces y clases de implementación para el acceso a datos (Patrón DAO).
* `src/DataBase`: Clase para la gestión de la conexión a la base de datos SQLite (`ConexionBD`).
* `src/Servicio`: Interfaces y clases de implementación para la lógica de negocio (`IAppServicio`, `AppImple`).
* `doc/`: Carpeta con documentación Javadoc generada.
* `lib/`: (Si aplica) Librerías externas como el driver JDBC de SQLite.
* `streaming.db`: Archivo de la base de datos SQLite generado en la raíz del proyecto.


La aplicación mostrará un menú en la consola para interactuar con las funcionalidades implementadas. La base de datos (`streaming.db`) se creará automáticamente en el directorio raíz del proyecto la primera vez que se ejecute la aplicación.