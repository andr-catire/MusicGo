package musicgo.ui;

import musicgo.modelo.Audio;
import musicgo.modelo.Cancion;
import musicgo.modelo.EpisodioPodcast;
import musicgo.modelo.Producto;
import musicgo.servicios.GestorCatalogo;
import musicgo.util.LimpiarTerminal;

import java.util.ArrayList;
import java.util.List;

/**
 * Interfaz de usuario para la exploracion del inventario global.
 * Permite filtrar por tipo de contenido, buscar por nombre y gestionar la carga.
 *
 * @author Equipo MusicGO
 */
public class MenuCatalogo {

    private final GestorCatalogo gestor;

    public MenuCatalogo(GestorCatalogo gestor) {
        this.gestor = gestor;
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            ConsolaUtil.linea();
            System.out.println("            EXPLORACION DEL CATALOGO            ");
            ConsolaUtil.linea();
            System.out.println(" 1) (Re)Cargar datos desde archivos JSON");
            System.out.println(" 2) Listar solo canciones");
            System.out.println(" 3) Listar solo episodios de podcast");
            System.out.println(" 4) Listar productos de la tienda");
            System.out.println(" 5) Listar catalogo completo");
            System.out.println(" 6) Buscar por nombre (Filtro)");
            System.out.println(" 0) Volver al menu principal");
            ConsolaUtil.linea();

            int op = ConsolaUtil.leerEnteroEnRango("Seleccione una opcion: ", 0 , 6);
            switch (op) {
                case 1 -> recargar();
                case 2 -> listarCanciones();
                case 3 -> listarPodcasts();
                case 4 -> listarProductos();
                case 5 -> listarTodo();
                case 6 -> buscarPorNombre();
                case 0 -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void recargar() {
        int n = gestor.cargarDesdeJson();
        System.out.println("[Sistema] " + n + " elementos de audio sincronizados.");
        ConsolaUtil.pausar();
    }

    private void listarCanciones() {
        System.out.println("\n--- LISTADO DE CANCIONES ---");
        List<Audio> canciones = new ArrayList<>();
        for (Audio a : gestor.getTodosLosAudios()) {
            if (a instanceof Cancion) canciones.add(a);
        }
        ConsolaUtil.imprimirTablaAudios(canciones);
        ConsolaUtil.pausar();
    }

    private void listarPodcasts() {
        System.out.println("\n--- LISTADO DE PODCASTS ---");
        List<Audio> podcasts = new ArrayList<>();
        for (Audio a : gestor.getTodosLosAudios()) {
            if (a instanceof EpisodioPodcast) podcasts.add(a);
        }
        ConsolaUtil.imprimirTablaAudios(podcasts);
        ConsolaUtil.pausar();
    }

    private void listarProductos() {
        System.out.println("\n--- PRODUCTOS EN TIENDA ---");
        List<Producto> productos = gestor.getTodosLosProductos();
        ConsolaUtil.imprimirTablaProductos(productos);
        ConsolaUtil.pausar();
    }

    private void listarTodo() {
        System.out.println("\n--- CATALOGO GLOBAL DE AUDIOS ---");
        ConsolaUtil.imprimirTablaAudios(gestor.getTodosLosAudios());

        System.out.println("\n--- INVENTARIO DE PRODUCTOS ---");
        ConsolaUtil.imprimirTablaProductos(gestor.getTodosLosProductos());
        ConsolaUtil.pausar();
    }

    private void buscarPorNombre() {
        String query = ConsolaUtil.leerLinea("Nombre a buscar: ").trim().toLowerCase();

        if (query.isEmpty()) {
            System.out.println("Busqueda cancelada.");
            return;
        }

        List<Audio> resultados = new ArrayList<>();
        for (Audio a : gestor.getTodosLosAudios()) {
            if (a.getTitulo().toLowerCase().contains(query)) {
                resultados.add(a);
            }
        }

        System.out.println("\n COINCIDENCIAS PARA: '" + query + "'");
        if (resultados.isEmpty()) {
            System.out.println("No se hallaron resultados.");
        } else {
            ConsolaUtil.imprimirTablaAudios(resultados);
        }
        ConsolaUtil.pausar();
    }
}