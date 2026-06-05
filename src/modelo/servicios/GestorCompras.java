package modelo.servicios;

import modelo.entidades.*;
import modelo.persistencia.RepositorioDatos;

import java.time.LocalDateTime;
import java.util.List;
import util.GeneradorId;

/**
 * Coordina la adquisicion de productos por parte de los usuarios, gestionando
 * la transaccion, el historial del usuario y la persistencia de los datos.
 *
 * @author Equipo MusicGO
 */
public class GestorCompras {

    private final GestorCatalogo gestorCatalogo;
    private final GestorUsuarios gestorUsuarios;
    private final RepositorioDatos repositorio;

    /**
     * Construye el gestor vinculando los servicios necesarios y el repositorio unico.
     *
     * @param catalogo Instancia del gestor de catalogo.
     * @param usuarios Instancia del gestor de usuarios.
     * @param repositorio Repositorio de datos compartido.
     */
    public GestorCompras(GestorCatalogo catalogo, GestorUsuarios usuarios, RepositorioDatos repositorio) {
        this.gestorCatalogo = catalogo;
        this.gestorUsuarios = usuarios;
        this.repositorio = repositorio;
    }

    /**
     * Procesa la compra de un producto validando existencias y persistiendo cambios.
     */
    public boolean comprarProducto(String idUsuarioOAlias, String idProductoONombre) {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        if (usuario == null) {
            System.err.println("Error: El usuario '" + idUsuarioOAlias + "' no existe.");
            return false;
        }

        Producto producto = null;
        for (Producto p : gestorCatalogo.getTodosLosProductos()) {
            if (p.getId().equalsIgnoreCase(idProductoONombre) || p.getNombre().equalsIgnoreCase(idProductoONombre)) {
                producto = p;
                break;
            }
        }

        if (producto == null) {
            System.err.println("Error: El producto '" + idProductoONombre + "' no existe.");
            return false;
        }

        Compra nuevaCompra = new Compra(producto.getId(), usuario.getId(), producto.getPrecio(), LocalDateTime.now());
        usuario.registrarCompra(nuevaCompra);

        if (producto instanceof modelo.entidades.PaqueteTopTen) {
            modelo.entidades.PaqueteTopTen paquete = (modelo.entidades.PaqueteTopTen) producto;
            modelo.entidades.Playlist nuevaPlaylist = new modelo.entidades.Playlist(paquete.getNombre(), usuario.getNombre());

            for (String idCancion : paquete.getIdsCanciones()) {
                modelo.entidades.Audio audio = gestorCatalogo.buscarAudioPorId(idCancion);
                if (audio != null) {
                    nuevaPlaylist.agregarAudio(audio);
                }
            }
            usuario.getBiblioteca().getPlaylists().add(nuevaPlaylist);

            System.out.println(" > [Sistema] Se ha creado tu nueva playlist: '" + paquete.getNombre() + "' con " + nuevaPlaylist.getContenido().size() + " canciones.");
        }

        System.out.println("\n--- COMPRA EXITOSA ---");
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Producto: " + producto.getNombre());
        System.out.println("----------------------\n");

        gestorUsuarios.guardarCambios();

        return true;
    }

    /**
     * Retorna los productos cargados en el gestor de catalogo.
     */
    public List<Producto> getProductosDisponibles() {
        return gestorCatalogo.getTodosLosProductos();
    }
}