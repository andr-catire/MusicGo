package musicgo;

import musicgo.ui.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   INICIANDO MUSIC GO - CARGANDO DATOS  ");
        System.out.println("========================================");

        try {
            MenuPrincipal app = new MenuPrincipal();
            app.mostrar();

        } catch (Exception e) {
            System.err.println("Error critico al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
