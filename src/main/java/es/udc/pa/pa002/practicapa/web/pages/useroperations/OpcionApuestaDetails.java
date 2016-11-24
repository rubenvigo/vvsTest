package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.util.Calendar;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.EventoStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidValueException;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.user.Login;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class OpcionApuestaDetails {

@Component
private Form apostarForm;

@Inject
private Messages messages;

private Long idOpcionApuesta;

private OpcionApuesta opcionApuesta;

private ApuestaRealizada apuesta;

@Property
private Float importe;

@Inject
private UserService userService;

@Property
@SessionState(create = false)
private UserSession userSession;

@InjectPage
private ApuestaCreada apuestaCreada;

@InjectPage
private Login login;

public Evento getEvento() {
    return opcionApuesta.getTipoApuesta().getEvento();
}

public boolean getPastEvent() {
    Calendar now = Calendar.getInstance();
    return this.getEvento().getFecha().before(now);
}

public TipoApuesta getTipoApuesta() {
    return opcionApuesta.getTipoApuesta();
}

public OpcionApuesta getOpcionApuesta() {
    return opcionApuesta;
}

public void setIdOpcionApuesta(Long idOpcionApuesta) {
    this.idOpcionApuesta = idOpcionApuesta;
}

public boolean getIsUsuario() {
    return userSession == null || !userSession.isAdmin();
}

public void onActivate(Long idOpcionApuesta) throws InstanceNotFoundException {
    this.idOpcionApuesta = idOpcionApuesta;
    opcionApuesta = userService.findOpcionApuestaById(idOpcionApuesta);
}

Long onPassivate() {
    return idOpcionApuesta;
}

void onValidateFromApostarForm() throws InvalidValueException,
        InstanceNotFoundException {
    if (!apostarForm.isValid()) {
        return;
    }
    if (userSession != null) {
        try {
            apuesta = userService.apostar(idOpcionApuesta, importe,
                    userSession.getUserProfileId());
        } catch (EventoStartedException e) {
            apostarForm.recordError(messages.format("error-eventoStarted", this
                    .getEvento().getNombre()));
        }
    }

}

Object onSuccess() {
    if (userSession == null) {
        login.setIdOpcionApuesta(idOpcionApuesta);
        return login;
    }

    apuestaCreada.setIdApuesta(apuesta.getIdApuestaRealizada());
    return apuestaCreada;
}

}
