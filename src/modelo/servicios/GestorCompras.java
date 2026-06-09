package modelo.servicios;

import modelo.entidades.*;
import modelo.persistencia.RepositorioDatos;
import excepciones.SaldoInsuficienteException;

import java.time.LocalDateTime;
import java.util.List;

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
    /**
     * Procesa la compra. Lanza SaldoInsuficienteException si el usuario no tiene fondos.
     */
    public void comprarProducto(String idUsuarioOAlias, String idProductoONombre) throws SaldoInsuficienteException {
        Usuario usuario = gestorUsuarios.buscarPorIdOAlias(idUsuarioOAlias);
        if (usuario == null) {
            System.err.println("Error: El usuario '" + idUsuarioOAlias + "' no existe.");
            return;
        }

        Producto producto = gestorCatalogo.buscarProductoPorIdONombre(idProductoONombre);
        if (producto == null) {
            System.err.println("Error: El producto '" + idProductoONombre + "' no existe.");
            return;
        }

        if (usuario.getSaldo() < producto.getPrecio()) {

            throw new SaldoInsuficienteException("Saldo insuficiente para comprar " + producto.getNombre() + ". Tienes: $" + usuario.getSaldo() + ", Cuesta: $" + producto.getPrecio());
        }

        usuario.descontarSaldo(producto.getPrecio());
        Compra nuevaCompra = new Compra(producto.getId(), usuario.getNombre(), producto.getPrecio(), LocalDateTime.now());
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
        System.out.println("Has comprado: " + producto.getNombre());
        System.out.println("Saldo restante: $" + usuario.getSaldo());
        System.out.println("----------------------\n");

        gestorUsuarios.guardarCambios();
    }

    /**
     * Retorna los productos cargados en el gestor de catalogo.
     */
    public List<Producto> getProductosDisponibles() {
        return gestorCatalogo.getTodosLosProductos();
    }
}