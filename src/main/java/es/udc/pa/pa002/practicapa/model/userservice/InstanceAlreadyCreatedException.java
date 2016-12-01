package es.udc.pa.pa002.practicapa.model.userservice;

/**
 * The Class InstanceAlreadyCreatedException.
 */
@SuppressWarnings("serial")
public class InstanceAlreadyCreatedException extends Exception {

/**
 * Instantiates a new instance already created exception.
 *
 * @param message
 *            the message
 */
public InstanceAlreadyCreatedException(final String message) {
    super(message);
}

}
