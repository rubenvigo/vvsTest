package es.udc.pa.pa002.practicapa.web.util;

import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class OpcionApuestaEncoder.
 */
public class OpcionApuestaEncoder implements ValueEncoder<OpcionApuesta> {

/** The user service. */
private UserService userService;

/**
 * Instantiates a new opcion apuesta encoder.
 *
 * @param userService
 *            the user service
 */
public OpcionApuestaEncoder(UserService userService) {
    this.userService = userService;
}

@Override
public final String toClient(OpcionApuesta value) {
    return String.valueOf(value.getIdOpcionApuesta());
}

@Override
public final OpcionApuesta toValue(String id) {
    try {
        return userService.findOpcionApuestaById(Long.parseLong(id));
    } catch (NumberFormatException e) {
        return null;
    } catch (InstanceNotFoundException e) {
        return null;
    }
}

}
