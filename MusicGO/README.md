# MusicGO - Plataforma de Streaming (Fase 1)

Proyecto de la cátedra de Programación Orientada a Objetos (UCAB).
Profesor: Marcel J. Castro G.

Simulador por consola de una plataforma de streaming de canciones y podcasts,
con gestión de usuarios, playlists, catálogo, compras de productos especiales
y estadísticas de escucha. La persistencia se realiza completamente en
archivos JSON; no se requieren librerías externas.

## Estructura del proyecto

```
MusicGO/
├── src/musicgo/             Codigo fuente
│   ├── Main.java
│   ├── interfaces/          Identificable, Reproducible, Comprable
│   ├── modelo/              Audio, Cancion, Producto, Usuario, ...
│   ├── persistencia/        JsonParser, JsonWriter, RepositorioDatos
│   ├── servicios/           Gestores (Usuarios, Catalogo, Playlists, ...)
│   ├── excepciones/         Excepciones del dominio
│   └── ui/                  Menus por consola
├── data/                    Archivos JSON (catalogo + usuarios)
├── docs/                    Diagramas UML y JavaDoc generado
├── lib/                     (vacio - no se usan dependencias)
└── README.md
```

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
