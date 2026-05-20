package musicgo.servicios;

import musicgo.excepciones.UsuarioNoEncontradoException;
import musicgo.excepciones.UsuarioYaExisteException;
import musicgo.modelo.Usuario;
import musicgo.persistencia.RepositorioDatos;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private final List<Usuario> usuarios;
    private final RepositorioDatos repositorio;

    public GestorUsuarios(RepositorioDatos repositorio) {
        this.repositorio = repositorio;
        List<Usuario> cargados = repositorio.cargarUsuarios();
        this.usuarios = (cargados != null) ? cargados : new ArrayList<>();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario buscarPorIdOAlias(String criterio) {
        if (criterio == null || criterio.isBlank()) return null;
        for (Usuario u : usuarios) {
            if (u.getId().equalsIgnoreCase(criterio) || u.getNombre().equalsIgnoreCase(criterio)) {
                return u;
            }
        }
        return null;
    }

    public void registrar(String alias, String correo) throws UsuarioYaExisteException {
        if (buscarPorIdOAlias(alias) != null) {
            throw new UsuarioYaExisteException("El alias '" + alias + "' ya esta en uso.");
        }
        Usuario nuevo = new Usuario(alias, correo);
        usuarios.add(nuevo);
        guardarCambios();
    }

    public void modificar(String aliasActual, String nuevoAlias, String nuevoCorreo) throws UsuarioNoEncontradoException, UsuarioYaExisteException {
        Usuario u = buscarPorIdOAlias(aliasActual);
        if (u == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado.");
        }

        if (nuevoAlias != null && !nuevoAlias.isBlank() && !nuevoAlias.equalsIgnoreCase(aliasActual)) {
            if (buscarPorIdOAlias(nuevoAlias) != null) {
                throw new UsuarioYaExisteException("El nuevo alias ya esta en uso.");
            }
            u.setNombre(nuevoAlias);
        }

        if (nuevoCorreo != null && !nuevoCorreo.isBlank() && !nuevoCorreo.equalsIgnoreCase(u.getCorreo())) {
            u.setCorreo(nuevoCorreo);
        }

        guardarCambios();
    }

    public void eliminar(String alias) throws UsuarioNoEncontradoException {
        Usuario u = buscarPorIdOAlias(alias);
        if (u == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado.");
        }
        usuarios.remove(u);
        guardarCambios();
    }

    public void guardarCambios() {
        repositorio.guardarUsuarios(this.usuarios);
    }
}