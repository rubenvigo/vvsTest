package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.util.Calendar;
import java.util.Set;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.admin.EspecificarGanadoras;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class TipoApuestaDetails {

private Long idTipoApuesta;

private TipoApuesta tipoApuesta;

@Property
private OpcionApuesta opcionApuesta;

@Inject
private UserService userService;

@Property
@SessionState(create = false)
private UserSession userSession;

@InjectPage
private EspecificarGanadoras especificarGanadoras;

public Evento getEvento() {
    return tipoApuesta.getEvento();
}

public boolean getIsAdmin() {
    return (userSession != null) && (userSession.isAdmin());
}

public boolean getNotResolved() {
    boolean resuelta = true;
    for (OpcionApuesta opcion : tipoApuesta.getOpcionesApuesta()) {
        resuelta = (opcion.getEstado() == null);
    }
    return resuelta;
}

public Set<OpcionApuesta> getOpcionesApuesta() {
    return tipoApuesta.getOpcionesApuesta();
}

public boolean getEventoStart() {
    Calendar now = Calendar.getInstance();
    return (tipoApuesta.getEvento().getFecha().before(now));
}

public TipoApuesta getTipoApuesta() {
    return tipoApuesta;
}

public void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

void onActivate(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
    try {
        tipoApuesta = userService.findTipoApuestaById(idTipoApuesta);
    } catch (InstanceNotFoundException e) {

    }

}

Long onPassivate() {
    return idTipoApuesta;
}

Object onSuccess() {
    especificarGanadoras.setIdTipoApuesta(idTipoApuesta);
    return especificarGanadoras;
}
}
