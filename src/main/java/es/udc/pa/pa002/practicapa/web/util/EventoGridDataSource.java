package es.udc.pa.pa002.practicapa.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;

public class EventoGridDataSource implements GridDataSource {

private UserService userService;
private String keywords;
private Long idCategoria;
private List<Evento> eventos;
private boolean admin;
private int startIndex;
private boolean eventoNotFound;

public EventoGridDataSource(UserService userService, String keywords,
        Long idCategoria, boolean admin) {

    this.userService = userService;
    this.idCategoria = idCategoria;
    this.keywords = keywords;
    this.admin = admin;

}

public int getAvailableRows() {

    int numberEventos = userService.getNumberOfEventos(keywords, idCategoria,
            admin);
    if (numberEventos == 0) {
        eventoNotFound = true;
    }
    return numberEventos;

}

public Class<Evento> getRowType() {
    return Evento.class;
}

public Object getRowValue(int index) {
    return eventos.get(index - this.startIndex);
}

public void prepare(int startIndex, int endIndex,
        List<SortConstraint> sortConstraints) {
    eventos = userService.findEventos(keywords, idCategoria, admin, startIndex,
            endIndex - startIndex + 1).getEventos();
    this.startIndex = startIndex;

}

public boolean getEventoNotFound() {
    return eventoNotFound;
}

}
