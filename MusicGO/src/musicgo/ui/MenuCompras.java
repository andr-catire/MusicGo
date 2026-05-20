package musicgo.ui;

import musicgo.modelo.Producto;
import musicgo.modelo.Usuario;
import musicgo.servicios.GestorCompras;
import musicgo.servicios.GestorUsuarios;
import musicgo.util.LimpiarTerminal;

import java.util.List;

/**
 * Submenu dedicado a la gestion de transacciones comerciales, permitiendo
 * a los usuarios visualizar el catalogo de productos y realizar adquisiciones.
 * Adaptado para compras multiples continuas.
 *
 * @author Equipo MusicGO
 */
public class MenuCompras {

    private final GestorCompras gestor;
    private final GestorUsuarios gestorUsuarios;

    /**
     * Construye el menu de compras vinculando los gestores de negocio necesarios.
     *
     * @param gestor Instancia del gestor de compras.
     * @param gestorUsuarios Instancia del gestor de usuarios para validacion.
     */
    public MenuCompras(GestorCompras gestor, GestorUsuarios gestorUsuarios) {
        this.gestor = gestor;
        this.gestorUsuarios = gestorUsuarios;
    }

    /**
     * Despliega la interfaz de usuario para la seleccion de productos y
     * ejecucion del proceso de compra mediante entrada por consola.
     */
    public void mostrar() {
        LimpiarTerminal.limpiar();
        ConsolaUtil.linea();
        System.out.println("            MODULO DE COMPRAS            ");
        ConsolaUtil.linea();

        String criterioUsuario = ConsolaUtil.leerLinea("Alias o ID del usuario: ").trim();
        Usuario u = gestorUsuarios.buscarPorIdOAlias(criterioUsuario);

        if (u == null) {
            System.out.println("Error: Usuario no identificado en el sistema.");
            ConsolaUtil.pausar();
            return;
        }

        System.out.println("\n--- Bienvenido, " + u.getNombre() + " ---");
        System.out.println("=== PRODUCTOS DISPONIBLES EN LA TIENDA ===");

        List<Producto> productos = gestor.getProductosDisponibles();

        if (productos.isEmpty()) {
            System.out.println("[!] No hay productos registrados en el inventario.");
            ConsolaUtil.pausar();
            return;
        } else {
            ConsolaUtil.imprimirTablaProductos(productos);
        }

        System.out.println("\n(Escribe '0' en cualquier momento para salir de la tienda)");

        while (true) {
            String entrada = ConsolaUtil.leerLinea("\nID o Nombre del producto a comprar: ").trim();

            if (entrada.equals("0")) {
                System.out.println("Saliendo de la tienda. ¡Gracias por tu visita!");
                break;
            }

            Producto productoSeleccionado = null;
            for (Producto p : productos) {
                if (p.getId().equalsIgnoreCase(entrada) || p.getNombre().equalsIgnoreCase(entrada)) {
                    productoSeleccionado = p;
                    break;
                }
            }

            if (productoSeleccionado != null) {
                if (ConsolaUtil.confirmar("¿Desea proceder con el pago del producto '" + productoSeleccionado.getNombre() + "'?")) {
                    boolean exito = gestor.comprarProducto(u.getId(), productoSeleccionado.getId());

                    if (exito) {
                        System.out.println(" > ¡Exito! Has comprado: " + productoSeleccionado.getNombre());
                    } else {
                        System.out.println("AVISO: La transaccion fue rechazada por el sistema.");
                    }
                } else {
                    System.out.println("Compra de '" + productoSeleccionado.getNombre() + "' descartada.");
                }
            } else {
                System.out.println("Error: No se encontro ningun producto con ese ID o Nombre.");
            }
        }
        ConsolaUtil.pausar();
    }
}