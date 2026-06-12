package excepciones;

public class PlaylistNoEncontradaException extends RuntimeException {
    public PlaylistNoEncontradaException(String message) {
        super(message);
    }
}
