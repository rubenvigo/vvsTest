package es.udc.pa.pa002.practicapa.model.userservice;

/**
 * The Class OpcionApuestaAlreadySolvedException.
 */
@SuppressWarnings("serial")
public class OpcionApuestaAlreadySolvedException extends Exception {

/**
 * Instantiates a new opcion apuesta already solved exception.
 *
 * @param mensaje
 *            the mensaje
 */
public OpcionApuestaAlreadySolvedException(final String mensaje) {
    super(mensaje);
}
}
