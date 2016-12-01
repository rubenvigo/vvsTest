package es.udc.pa.pa002.practicapa.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;

/**
 * The Class EventoGridDataSource.
 */
public class EventoGridDataSource implements GridDataSource {

/** The user service. */
private UserService userService;

/** The keywords. */
private String keywords;

/** The id categoria. */
private Long idCategoria;

/** The eventos. */
private List<Evento> eventos;

/** The admin. */
private boolean admin;

/** The start index. */
private int startIndex;

/** The evento not found. */
private boolean eventoNotFound;

/**
 * Instantiates a new evento grid data source.
 *
 * @param userService
 *            the user service
 * @param keywords
 *            the keywords
 * @param idCategoria
 *            the id categoria
 * @param admin
 *            the admin
 */
public EventoGridDataSource(final UserService userService,
        final String keywords, final Long idCategoria, final boolean admin) {

    this.userService = userService;
    this.idCategoria = idCategoria;
    this.keywords = keywords;
    this.admin = admin;

}

@Override
public final int getAvailableRows() {

    int numberEventos = userService.getNumberOfEventos(keywords, idCategoria,
            admin);
    if (numberEventos == 0) {
        eventoNotFound = true;
    }
    return numberEventos;

}

@Override
public final Class<Evento> getRowType() {
    return Evento.class;
}

@Override
public final Object getRowValue(final int index) {
    return eventos.get(index - this.startIndex);
}

@Override
public final void prepare(final int startIndex, final int endIndex,
        final List<SortConstraint> sortConstraints) {
    eventos = userService.findEventos(keywords, idCategoria, admin, startIndex,
            endIndex - startIndex + 1).getEventos();
    this.startIndex = startIndex;

}

/**
 * Gets the evento not found.
 *
 * @return the evento not found
 */
public final boolean getEventoNotFound() {
    return eventoNotFound;
}

}
