package musicgo.ui;

import musicgo.excepciones.ContenidoNoEncontradoException;
import musicgo.modelo.Audio;
import musicgo.modelo.Playlist;
import musicgo.modelo.Usuario;
import musicgo.servicios.GestorPlaylists;
import musicgo.servicios.GestorUsuarios;
import musicgo.util.LimpiarTerminal;

/**
 * Submenu para la gestion de playlists.
 * Adaptado a la arquitectura por IDs y busqueda por nombres.
 *
 * @author Equipo MusicGO
 */
public class MenuPlaylists {

    private final GestorPlaylists gestorPl;
    private final GestorUsuarios gestorUsuarios;

    /**
     * Inicializa el menu de playlists con los gestores correspondientes.
     */
    public MenuPlaylists(GestorPlaylists gestorPl, GestorUsuarios gestorUsuarios) {
        this.gestorPl = gestorPl;
        this.gestorUsuarios = gestorUsuarios;
    }

    /**
     * Despliega el menu interactivo de playlists y gestiona las opciones seleccionadas.
     */
    public void mostrar() {
        LimpiarTerminal.limpiar();
        Usuario u = pedirUsuario();
        if (u == null) return;

        boolean salir = false;
        while (!salir) {
            LimpiarTerminal.limpiar();
            ConsolaUtil.linea();
            System.out.println(" PLAYLISTS de " + u.getNombre());
            ConsolaUtil.linea();
            System.out.println(" 1) Crear playlist");
            System.out.println(" 2) Listar playlists");
            System.out.println(" 3) Ver contenido de una playlist");
            System.out.println(" 4) Agregar audio a playlist");
            System.out.println(" 5) Remover audio de playlist");
            System.out.println(" 6) Eliminar playlist");
            System.out.println(" 0) Volver");
            ConsolaUtil.linea();

            int op = ConsolaUtil.leerEnteroEnRango("Opcion: ",0 ,6);
            switch (op) {
                case 1: crear(u);    break;
                case 2: listar(u);   break;
                case 3: verContenido(u); break;
                case 4: agregarAudio(u); break;
                case 5: removerAudio(u); break;
                case 6: eliminar(u); break;
                case 0: salir = true; break;
                default: System.out.println("Opcion no valida.");
            }
        }
    }

    /**
     * Solicita por consola la identidad del usuario y lo busca en el sistema.
     *
     * @return El usuario encontrado, o null si no existe.
     */
    private Usuario pedirUsuario() {
        String nombreABuscar = ConsolaUtil.leerLinea("Nombre o Alias del usuario: ").trim();
        Usuario u = gestorUsuarios.buscarPorIdOAlias(nombreABuscar);
        if (u == null) {
            System.out.println("No existe ese usuario.");
            ConsolaUtil.pausar();
        }
        return u;
    }

    /**
     * Solicita los datos y crea una nueva playlist para el usuario.
     */
    private void crear(Usuario u) {
        String nombre = ConsolaUtil.leerLinea("Nombre de la nueva playlist: ").trim();
        gestorPl.crearPlaylist(u.getId(), nombre);
        ConsolaUtil.pausar();
    }

    /**
     * Muestra en pantalla todas las playlists asociadas al usuario.
     */
    private void listar(Usuario u) {
        var pls = u.getBiblioteca().getPlaylists();
        if (pls.isEmpty()) {
            System.out.println("(este usuario no tiene playlists)");
        } else {
            for (Playlist p : pls) {
                System.out.println("  " + p);
            }
        }
        ConsolaUtil.pausar();
    }

    /**
     * Solicita el identificador o nombre de una playlist y muestra los audios que contiene.
     */
    private void verContenido(Usuario u) {
        imprimirPlaylistsUsuario(u);
        String entradaPlaylist = ConsolaUtil.leerLinea("Ingrese el ID o Nombre de la playlist a visualizar: ").trim();
        Playlist p = encontrarPlaylist(u, entradaPlaylist);

        if (p == null) {
            System.out.println("Error: No se encontro ninguna playlist con ese ID o Nombre.");
            ConsolaUtil.pausar();
            return;
        }

        System.out.println("\nContenido de '" + p.getNombre() + "':");
        int duracionTotal = 0;

        if (p.getContenido().isEmpty()) {
            System.out.println("  (vacia)");
        } else {
            for (Audio a : p.getContenido()) {
                System.out.println("  " + a);
                duracionTotal += a.getDuracionSegundos();
            }
        }

        System.out.println("Duracion total: " + duracionTotal + " segundos (" + (duracionTotal/60) + " min)");
        ConsolaUtil.pausar();
    }

    /**
     * Solicita la playlist (por ID/Nombre) y permite agregar multiples audios
     * en un bucle hasta que el usuario decida salir.
     */
    private void agregarAudio(Usuario u) {
        imprimirPlaylistsUsuario(u);
        String entradaAgregar = ConsolaUtil.leerLinea("Ingrese el ID o Nombre de la playlist destino (o '0' para cancelar): ").trim();

        if (entradaAgregar.equals("0")) return; // Sale inmediatamente si el usuario se arrepiente

        Playlist pAgregar = encontrarPlaylist(u, entradaAgregar);

        if (pAgregar != null) {
            System.out.println("\n--- CATALOGO DE AUDIOS DISPONIBLES ---");
            ConsolaUtil.imprimirTablaAudios(gestorPl.getTodosLosAudios());
            System.out.println();

            while (true) {
                String idAudio = ConsolaUtil.leerLinea("Ingrese ID del audio a agregar (o '0' para terminar): ").trim();

                if (idAudio.equals("0")) {
                    System.out.println("Terminaste de agregar audios a '" + pAgregar.getNombre() + "'.");
                    break;
                }

                gestorPl.agregarAudioAPlaylist(u.getId(), pAgregar.getId(), idAudio);
                System.out.println(" > Intento de agregado enviado. Puedes agregar otro.");
            }

        } else {
            System.out.println("Error: No se encontro ninguna playlist con ese ID o Nombre.");
            ConsolaUtil.pausar();
        }
    }

    /**
     * Permite remover multiples audios de una playlist en un bucle
     * hasta que la playlist quede vacia o el usuario decida salir.
     */
    private void removerAudio(Usuario u) {
        imprimirPlaylistsUsuario(u);
        String entradaRemoverPL = ConsolaUtil.leerLinea("Ingrese el ID o Nombre de la playlist (o '0' para salir): ").trim();

        if (entradaRemoverPL.equals("0")) return;

        Playlist pRemover = encontrarPlaylist(u, entradaRemoverPL);

        if (pRemover != null) {

            while (true) {
                if (pRemover.getContenido().isEmpty()) {
                    System.out.println("\nLa playlist '" + pRemover.getNombre() + "' esta vacia. Saliendo...");
                    break;
                }

                System.out.println("\n--- AUDIOS EN LA PLAYLIST ---");
                ConsolaUtil.imprimirTablaAudios(pRemover.getContenido());

                String entradaAudio = ConsolaUtil.leerLinea("Ingrese ID o Titulo del audio a remover (o '0' para terminar): ").trim();

                if (entradaAudio.equals("0")) {
                    System.out.println("Terminaste de remover audios.");
                    break;
                }

                Audio audioARemover = null;
                for (Audio a : pRemover.getContenido()) {
                    if (a.getId().equalsIgnoreCase(entradaAudio) || a.getTitulo().equalsIgnoreCase(entradaAudio)) {
                        audioARemover = a;
                        break;
                    }
                }

                if (audioARemover != null) {
                    gestorPl.removerAudioDePlaylist(u.getId(), pRemover.getId(), audioARemover.getId());
                    System.out.println(" > Audio removido. La lista se ha actualizado.");
                } else {
                    System.out.println("Error: El audio indicado no existe en esta playlist.");
                }
            }

        } else {
            System.out.println("Error: No se encontro ninguna playlist con ese ID o Nombre.");
            ConsolaUtil.pausar();
        }
    }

    /**
     * Borra permanentemente una playlist de la biblioteca del usuario buscando por ID o Nombre.
     */
    private void eliminar(Usuario u) {
        imprimirPlaylistsUsuario(u);
        String entradaEliminar = ConsolaUtil.leerLinea("Ingrese el ID o Nombre de la playlist a eliminar: ").trim();
        Playlist pEliminar = encontrarPlaylist(u, entradaEliminar);

        if (pEliminar != null) {
            gestorPl.eliminarPlaylist(u.getId(), pEliminar.getId());
        } else {
            System.out.println("Error: No se encontro ninguna playlist con ese ID o Nombre.");
        }
        ConsolaUtil.pausar();
    }

    /**
     * Imprime en la consola la lista de playlists pertenecientes a un usuario.
     * Muestra el ID, el nombre y la cantidad de audios que contiene cada una.
     * * NOTA SOBRE VISIBILIDAD: Este metodo es privado (private) porque es una
     * funcion auxiliar (helper) de uso exclusivo para la logica interna de esta
     * clase (MenuPlaylists). Al hacerlo privado, evitamos que otras partes del
     * sistema lo llamen por error y mantenemos el codigo encapsulado y seguro.
     *
     * @param u El usuario cuyas playlists se van a mostrar en pantalla.
     */
    private void imprimirPlaylistsUsuario(Usuario u) {
        System.out.println("\n--- TUS PLAYLISTS ---");
        java.util.List<Playlist> playlists = u.getBiblioteca().getPlaylists();
        if (playlists.isEmpty()) {
            System.out.println(" (No tienes playlists creadas aun)");
        } else {
            for (Playlist p : playlists) {
                System.out.println(" > ID: " + p.getId() + " | Nombre: " + p.getNombre() + " | Audios: " + p.getContenido().size());
            }
        }
        System.out.println("--------------------------------------");
    }

    /**
     * Busca y retorna una playlist especifica dentro de la biblioteca de un usuario,
     * permitiendo la busqueda tanto por su identificador unico (ID) como por su nombre.
     * * NOTA SOBRE VISIBILIDAD: Este metodo es privado (private) ya que su unico
     * proposito es reutilizar codigo dentro de las distintas opciones de este menu
     * (agregar, ver, eliminar). No forma parte del comportamiento publico de la clase,
     * por lo que se oculta al resto del programa para evitar acoplamiento innecesario.
     *
     * @param u       El usuario propietario de la biblioteca donde se realizara la busqueda.
     * @param entrada El texto introducido en consola por el usuario (puede ser ID o Nombre).
     * @return El objeto Playlist si se encuentra una coincidencia, o null si no existe.
     */
    private Playlist encontrarPlaylist(Usuario u, String entrada) {
        for (Playlist p : u.getBiblioteca().getPlaylists()) {
            if (p.getId().equalsIgnoreCase(entrada) || p.getNombre().equalsIgnoreCase(entrada)) {
                return p;
            }
        }
        return null;
    }
}