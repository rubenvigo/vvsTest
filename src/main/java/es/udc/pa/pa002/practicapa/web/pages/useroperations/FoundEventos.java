package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.EventoBlock;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.EventoGridDataSource;
import es.udc.pa.pa002.practicapa.web.util.UserSession;

/**
 * The Class FoundEventos.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FoundEventos {

/** The Constant EVENTOS_PER_PAGE. */
private final static int EVENTOS_PER_PAGE = 10;

/** The evento block. */
private EventoBlock eventoBlock;

/** The start index. */
private int startIndex = 0;

/** The keywords. */
private String keywords;

/** The id categoria. */
private Long idCategoria;

/** The evento. */
@Property
private Evento evento;

/** The evento grid data source. */
private EventoGridDataSource eventoGridDataSource;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@SessionState(create = false)
private UserSession userSession;

/**
 * Gets the format.
 *
 * @return the format
 */
public Format getFormat() {
    return new SimpleDateFormat("dd/MM/yyy HH:mm");
}

/**
 * Gets the eventos.
 *
 * @return the eventos
 */
public List<Evento> getEventos() {
    return eventoBlock.getEventos();
}

/**
 * Gets the eventos per page.
 *
 * @return the eventos per page
 */
public int getEventosPerPage() {
    return EVENTOS_PER_PAGE;
}

/**
 * Gets the id categoria.
 *
 * @return the id categoria
 */
public Long getIdCategoria() {
    return idCategoria;
}

/**
 * Sets the id categoria.
 *
 * @param idCategoria
 *            the new id categoria
 */
public void setIdCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
}

/**
 * Gets the keywords.
 *
 * @return the keywords
 */
public String getKeywords() {
    return keywords;
}

/**
 * Gets the evento grid data source.
 *
 * @return the evento grid data source
 */
public EventoGridDataSource getEventoGridDataSource() {
    return eventoGridDataSource;
}

/**
 * Sets the keywords.
 *
 * @param keywords
 *            the new keywords
 */
public void setKeywords(String keywords) {
    this.keywords = keywords;
}

/**
 * On passivate.
 *
 * @return the object[]
 */
Object[] onPassivate() {
    return new Object[] {keywords, idCategoria, startIndex };
}

/**
 * On activate.
 *
 * @param keywords
 *            the keywords
 * @param idCategoria
 *            the id categoria
 * @param startIndex
 *            the start index
 */
void onActivate(String keywords, Long idCategoria, int startIndex) {
    this.keywords = keywords;
    this.idCategoria = idCategoria;
    this.startIndex = startIndex;

    if (userSession != null) {
        eventoGridDataSource = new EventoGridDataSource(userService, keywords,
                idCategoria, userSession.isAdmin());
        eventoBlock = userService.findEventos(keywords, idCategoria,
                userSession.isAdmin(), startIndex, EVENTOS_PER_PAGE);
    } else {
        eventoGridDataSource = new EventoGridDataSource(userService, keywords,
                idCategoria, false);
        eventoBlock = userService.findEventos(keywords, idCategoria, false,
                startIndex, EVENTOS_PER_PAGE);
    }
}

}
