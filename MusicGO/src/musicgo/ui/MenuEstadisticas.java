package musicgo.ui;

import musicgo.modelo.Usuario;
import musicgo.servicios.GestorEstadisticas;
import musicgo.servicios.GestorUsuarios;
import musicgo.util.LimpiarTerminal;

/**
 * Submenu para visualizar estadisticas individuales por usuario.
 *
 * @author Equipo MusicGO
 */
public class MenuEstadisticas {

    private final GestorEstadisticas gestor;
    private final GestorUsuarios gestorUsuarios;

    /**
     * Inicializa el menu de estadisticas con los gestores necesarios
     * para la consulta de datos.
     */
    public MenuEstadisticas(GestorEstadisticas gestor, GestorUsuarios gestorUsuarios) {
        this.gestor = gestor;
        this.gestorUsuarios = gestorUsuarios;
    }

    /**
     * Solicita la identidad de un usuario para desplegar su reporte detallado
     * de actividad y consumo en la plataforma.
     */
    public void mostrar() {
        LimpiarTerminal.limpiar();
        String alias = ConsolaUtil.leerLinea("Alias o ID del usuario: ").trim();

        Usuario u = gestorUsuarios.buscarPorIdOAlias(alias);

        if (u == null) {
            System.out.println("No existe ese usuario.");
        } else {
            u.refrescarConteoBiblioteca();
            gestor.mostrarReporteCompleto(u.getId());
        }
        ConsolaUtil.pausar();
    }
}