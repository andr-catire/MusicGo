package musicgo.ui;

import java.util.List;
import java.util.Scanner;
import musicgo.modelo.Audio;
import musicgo.modelo.Cancion;
import musicgo.modelo.EpisodioPodcast;
import musicgo.modelo.Producto;
import  musicgo.util.Validadores;
import musicgo.modelo.Usuario;

/**
 * Helpers para leer datos desde la consola sin que cada menu se llene
 * de codigo repetido (DRY).
 *
 * <p>Mantiene un unico Scanner reutilizable para evitar el clasico
 * problema de cerrar System.in.</p>
 *
 * @author Equipo MusicGO
 */
public class ConsolaUtil {

    private static final Scanner scanner = new Scanner(System.in);

    private ConsolaUtil() {
    }

    /**
     * Lee una linea, mostrando previamente un prompt.
     */
    public static String leerLinea(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Alias de leerLinea por compatibilidad.
     */
    public static String leerTexto(String prompt) {
        return leerLinea(prompt);
    }

    /**
     * Lee un entero. Si el usuario escribe basura devuelve {@code defecto}.
     */
    public static int leerEntero(String prompt, int defecto) {
        System.out.print(prompt);
        String linea = scanner.nextLine();
        try {
            return Integer.parseInt(linea.trim());
        } catch (NumberFormatException ex) {
            System.out.println("(numero invalido, se usa " + defecto + ")");
            return defecto;
        }
    }

    /**
     * Pregunta s/n; cualquier cosa distinta de "s" se considera "no".
     */
    public static boolean confirmar(String prompt) {
        String resp = leerLinea(prompt + " (s/n): ").trim().toLowerCase();
        return resp.equals("s") || resp.equals("si");
    }

    public static void pausar() {
        System.out.print("\n[Enter para continuar]");
        scanner.nextLine();
    }

    public static void linea() {
        System.out.println("==========================================");
    }


    /**
     * Imprime una tabla alineada con la lista de productos.
     */
    public static void imprimirTablaProductos(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            System.out.println("   (No hay productos disponibles para mostrar)");
            return;
        }

        String separador = "+------------+--------------------------------+---------+------------+";
        String formato = "| %-10s | %-30s | %-7s | %-10s |%n";

        System.out.println(separador);
        System.out.printf(formato, "ID", "NOMBRE DEL PRODUCTO", "PRECIO", "TIPO");
        System.out.println(separador);

        for (Producto p : productos) {
            System.out.printf(formato,
                    p.getId(),
                    recortar(p.getNombre(), 30),
                    "$" + p.getPrecio(),
                    recortar(p.getTipo(), 10)
            );
        }
        System.out.println(separador);
    }

    /**
     * Imprime una tabla alineada con la lista de audios (Canciones y Podcasts).
     */
    public static void imprimirTablaAudios(List<Audio> audios) {
        if (audios == null || audios.isEmpty()) {
            System.out.println("   (No hay audios para mostrar)");
            return;
        }

        String separador = "+------------+--------------------------------+----------------------+------------+";
        String formato = "| %-10s | %-30s | %-20s | %-10s |%n";

        System.out.println(separador);
        System.out.printf(formato, "ID", "TÍTULO", "ARTISTA/AUTOR", "DURACIÓN");
        System.out.println(separador);

        for (Audio a : audios) {
            // Identificar si es canción o podcast para sacar el creador
            String creador = "Desconocido";
            if (a instanceof Cancion) {
                creador = ((Cancion) a).getArtista();
            } else if (a instanceof EpisodioPodcast) {
                creador = ((EpisodioPodcast) a).getAnfitrion();
            }
            int duracionTotal = a.getDuracionSegundos();
            String tiempo = (duracionTotal / 60) + ":" + String.format("%02d", (duracionTotal % 60));

            System.out.printf(formato,
                    a.getId(),
                    recortar(a.getTitulo(), 30),
                    recortar(creador, 20),
                    tiempo);
        }
        System.out.println(separador);
    }

    /**
     * Helper para evitar que textos muy largos deformen la tabla ASCII.
     */
    private static String recortar(String texto, int max) {
        if (texto == null) return "";
        if (texto.length() > max) {
            return texto.substring(0, max - 3) + "...";
        }
        return texto;
    }

    /**
     * Lee un numero entero desde la consola asegurando que se encuentre dentro de un rango.
     * Mantiene un bucle de reintento si la entrada no es un numero o esta fuera del limite.
     *
     * @param prompt Mensaje a mostrar al usuario solicitando la entrada.
     * @param min Valor minimo aceptado (inclusivo).
     * @param max Valor maximo aceptado (inclusivo).
     * @return El numero entero validado ingresado por el usuario.
     */
    public static int leerEnteroEnRango(String prompt, int min, int max) {
        int valor;
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine();
            try {
                valor = Integer.parseInt(linea.trim());
                if (musicgo.util.Validadores.estaEnRango(valor, min, max)) {
                    return valor;
                } else {
                    System.out.println("Error: Opcion fuera de rango. Debe ser entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error: Entrada no valida. Por favor, ingrese un numero.");
            }
        }
    }
    /**
     * Imprime una tabla amigable con la lista de usuarios.
     */
    public static void imprimirTablaUsuarios(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println(" (No hay usuarios registrados en el sistema) ");
            return;
        }

        String separador = "+----------+----------------------+--------------------------------+-----------+---------+";
        String formato = "| %-8s | %-20s | %-30s | %-9s | %-7s |%n";

        System.out.println(separador);
        System.out.printf(formato, "ID", "NOMBRE/ALIAS", "CORREO", "PLAYLISTS", "COMPRAS");
        System.out.println(separador);

        for (Usuario u : usuarios) {
            int playlists = u.getBiblioteca() != null ? u.getBiblioteca().cantidadPlaylists() : 0;
            int compras = u.getHistorialCompras() != null ? u.getHistorialCompras().size() : 0;

            System.out.printf(formato,
                    u.getId(),
                    recortar(u.getNombre(), 20),
                    recortar(u.getCorreo(), 30),
                    playlists,
                    compras);
        }
        System.out.println(separador);
    }
}