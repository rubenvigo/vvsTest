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

/**
 * The Class FindEventos.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FindEventos {

/** The categoria select model. */
@Property
private SelectModel categoriaSelectModel;

/** The select model factory. */
@Inject
private SelectModelFactory selectModelFactory;

/** The keywords. */
@Property
private String keywords;

/** The categoria. */
@Property
private Categoria categoria;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@SessionState(create = false)
private UserSession userSession;

/** The found eventos. */
@InjectPage
private FoundEventos foundEventos;

/**
 * On activate.
 */
final void onActivate() {
    List<Categoria> categorias = userService.findCategories();
    categoriaSelectModel = selectModelFactory.create(categorias, "nombre");
}

/**
 * Gets the categoria encoder.
 *
 * @return the categoria encoder
 */
public final CategoriaEncoder getCategoriaEncoder() {
    return new CategoriaEncoder(userService);
}

/**
 * On provide completions from keywords.
 *
 * @param partial
 *            the partial
 * @return the list
 */
final List<String> onProvideCompletionsFromKeywords(final String partial) {
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

/**
 * On success.
 *
 * @return the object
 */
final Object onSuccess() {
    foundEventos.setKeywords(keywords);
    if (categoria != null) {
        foundEventos.setIdCategoria(categoria.getIdCategoria());
    } else {
        foundEventos.setIdCategoria(null);
    }
    return foundEventos;

}
}
