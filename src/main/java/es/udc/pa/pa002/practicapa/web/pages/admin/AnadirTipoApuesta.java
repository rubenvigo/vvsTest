package es.udc.pa.pa002.practicapa.web.pages.admin;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class AnadirTipoApuesta.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class AnadirTipoApuesta {

/** The id evento. */
private Long idEvento;

/** The evento. */
private Evento evento;

/** The multiplesganadoras. */
@Property
private boolean multiplesganadoras;

/** The pregunta. */
@Property
private String pregunta;

/** The anadir opcion apuesta. */
@InjectPage
private AnadirOpcionApuesta anadirOpcionApuesta;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/**
 * Sets the id evento.
 *
 * @param idEvento
 *            the new id evento
 */
public final void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

/**
 * Gets the id evento.
 *
 * @return the id evento
 */
public final Long getIdEvento() {
    return idEvento;
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
 * On activate.
 *
 * @param idEvento
 *            the id evento
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final void onActivate(Long idEvento) throws InstanceNotFoundException {
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
    anadirOpcionApuesta.setIdEvento(idEvento);
    anadirOpcionApuesta.setPregunta(pregunta);
    anadirOpcionApuesta.setMultiplesganadoras(multiplesganadoras);
    anadirOpcionApuesta.setOpciones(null);
    return anadirOpcionApuesta;
}

}
