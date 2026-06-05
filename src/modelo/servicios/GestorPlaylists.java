package modelo.servicios;

import modelo.entidades.Audio;
import modelo.entidades.Playlist;
import modelo.entidades.Usuario;
import util.GeneradorId;
import  modelo.servicios.GestorUsuarios;

/**
 * Administra las listas de reproduccion de los usuarios, gestionando su creacion,
 * modificacion de contenido y sincronizacion con el catalogo global.
 */
public class GestorPlaylists {

    private GestorUsuarios gestorUsuarios;
    private GestorCatalogo gestorCatalogo;

    public GestorPlaylists(GestorUsuarios gestorUsuarios, GestorCatalogo gestorCatalogo) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorCatalogo = gestorCatalogo;
    }

    /**
     * Crea una nueva playlist para un usuario, genera su ID automaticamente y la guarda en disco.
     */
    public void crearPlaylist(String idUsuarioOAlias, String nombre) {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        if (usuario != null) {
            Playlist nueva = new Playlist(nombre, usuario.getNombre());
            String nuevoId = nueva.getId();
            if (usuario.getBiblioteca().agregarPlaylist(nueva)) {
                System.out.println("Playlist '" + nombre + "' creada con exito. ID: " + nuevoId);
                gestorUsuarios.guardarCambios();
            } else {
                System.err.println("Error: Ya existe una playlist con ese identificador.");
            }
        } else {
            System.err.println("Usuario no encontrado.");
        }
    }

    /**
     * Agrega un audio del catalogo a una playlist especifica del usuario.
     */
    public void agregarAudioAPlaylist(String idUsuarioOAlias, String idPlaylist, String idAudio) {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        Audio audio = gestorCatalogo.buscarAudioPorId(idAudio);

        if (usuario != null && audio != null) {
            Playlist playlist = usuario.getBiblioteca().buscarPorId(idPlaylist);
            if (playlist != null) {
                if (playlist.agregarAudio(audio)) {
                    System.out.println("Agregado: " + audio.getTitulo() + " -> " + playlist.getNombre());
                    usuario.refrescarConteoBiblioteca();
                    gestorUsuarios.guardarCambios();
                } else {
                    System.out.println("El audio ya se encuentra en la playlist.");
                }
            } else {
                System.err.println("Playlist no encontrada.");
            }
        } else {
            System.err.println("Usuario o Audio no existen.");
        }
    }

    /**
     * Remueve un audio especifico de una playlist de un usuario y guarda los cambios.
     */
    public void removerAudioDePlaylist(String idUsuarioOAlias, String idPlaylist, String idAudio) {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        if (usuario == null) {
            System.out.println("Error: Usuario no encontrado.");
            return;
        }

        Playlist playlist = usuario.getBiblioteca().buscarPorId(idPlaylist);
        if (playlist == null) {
            System.out.println("Error: No se encontro la playlist con ID '" + idPlaylist + "'.");
            return;
        }

        boolean eliminado = playlist.getContenido().removeIf(audio -> audio.getId().equals(idAudio));

        if (eliminado) {
            System.out.println("¡Audio removido exitosamente de '" + playlist.getNombre() + "'!");
            usuario.refrescarConteoBiblioteca();
            gestorUsuarios.guardarCambios();
        } else {
            System.out.println("Aviso: Ese audio no se encontraba en la playlist.");
        }
    }

    /**
     * Elimina una playlist completa de la biblioteca.
     */
    public void eliminarPlaylist(String idUsuarioOAlias, String idPlaylist) {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        if (usuario != null) {
            if (usuario.getBiblioteca().eliminarPlaylist(idPlaylist)) {
                System.out.println("Playlist eliminada.");
                usuario.refrescarConteoBiblioteca();
                gestorUsuarios.guardarCambios();
            } else {
                System.err.println("No se pudo eliminar la playlist.");
            }
        } else {
            System.err.println("Usuario no encontrado.");
        }
    }

    /**
     * Muestra el contenido detallado de una playlist especifica por consola.
     */
    public void mostrarContenidoPlaylist(String idUsuarioOAlias, String idPlaylist) {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        if (usuario != null) {
            Playlist p = usuario.getBiblioteca().buscarPorId(idPlaylist);
            if (p != null) {
                System.out.println("\n--- Playlist: " + p.getNombre() + " ---");
                if (p.getContenido().isEmpty()) {
                    System.out.println(" (La playlist esta vacia)");
                } else {
                    for (Audio a : p.getContenido()) {
                        System.out.println(" > " + a.toString());
                    }
                }
            } else {
                System.err.println("Playlist no encontrada.");
            }
        } else {
            System.err.println("Usuario no encontrado.");
        }

    }
    public java.util.List<Audio> getTodosLosAudios() {
        return gestorCatalogo.getTodosLosAudios();
    }


}