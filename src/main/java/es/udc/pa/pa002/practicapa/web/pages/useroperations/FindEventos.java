package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.CategoriaEncoder;
import es.udc.pa.pa002.practicapa.web.util.UserSession;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FindEventos {

@Property
private SelectModel categoriaSelectModel;

@Inject
SelectModelFactory selectModelFactory;

@Property
private String keywords;

@Property
private Categoria categoria;

@Inject
private UserService userService;

@SessionState(create = false)
private UserSession userSession;

@InjectPage
private FoundEventos foundEventos;

void onActivate() {
    List<Categoria> categorias = userService.findCategories();
    categoriaSelectModel = selectModelFactory.create(categorias, "nombre");
}

public CategoriaEncoder getCategoriaEncoder() {
    return new CategoriaEncoder(userService);
}

List<String> onProvideCompletionsFromKeywords(String partial) {
    List<String> matches = new ArrayList<String>();
    boolean isAdmin = false;
    if (userSession != null) {
        isAdmin = userSession.isAdmin();
    }
    List<Evento> eventos = userService.findEventos(partial, null, isAdmin, 0,
            1000).getEventos();
    for (Evento evento : eventos) {
        matches.add(evento.getNombre());

    }

    return matches;
}

Object onSuccess() {
    foundEventos.setKeywords(keywords);
    if (categoria != null) {
        foundEventos.setIdCategoria(categoria.getIdCategoria());
    } else
        foundEventos.setIdCategoria(null);
    return foundEventos;

}
}
