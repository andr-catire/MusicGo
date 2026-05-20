package musicgo.modelo;

import musicgo.interfaces.Identificable;
import musicgo.util.GeneradorId;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario de la plataforma MusicGO.
 *
 * Cada usuario tiene un alias unico (que actua como identificador),
 * un correo electronico, su biblioteca personal y un historial de
 * compras.
 *
 * <p>Relaciones:</p>
 * <ul>
 * <li>Composicion con {@link Biblioteca} y {@link Estadisticas}.</li>
 * <li>Agregacion con {@link Compra} (las compras pueden vivir
 * en un historial general aparte para reportes).</li>
 * </ul>
 *
 * @author Equipo MusicGO
 */
public class Usuario implements Identificable {

    private String id;
    private String nombre;
    private String correo;
    private Biblioteca biblioteca;
    private Estadisticas estadisticas;
    private List<Compra> historialCompras;

    public Usuario(String nombre, String correo) {
        this.id = GeneradorId.generarId("USR");
        this.nombre = nombre;
        this.correo = correo;
        this.biblioteca = new Biblioteca();
        this.estadisticas = new Estadisticas();
        this.historialCompras = new ArrayList<>();
    }

    /**
     * Constructor pensado para reconstruir un usuario desde JSON con
     * sus datos ya completos.
     */
    public Usuario(String id, String nombre, String correo, Biblioteca biblioteca,
                   Estadisticas estadisticas, List<Compra> historialCompras) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.biblioteca = (biblioteca != null) ? biblioteca : new Biblioteca();
        this.estadisticas = (estadisticas != null) ? estadisticas : new Estadisticas();
        this.historialCompras = (historialCompras != null) ? historialCompras : new ArrayList<>();
    }

    /**
     * El alias funciona como id en este sistema.
     */
    @Override
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public Biblioteca getBiblioteca() {
        return biblioteca;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public List<Compra> getHistorialCompras() {
        return historialCompras;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Agrega una compra al historial y actualiza las estadisticas.
     */
    public void registrarCompra(Compra c) {
        if (c == null) return;
        historialCompras.add(c);
        estadisticas.registrarCompra();
    }

    /**
     * Recalcula la cantidad de canciones en la biblioteca a partir de
     * las playlists. Se llama despues de agregar/remover audios.
     */
    public void refrescarConteoBiblioteca() {
        if (estadisticas != null && biblioteca != null) {
            estadisticas.setCancionesEnBiblioteca(biblioteca.totalCanciones());
        }
    }

    @Override
    public String toString() {
        return "Usuario{id='" + id + "', nombre='" + nombre + "', correo='" + correo
                + "', playlists=" + (biblioteca != null ? biblioteca.cantidadPlaylists() : 0)
                + ", compras=" + historialCompras.size() + "}";
    }
}