package es.udc.pa.pa002.practicapa.web.pages.admin;

import java.util.Set;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class TipoApuestaCreada.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class TipoApuestaCreada {

/** The id tipo apuesta. */
private Long idTipoApuesta;

/** The tipo apuesta. */
private TipoApuesta tipoApuesta;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/** The opcion. */
@Property
private OpcionApuesta opcion;

/**
 * Gets the id tipo apuesta.
 *
 * @return the id tipo apuesta
 */
public Long getIdTipoApuesta() {
    return idTipoApuesta;
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
 * Gets the opciones.
 *
 * @return the opciones
 */
public Set<OpcionApuesta> getOpciones() {
    return tipoApuesta.getOpcionesApuesta();
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
 * Sets the tipo apuesta.
 *
 * @param tipoApuesta
 *            the new tipo apuesta
 */
public void setTipoApuesta(TipoApuesta tipoApuesta) {
    this.tipoApuesta = tipoApuesta;
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

}
