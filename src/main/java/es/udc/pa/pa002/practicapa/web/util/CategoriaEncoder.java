package es.udc.pa.pa002.practicapa.web.util;

import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class CategoriaEncoder implements ValueEncoder<Categoria> {

private UserService userService;

public CategoriaEncoder(UserService userService) {
    super();
    this.userService = userService;
}

@Override
public String toClient(Categoria value) {
    return String.valueOf(value.getIdCategoria());
}

@Override
public Categoria toValue(String id) {
    try {
        return userService.findCategoryById(Long.parseLong(id));
    } catch (NumberFormatException | InstanceNotFoundException e) {
        return null;
    }

}

}
