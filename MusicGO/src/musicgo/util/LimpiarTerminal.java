package musicgo.util; // Ajusta el paquete según tu estructura (ej: musicgo.ui)

/**
 * Clase de utilidad dedicada exclusivamente a gestionar el estado visual
 * de la terminal (limpieza de pantalla).
 * * @author Equipo MusicGO
 */
public class LimpiarTerminal {

    /**
     * Limpia por completo la consola de comandos detectando de forma
     * automática el sistema operativo del usuario.
     */
    public static void limpiar() {
        try {
            String sistemaOperativo = System.getProperty("os.name");

            if (sistemaOperativo.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}