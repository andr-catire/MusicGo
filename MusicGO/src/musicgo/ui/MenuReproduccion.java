package musicgo.ui;

import musicgo.excepciones.ContenidoNoEncontradoException;
import musicgo.modelo.Mensaje;
import musicgo.modelo.Usuario;
import musicgo.servicios.GestorReproduccion;
import musicgo.servicios.GestorUsuarios;
import musicgo.servicios.GestorCatalogo;
import musicgo.util.LimpiarTerminal;

/**
 * Submenu para simular la reproduccion de un contenido.
 *
 * @author Equipo MusicGO
 */
public class MenuReproduccion {

    private final GestorReproduccion gestor;
    private final GestorUsuarios gestorUsuarios;

    public MenuReproduccion(GestorReproduccion gestor, GestorUsuarios gestorUsuarios) {
        this.gestor = gestor;
        this.gestorUsuarios = gestorUsuarios;
    }

    /**
     * Muestra la interfaz interactiva para simular la reproduccion de contenido.
     * Solicita las credenciales del usuario, despliega el catalogo global de audios
     * junto con las playlists personales, y permite al usuario ingresar el ID o
     * el nombre de un audio o playlist para iniciar la reproduccion.
     */
    public void mostrar() {
        LimpiarTerminal.limpiar();
        ConsolaUtil.linea();
        System.out.println("            SIMULACION DE REPRODUCCION            ");
        ConsolaUtil.linea();

        String alias = ConsolaUtil.leerLinea("Alias o ID del usuario: ").trim();

        Usuario u = gestorUsuarios.buscarPorIdOAlias(alias);

        if (u == null) {
            System.out.println("Error: El usuario '" + alias + "' no existe en el sistema.");
            ConsolaUtil.pausar();
            return;
        }

        System.out.println("\n--- CATALOGO DE AUDIOS DISPONIBLES ---");
        ConsolaUtil.imprimirTablaAudios(gestor.getTodosLosAudios());
        System.out.println("\n--- TUS PLAYLISTS ---");
        java.util.List<musicgo.modelo.Playlist> playlists = u.getBiblioteca().getPlaylists();

        if (playlists.isEmpty()) {
            System.out.println(" (No tienes playlists creadas aun)");
        } else {
            for (musicgo.modelo.Playlist p : playlists) {
                System.out.println(" > ID: " + p.getId() + " | Nombre: " + p.getNombre() + " | Audios: " + p.getContenido().size());
            }
        }
        System.out.println("--------------------------------------\n");

        String entrada = ConsolaUtil.leerLinea("Seleccione el audio o playlist a reproducir (ID o Nombre): ").trim();

        musicgo.modelo.Playlist playlistSeleccionada = null;
        for (musicgo.modelo.Playlist p : playlists) {
            if (p.getId().equalsIgnoreCase(entrada) || p.getNombre().equalsIgnoreCase(entrada)) {
                playlistSeleccionada = p;
                break;
            }
        }

        try {
            if (playlistSeleccionada != null) {
                System.out.println("\nIniciando Playlist: " + playlistSeleccionada.getNombre());

                if (playlistSeleccionada.getContenido().isEmpty()) {
                    System.out.println("La playlist esta vacia.");
                } else {
                    for (musicgo.modelo.Audio audio : playlistSeleccionada.getContenido()) {
                        Mensaje m = gestor.reproducir(u, audio.getId());
                        System.out.println(m);
                    }
                }
            }
            else {
                Mensaje m = gestor.reproducir(u, entrada);
                System.out.println(m);
            }
        } catch (ContenidoNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        ConsolaUtil.pausar();
    }
}