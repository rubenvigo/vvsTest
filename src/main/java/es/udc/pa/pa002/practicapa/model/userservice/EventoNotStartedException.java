package es.udc.pa.pa002.practicapa.model.userservice;

/**
 * The Class EventoNotStartedException.
 */
@SuppressWarnings("serial")
public class EventoNotStartedException extends Exception {

/**
 * Instantiates a new evento not started exception.
 *
 * @param mensaje
 *            the mensaje
 */
public EventoNotStartedException(final String mensaje) {
    super(mensaje);
}
}
