# MusicGO - Plataforma de Streaming (Fase 1)

Proyecto de la cátedra de Programación Orientada a Objetos (UCAB).
Profesor: Marcel J. Castro G.

Simulador por consola de una plataforma de streaming de canciones y podcasts,
con gestión de usuarios, playlists, catálogo, compras de productos especiales
y estadísticas de escucha. La persistencia se realiza completamente en
archivos JSON; no se requieren librerías externas.

## Estructura del proyecto

Directory structure:
└── MusicGO/
    ├── README.md
    ├── lib/
    │   ├── README.txt
    │   └── .gitkeep
    └── src/
        └── musicgo/
            ├── Main.java
            ├── excepciones/
            │   ├── ContenidoNoEncontradoException.java
            │   ├── UsuarioNoEncontradoException.java
            │   └── UsuarioYaExisteException.java
            ├── interfaces/
            │   ├── Comprable.java
            │   ├── Identificable.java
            │   └── Reproducible.java
            ├── modelo/
            │   ├── ArteVisualAlbum.java
            │   ├── Audio.java
            │   ├── Biblioteca.java
            │   ├── Cancion.java
            │   ├── Catalogo.java
            │   ├── Compra.java
            │   ├── EpisodioPodcast.java
            │   ├── Estadisticas.java
            │   ├── Mensaje.java
            │   ├── PaqueteTopTen.java
            │   ├── Playlist.java
            │   ├── Producto.java
            │   └── Usuario.java
            ├── persistencia/
            │   ├── JsonParser.java
            │   ├── JsonWriter.java
            │   └── RepositorioDatos.java
            ├── servicios/
            │   ├── GestorCatalogo.java
            │   ├── GestorCompras.java
            │   ├── GestorEstadisticas.java
            │   ├── GestorPlaylists.java
            │   ├── GestorReproduccion.java
            │   └── GestorUsuarios.java
            ├── ui/
            │   ├── ConsolaUtil.java
            │   ├── MenuCatalogo.java
            │   ├── MenuCompras.java
            │   ├── MenuEstadisticas.java
            │   ├── MenuPlaylists.java
            │   ├── MenuPrincipal.java
            │   ├── MenuReproduccion.java
            │   └── MenuUsuarios.java
            └── util/
                ├── GeneradorId.java
                ├── LimpiarTerminal.java
                └── Validadores.java


## Compilar

Desde la raíz del proyecto, en una terminal con JDK 8 o superior:

### Linux / macOS

```bash
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
```

### Windows (PowerShell)

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName | Out-File sources.txt -Encoding ASCII
javac -d out "@sources.txt"
```

## Ejecutar

```bash
java -cp out musicgo.Main
```

Por defecto la aplicación lee y escribe en la carpeta `data/` ubicada en el
directorio actual de ejecución. Para apuntar a otra ubicación:

```bash
java -cp out musicgo.Main /ruta/a/otra/carpeta
```

## Generar la documentación JavaDoc

```bash
javadoc -d docs/javadoc -sourcepath src -subpackages musicgo -encoding UTF-8 -charset UTF-8
```

La documentación se genera en `docs/javadoc/index.html`.

## Diagramas UML

Las fuentes están en `docs/`:

- `DiagramaClases.puml` y `DiagramaClases.png` — diagrama de clases
- `DiagramaSecuencia.puml` y `DiagramaSecuencia.png` — diagrama de secuencia
  para el caso de uso "Gestión de Playlists"

Los archivos `.puml` se pueden re-renderizar en https://www.plantuml.com/plantuml
o con la extensión PlantUML para VS Code.

## Datos de ejemplo

`data/catalogo.json` trae variedad de canciones, episodios de podcast y  productos.
`data/usuarios.json` trae base de usuarios de prueba .
La aplicación carga estos archivos al iniciar y los reescribe al salir.

## Notas

- La aplicación no usa librerías externas. El parser y writer de JSON son
  parte del proyecto (`persistencia/JsonParser.java`, `JsonWriter.java`).
- La codificación de los archivos JSON es UTF-8.
- Los IDs siguen la convención: `C###` canciones, `E###` episodios,
  `P###` productos y playlists. Los usuarios se identifican por su alias.
