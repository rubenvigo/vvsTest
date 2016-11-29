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

/**
 * The Class OpcionApuestaDetails.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class OpcionApuestaDetails {

/** The apostar form. */
@Component
private Form apostarForm;

/** The messages. */
@Inject
private Messages messages;

/** The id opcion apuesta. */
private Long idOpcionApuesta;

/** The opcion apuesta. */
private OpcionApuesta opcionApuesta;

/** The apuesta. */
private ApuestaRealizada apuesta;

/** The importe. */
@Property
private Float importe;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/** The apuesta creada. */
@InjectPage
private ApuestaCreada apuestaCreada;

/** The login. */
@InjectPage
private Login login;

/**
 * Gets the evento.
 *
 * @return the evento
 */
public Evento getEvento() {
    return opcionApuesta.getTipoApuesta().getEvento();
}

/**
 * Gets the past event.
 *
 * @return the past event
 */
public boolean getPastEvent() {
    Calendar now = Calendar.getInstance();
    return this.getEvento().getFecha().before(now);
}

/**
 * Gets the tipo apuesta.
 *
 * @return the tipo apuesta
 */
public TipoApuesta getTipoApuesta() {
    return opcionApuesta.getTipoApuesta();
}

/**
 * Gets the opcion apuesta.
 *
 * @return the opcion apuesta
 */
public OpcionApuesta getOpcionApuesta() {
    return opcionApuesta;
}

/**
 * Sets the id opcion apuesta.
 *
 * @param idOpcionApuesta
 *            the new id opcion apuesta
 */
public void setIdOpcionApuesta(Long idOpcionApuesta) {
    this.idOpcionApuesta = idOpcionApuesta;
}

/**
 * Gets the checks if is usuario.
 *
 * @return the checks if is usuario
 */
public boolean getIsUsuario() {
    return userSession == null || !userSession.isAdmin();
}

/**
 * On activate.
 *
 * @param idOpcionApuesta
 *            the id opcion apuesta
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public void onActivate(Long idOpcionApuesta) throws InstanceNotFoundException {
    this.idOpcionApuesta = idOpcionApuesta;
    opcionApuesta = userService.findOpcionApuestaById(idOpcionApuesta);
}

/**
 * On passivate.
 *
 * @return the long
 */
Long onPassivate() {
    return idOpcionApuesta;
}

/**
 * On validate from apostar form.
 *
 * @throws InvalidValueException
 *             the invalid value exception
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
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

/**
 * On success.
 *
 * @return the object
 */
Object onSuccess() {
    if (userSession == null) {
        login.setIdOpcionApuesta(idOpcionApuesta);
        return login;
    }

    apuestaCreada.setIdApuesta(apuesta.getIdApuestaRealizada());
    return apuestaCreada;
}

}
