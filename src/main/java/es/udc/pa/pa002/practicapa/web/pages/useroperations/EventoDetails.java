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
import es.udc.pa.pa002.practicapa.web.pages.admin.AnadirTipoApuesta;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class EventoDetails.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventoDetails {

/** The id evento. */
private Long idEvento;

/** The evento. */
private Evento evento;

/** The tipo apuesta. */
@Property
private TipoApuesta tipoApuesta;

/** The anadir tipo apuesta. */
@InjectPage
private AnadirTipoApuesta anadirTipoApuesta;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/**
 * Gets the tipos apuesta.
 *
 * @return the tipos apuesta
 */
public final Set<TipoApuesta> getTiposApuesta() {
    return evento.getTipoApuesta();
}

/**
 * Gets the checks if is resolved.
 *
 * @return the checks if is resolved
 */
public final boolean getIsResolved() {
    boolean resuelta = false;
    for (OpcionApuesta opcion : tipoApuesta.getOpcionesApuesta()) {
        resuelta = opcion.getEstado() != null;
    }
    return resuelta;
}

/**
 * Sets the id evento.
 *
 * @param idEvento
 *            the new id evento
 */
public final void setIdEvento(final Long idEvento) {
    this.idEvento = idEvento;
}

/**
 * Gets the evento.
 *
 * @return the evento
 */
public final Evento getEvento() {
    return evento;
}

/**
 * Gets the checks if is admin.
 *
 * @return the checks if is admin
 */
public final boolean getIsAdmin() {
    return userSession != null && userSession.isAdmin();
}

/**
 * Gets the evento start.
 *
 * @return the evento start
 */
public final boolean getEventoStart() {
    Calendar now = Calendar.getInstance();
    return evento.getFecha().before(now);
}

/**
 * On activate.
 *
 * @param idEvento
 *            the id evento
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final void onActivate(final Long idEvento) throws InstanceNotFoundException {
    this.idEvento = idEvento;
    evento = userService.findEventoById(idEvento);
}

/**
 * On passivate.
 *
 * @return the long
 */
final Long onPassivate() {
    return idEvento;
}

/**
 * On success.
 *
 * @return the object
 */
final Object onSuccess() {
    anadirTipoApuesta.setIdEvento(idEvento);
    return anadirTipoApuesta;
}

}
