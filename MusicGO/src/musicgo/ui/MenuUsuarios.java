package musicgo.ui;

import musicgo.excepciones.UsuarioNoEncontradoException;
import musicgo.excepciones.UsuarioYaExisteException;
import musicgo.modelo.Usuario;
import musicgo.servicios.GestorUsuarios;
import musicgo.util.LimpiarTerminal;

public class MenuUsuarios {

    private final GestorUsuarios gestor;

    public MenuUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            LimpiarTerminal.limpiar();
            ConsolaUtil.linea();
            System.out.println(" GESTION DE USUARIOS");
            ConsolaUtil.linea();
            System.out.println(" 1) Registrar usuario");
            System.out.println(" 2) Listar usuarios");
            System.out.println(" 3) Consultar usuario");
            System.out.println(" 4) Modificar usuario");
            System.out.println(" 5) Eliminar usuario");
            System.out.println(" 0) Volver");
            ConsolaUtil.linea();

            int op = ConsolaUtil.leerEnteroEnRango("Opcion: ", 0,5);
            switch (op) {
                case 1 -> registrar();
                case 2 -> listar();
                case 3 -> consultar();
                case 4 -> modificar();
                case 5 -> eliminar();
                case 0 -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void registrar() {
        String alias = ConsolaUtil.leerLinea("Alias: ").trim();
        String correo = ConsolaUtil.leerLinea("Correo: ").trim();
        try {
            gestor.registrar(alias, correo);
            System.out.println("Usuario registrado con exito.");
        } catch (UsuarioYaExisteException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        ConsolaUtil.pausar();
    }

    private void listar() {
        System.out.println("\n--- LISTADO DE USUARIOS REGISTRADOS ---");
        ConsolaUtil.imprimirTablaUsuarios(gestor.getUsuarios());
        ConsolaUtil.pausar();
    }

    private void consultar() {
        String alias = ConsolaUtil.leerLinea("Alias o ID del usuario: ").trim();
        Usuario u = gestor.buscarPorIdOAlias(alias);

        if (u == null) {
            System.out.println("Error: No existe el usuario '" + alias + "' en el sistema.");
        } else {
            System.out.println("\n==========================================");
            System.out.println("             PERFIL DE USUARIO            ");
            System.out.println("==========================================");
            System.out.println(" > ID     : " + u.getId());
            System.out.println(" > Nombre : " + u.getNombre());
            System.out.println(" > Correo : " + u.getCorreo());
            System.out.println("==========================================\n");
        }

        ConsolaUtil.pausar();
    }

    private void modificar() {
        String alias = ConsolaUtil.leerLinea("Alias actual: ").trim();
        String nuevoAlias = ConsolaUtil.leerLinea("Nuevo alias (enter para mantener): ").trim();
        String nuevoCorreo = ConsolaUtil.leerLinea("Nuevo correo (enter para mantener): ").trim();

        try {
            gestor.modificar(alias, nuevoAlias, nuevoCorreo);
            System.out.println("Usuario actualizado.");
        } catch (UsuarioNoEncontradoException | UsuarioYaExisteException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        ConsolaUtil.pausar();
    }

    private void eliminar() {
        String alias = ConsolaUtil.leerLinea("Alias a eliminar: ").trim();
        Usuario u = gestor.buscarPorIdOAlias(alias);
        if (u == null) {
            System.out.println("ERROR: No se encontró ningún usuario con el alias o ID '" + alias + "'.");
            ConsolaUtil.pausar();
            return;
        }
        if (!ConsolaUtil.confirmar("¿Confirma la eliminacion del usuario '" + u.getNombre() + "'?")) {
            System.out.println("Cancelado.");
            ConsolaUtil.pausar();
            return;
        }
        try {
            gestor.eliminar(alias);
            System.out.println("Usuario eliminado correctamente.");
        } catch (UsuarioNoEncontradoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        ConsolaUtil.pausar();
    }
}