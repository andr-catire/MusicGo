package musicgo.ui;

import musicgo.persistencia.RepositorioDatos;
import musicgo.util.LimpiarTerminal;
import musicgo.servicios.*;

/**
 * Menu principal de la plataforma.
 * Enlaza los submenus y centraliza el uso del repositorio.
 */
public class MenuPrincipal {

    private final GestorUsuarios gestorUsuarios;
    private final GestorCatalogo gestorCatalogo;
    private final GestorPlaylists gestorPlaylists;
    private final GestorReproduccion gestorReproduccion;
    private final GestorCompras gestorCompras;
    private final GestorEstadisticas gestorEstadisticas;

    /**
     * Inicializa los gestores compartiendo una unica instancia del repositorio.
     */
    public MenuPrincipal() {
        RepositorioDatos repositorio = new RepositorioDatos();
        this.gestorUsuarios = new GestorUsuarios(repositorio);

        this.gestorCatalogo = new GestorCatalogo();
        int n = gestorCatalogo.cargarDesdeJson();
        System.out.println("[Inicio] Sistema listo con " + n + " audios.");

        this.gestorPlaylists = new GestorPlaylists(gestorUsuarios, gestorCatalogo);
        this.gestorReproduccion = new GestorReproduccion(gestorUsuarios, gestorCatalogo);
        this.gestorEstadisticas = new GestorEstadisticas(gestorUsuarios);

        this.gestorCompras = new GestorCompras(gestorCatalogo, gestorUsuarios, repositorio);
    }

    public void mostrar() {
        boolean cerrarAplicacion = false;
        while (!cerrarAplicacion) {
            LimpiarTerminal.limpiar();
            ConsolaUtil.linea();
            System.out.println("          PLATAFORMA MUSICGO          ");
            ConsolaUtil.linea();
            System.out.println(" 1) Gestion de usuarios");
            System.out.println(" 2) Exploracion del catalogo");
            System.out.println(" 3) Mis Playlists");
            System.out.println(" 4) Reproducir contenido");
            System.out.println(" 5) Comprar producto");
            System.out.println(" 6) Mis Estadisticas");
            System.out.println(" 7) Guardar cambios");
            System.out.println(" 0) Salir");
            ConsolaUtil.linea();

            int op = ConsolaUtil.leerEnteroEnRango("Seleccione una opcion: ",0 ,7);

            switch (op) {
                case 1 -> new MenuUsuarios(gestorUsuarios).mostrar();
                case 2 -> new MenuCatalogo(gestorCatalogo).mostrar();
                case 3 -> new MenuPlaylists(gestorPlaylists, gestorUsuarios).mostrar();
                case 4 -> new MenuReproduccion(gestorReproduccion, gestorUsuarios).mostrar();
                case 5 -> new MenuCompras(gestorCompras, gestorUsuarios).mostrar();
                case 6 -> new MenuEstadisticas(gestorEstadisticas, gestorUsuarios).mostrar();
                case 7 -> guardarDatos();
                case 0 -> {
                    if (ConsolaUtil.confirmar("¿Guardar cambios antes de salir?")) {
                        guardarDatos();
                    }
                    cerrarAplicacion = true;
                    System.out.println("¡Hasta la proxima!");
                }
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void guardarDatos() {
        System.out.println("[Sistema] Guardando datos de usuarios y compras...");
        gestorUsuarios.guardarCambios();
        System.out.println("[Exito] Datos sincronizados en la carpeta data/.");
    }
}