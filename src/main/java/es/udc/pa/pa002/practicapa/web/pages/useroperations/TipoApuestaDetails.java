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

/**
 * The Class TipoApuestaDetails.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class TipoApuestaDetails {

/** The id tipo apuesta. */
private Long idTipoApuesta;

/** The tipo apuesta. */
private TipoApuesta tipoApuesta;

/** The opcion apuesta. */
@Property
private OpcionApuesta opcionApuesta;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/** The especificar ganadoras. */
@InjectPage
private EspecificarGanadoras especificarGanadoras;

/**
 * Gets the evento.
 *
 * @return the evento
 */
public Evento getEvento() {
    return tipoApuesta.getEvento();
}

/**
 * Gets the checks if is admin.
 *
 * @return the checks if is admin
 */
public boolean getIsAdmin() {
    return userSession != null && userSession.isAdmin();
}

/**
 * Gets the not resolved.
 *
 * @return the not resolved
 */
public boolean getNotResolved() {
    boolean resuelta = true;
    for (OpcionApuesta opcion : tipoApuesta.getOpcionesApuesta()) {
        resuelta = opcion.getEstado() == null;
    }
    return resuelta;
}

/**
 * Gets the opciones apuesta.
 *
 * @return the opciones apuesta
 */
public Set<OpcionApuesta> getOpcionesApuesta() {
    return tipoApuesta.getOpcionesApuesta();
}

/**
 * Gets the evento start.
 *
 * @return the evento start
 */
public boolean getEventoStart() {
    Calendar now = Calendar.getInstance();
    return tipoApuesta.getEvento().getFecha().before(now);
}

/**
 * Gets the tipo apuesta.
 *
 * @return the tipo apuesta
 */
public TipoApuesta getTipoApuesta() {
    return tipoApuesta;
}

/**
 * Sets the id tipo apuesta.
 *
 * @param idTipoApuesta
 *            the new id tipo apuesta
 */
public void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

/**
 * On activate.
 *
 * @param idTipoApuesta
 *            the id tipo apuesta
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
void onActivate(Long idTipoApuesta) throws InstanceNotFoundException {
    this.idTipoApuesta = idTipoApuesta;
    tipoApuesta = userService.findTipoApuestaById(idTipoApuesta);
}

/**
 * On passivate.
 *
 * @return the long
 */
Long onPassivate() {
    return idTipoApuesta;
}

/**
 * On success.
 *
 * @return the object
 */
Object onSuccess() {
    especificarGanadoras.setIdTipoApuesta(idTipoApuesta);
    return especificarGanadoras;
}
}
