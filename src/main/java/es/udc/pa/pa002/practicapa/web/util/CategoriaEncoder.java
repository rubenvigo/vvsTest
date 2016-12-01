package es.udc.pa.pa002.practicapa.web.util;

import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class CategoriaEncoder.
 */
public class CategoriaEncoder implements ValueEncoder<Categoria> {

/** The user service. */
private UserService userService;

/**
 * Instantiates a new categoria encoder.
 *
 * @param userService
 *            the user service
 */
public CategoriaEncoder(final UserService userService) {
    super();
    this.userService = userService;
}

@Override
public final String toClient(final Categoria value) {
    return String.valueOf(value.getIdCategoria());
}

@Override
public final Categoria toValue(final String id) {
    try {
        return userService.findCategoryById(Long.parseLong(id));
    } catch (NumberFormatException | InstanceNotFoundException e) {
        return null;
    }

}

}
