# MusicGO - Plataforma de Streaming (Fase 1)

Proyecto de la cátedra de Programación Orientada a Objetos (UCAB).
Profesor: Marcel J. Castro G.

Simulador por consola de una plataforma de streaming de canciones y podcasts,
con gestión de usuarios, playlists, catálogo, compras de productos especiales
y estadísticas de escucha. La persistencia se realiza completamente en
archivos JSON; no se requieren librerías externas.

## Estructura del proyecto

```text
## Estructura del proyecto

```text
MusicGO/
├── src/musicgo/             Codigo fuente
│   ├── Main.java
│   ├── interfaces/          Identificable, Reproducible, Comprable
│   ├── modelo/              Audio, Cancion, Producto, Usuario, Mensaje...
│   ├── persistencia/        JsonParser, JsonWriter, RepositorioDatos
│   ├── servicios/           Gestores (Usuarios, Catalogo, Reproduccion, Playlists...)
│   ├── excepciones/         Excepciones del dominio
│   ├── utiles/              GeneradorId, ValidadorEmail
│   └── ui/                  Menus interactivos por consola
├── data/                    Archivos JSON de persistencia
│   ├── canciones.json
│   ├── catalogo.json
│   ├── productos.json
│   └── usuarios.json
├── docs/                    Diagramas UML y JavaDoc generado
├── lib/                     (vacio - no se usan dependencias externas)
└── README.md