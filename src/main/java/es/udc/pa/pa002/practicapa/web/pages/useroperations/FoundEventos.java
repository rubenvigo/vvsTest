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

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FoundEventos {

private final static int EVENTOS_PER_PAGE = 10;

private EventoBlock eventoBlock;

private int startIndex = 0;

private String keywords;

private Long idCategoria;

@Property
private Evento evento;

private EventoGridDataSource eventoGridDataSource;

@Inject
private UserService userService;

@SessionState(create = false)
private UserSession userSession;

public Format getFormat() {
    return new SimpleDateFormat("dd/MM/yyy HH:mm");
}

public List<Evento> getEventos() {
    return eventoBlock.getEventos();
}

public int getEventosPerPage() {
    return EVENTOS_PER_PAGE;
}

public Long getIdCategoria() {
    return idCategoria;
}

public void setIdCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
}

public String getKeywords() {
    return keywords;
}

public EventoGridDataSource getEventoGridDataSource() {
    return eventoGridDataSource;
}

public void setKeywords(String keywords) {
    this.keywords = keywords;
}

Object[] onPassivate() {
    return new Object[] {keywords, idCategoria, startIndex };
}

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
