@echo off
REM Compiling Java source files
javac -cp "lib/*;src" -d "bin" ^
    src/comparador/ComparadorPeliculaDuracion.java ^
    src/comparador/ComparadorPeliculaGenero.java ^
    src/comparador/ComparadorPeliculaTitulo.java ^
    src/comparador/ComparadorUsuarioEmail.java ^
    src/comparador/ComparadorUsuarioNombre.java ^
    src/enums/GeneroPelicula.java ^
    src/excepciones/CredencialesInvalidasException.java ^
    src/excepciones/DatoInvalidoException.java ^
    src/excepciones/UsuarioYaExisteException.java ^
    src/modelo/Usuario.java ^
    src/modelo/Cliente.java ^
    src/modelo/Administrador.java ^
    src/modelo/Staff.java ^
    src/modelo/Contenido.java ^
    src/modelo/Pelicula.java ^
    src/modelo/Reseña.java ^
    src/dao/PeliculaDAO.java ^
    src/dao/PeliculaDAOimple.java ^
    src/dao/UsuarioDAO.java ^
    src/dao/UsuarioDAOimple.java ^
    src/dao/ReseñaDAO.java ^
    src/dao/ReseñaDAOimple.java ^
    src/database/ConexionBD.java ^
    src/database/SetupBD.java ^
    src/database/AutoCargaPeliculas.java ^
    src/servicio/AppImple.java ^
    src/vista/VistaLogin.java ^
    src/vista/VistaPrincipal.java ^
    src/vista/VistaRegistro.java ^
    src/controlador/AppGUI.java ^
    src/controlador/ControladorLogin.java ^
    src/controlador/ControladorPrincipal.java ^
    src/controlador/ControladorRegistro.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
) else (
    echo.
    echo Compilation failed!
    pause
)
