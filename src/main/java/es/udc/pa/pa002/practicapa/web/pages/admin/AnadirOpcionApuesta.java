package es.udc.pa.pa002.practicapa.web.pages.admin;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.EventoStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.InstanceAlreadyCreatedException;
import es.udc.pa.pa002.practicapa.model.userservice.RepeatedOpcionApuestaException;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class AnadirOpcionApuesta.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class AnadirOpcionApuesta {

/** The opciones. */
@Persist
private Set<OpcionApuesta> opciones;

/** The zona opciones registradas. */
@InjectComponent
private Zone zonaOpcionesRegistradas;

/** The request. */
@Inject
private Request request;

/** The ajax response renderer. */
@Inject
private AjaxResponseRenderer ajaxResponseRenderer;

/** The evento. */
private Evento evento;

/** The opcion apuesta. */
private OpcionApuesta opcionApuesta;

/** The tipo apuesta. */
private TipoApuesta tipoApuesta;

/** The pregunta. */
private String pregunta;

/** The click finalizar. */
@Persist
private boolean clickFinalizar;

/** The id evento. */
private Long idEvento;

/** The multiplesganadoras. */
private boolean multiplesganadoras;

/** The anadir opcion apuesta form. */
@Component
private Form anadirOpcionApuestaForm;

/** The user service. */
@Inject
private UserService userService;

/** The respuesta. */
@Property
private String respuesta;

/** The cuota. */
@Property
private float cuota = 1F;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/** The tipo apuesta creada. */
@InjectPage
private TipoApuestaCreada tipoApuestaCreada;

/** The messages. */
@Inject
private Messages messages;

/**
 * Gets the opciones.
 *
 * @return the opciones
 */
public final Set<OpcionApuesta> getOpciones() {
    return opciones;
}

/**
 * Sets the opciones.
 *
 * @param opciones
 *            the new opciones
 */
public final void setOpciones(Set<OpcionApuesta> opciones) {
    this.opciones = opciones;
}

/**
 * Gets the num opciones.
 *
 * @return the num opciones
 */
public final int getNumOpciones() {
    return opciones.size();
}

/**
 * Sets the pregunta.
 *
 * @param pregunta
 *            the new pregunta
 */
public final void setPregunta(String pregunta) {
    this.pregunta = pregunta;
}

/**
 * Gets the pregunta.
 *
 * @return the pregunta
 */
public final String getPregunta() {
    return pregunta;
}

/**
 * Gets the opcion apuesta.
 *
 * @return the opcion apuesta
 */
public final OpcionApuesta getOpcionApuesta() {
    return opcionApuesta;
}

/**
 * Sets the opcion apuesta.
 *
 * @param opcionApuesta
 *            the new opcion apuesta
 */
public final void setOpcionApuesta(OpcionApuesta opcionApuesta) {
    this.opcionApuesta = opcionApuesta;
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
 * Sets the id evento.
 *
 * @param idEvento
 *            the new id evento
 */
public final void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

/**
 * Sets the multiplesganadoras.
 *
 * @param multiplesganadoras
 *            the new multiplesganadoras
 */
public final void setMultiplesganadoras(boolean multiplesganadoras) {
    this.multiplesganadoras = multiplesganadoras;
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
 * @param pregunta
 *            the pregunta
 * @param multiplesganadoras
 *            the multiplesganadoras
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final void onActivate(Long idEvento, String pregunta, boolean multiplesganadoras)
        throws InstanceNotFoundException {
    this.idEvento = idEvento;
    this.pregunta = pregunta;
    this.multiplesganadoras = multiplesganadoras;

    evento = userService.findEventoById(idEvento);
}

/**
 * On passivate.
 *
 * @return the object[]
 */
final Object[] onPassivate() {
    return new Object[] {idEvento, pregunta, multiplesganadoras };
}

/**
 * On validate from anadir opcion apuesta form.
 */
@OnEvent(value = "validate", component = "anadirOpcionApuestaForm")
final void OnValidateFromAnadirOpcionApuestaForm() {

    if (!anadirOpcionApuestaForm.isValid()) {
        return;
    }
    if (opciones != null) {
        opciones.add(new OpcionApuesta(respuesta, cuota, null));
    } else {
        opciones = new HashSet<OpcionApuesta>();
        opciones.add(new OpcionApuesta(respuesta, cuota, null));
    }
    respuesta = "";
    cuota = 1;

    if (clickFinalizar) {

        try {
            tipoApuesta = userService.addTipoApuesta(idEvento, new TipoApuesta(
                    evento, pregunta, opciones, multiplesganadoras));
            tipoApuestaCreada.setIdTipoApuesta(tipoApuesta.getIdTipoApuesta());

        } catch (EventoStartedException e) {
            clickFinalizar = false;
            anadirOpcionApuestaForm.recordError(messages
                    .get("error-eventoStarted"));
        } catch (InstanceAlreadyCreatedException e) {
            clickFinalizar = false;
            anadirOpcionApuestaForm.recordError(messages
                    .get("error-instanceCreated"));
        } catch (RepeatedOpcionApuestaException e) {
            clickFinalizar = false;
            anadirOpcionApuestaForm.recordError(messages
                    .get("error-repeatedOpcion"));
        } catch (InstanceNotFoundException e) {
            clickFinalizar = false;
            anadirOpcionApuestaForm.recordError(messages
                    .get("error-instanceNotFound"));
        }

        opciones.clear();
    }

}

/**
 * On success.
 *
 * @return the object
 */
final Object onSuccess() {
    if (clickFinalizar) {
        clickFinalizar = false;
        return tipoApuestaCreada;
    }

    if (request.isXHR()) {
        return zonaOpcionesRegistradas.getBody();
    } else {
        return null;
    }
}

/**
 * On failure.
 *
 * @return the object
 */
final Object onFailure() {
    if (request.isXHR()) {
        return zonaOpcionesRegistradas.getBody();
    } else {
        return null;
    }
}

/**
 * On selected from finalizar.
 */
final void onSelectedFromFinalizar() {
    clickFinalizar = true;

}
}
