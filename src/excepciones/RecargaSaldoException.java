package excepciones;

public class RecargaSaldoException extends RuntimeException {
    public RecargaSaldoException(String mensaje) {
        super(mensaje);
    }
}
