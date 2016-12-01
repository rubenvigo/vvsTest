package es.udc.pa.pa002.practicapa.model.userservice;

/**
 * The Class InputValidationException.
 */
@SuppressWarnings("serial")
public class InputValidationException extends Exception {

/**
 * Instantiates a new input validation exception.
 *
 * @param message
 *            the message
 */
public InputValidationException(final String message) {
    super(message);
}
}
