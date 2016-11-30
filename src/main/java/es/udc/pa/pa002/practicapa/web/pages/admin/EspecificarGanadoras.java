package es.udc.pa.pa002.practicapa.web.pages.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.EventoNotStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidOptionException;
import es.udc.pa.pa002.practicapa.model.userservice.OpcionApuestaAlreadySolvedException;
import es.udc.pa.pa002.practicapa.model.userservice.SimpleWinnerException;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.useroperations.TipoApuestaDetails;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.OpcionApuestaEncoder;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class EspecificarGanadoras.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class EspecificarGanadoras {

/** The especificar ganadoras form. */
@Component
private Form especificarGanadorasForm;

/** The id tipo apuesta. */
private Long idTipoApuesta;

/** The tipo apuesta. */
private TipoApuesta tipoApuesta;

/** The opcion apuesta. */
@Property
private OpcionApuesta opcionApuesta;

/** The ganadoras. */
@Property
private List<OpcionApuesta> ganadoras;

/** The opcion apuesta select model. */
@Property
private SelectModel opcionApuestaSelectModel;

/** The select model factory. */
@Inject
private SelectModelFactory selectModelFactory;

/** The messages. */
@Inject
private Messages messages;

/** The user service. */
@Inject
private UserService userService;

/** The tipo apuesta details. */
@InjectPage
private TipoApuestaDetails tipoApuestaDetails;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/**
 * Gets the evento.
 *
 * @return the evento
 */
public final Evento getEvento() {
    return tipoApuesta.getEvento();
}

/**
 * Gets the opciones apuesta.
 *
 * @return the opciones apuesta
 */
public final List<OpcionApuesta> getOpcionesApuesta() {
    List<OpcionApuesta> opciones = new ArrayList<OpcionApuesta>();

    Set<OpcionApuesta> opcionesSet = tipoApuesta.getOpcionesApuesta();
    for (OpcionApuesta opcion : opcionesSet) {
        opciones.add(opcion);
    }
    return opciones;
}

/**
 * Gets the tipo apuesta.
 *
 * @return the tipo apuesta
 */
public final TipoApuesta getTipoApuesta() {
    return tipoApuesta;
}

/**
 * On activate.
 *
 * @param idTipoApuesta
 *            the id tipo apuesta
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final void onActivate(Long idTipoApuesta) throws InstanceNotFoundException {
    opcionApuesta = new OpcionApuesta();
    this.idTipoApuesta = idTipoApuesta;
    tipoApuesta = userService.findTipoApuestaById(idTipoApuesta);
    if (tipoApuesta != null) {
        opcionApuestaSelectModel = selectModelFactory.create(
                this.getOpcionesApuesta(), "respuesta");
    }
}

/**
 * Sets the id tipo apuesta.
 *
 * @param idTipoApuesta
 *            the new id tipo apuesta
 */
public final void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

/**
 * On passivate.
 *
 * @return the long
 */
final Long onPassivate() {
    return idTipoApuesta;
}

/**
 * Gets the opcion apuesta encoder.
 *
 * @return the opcion apuesta encoder
 */
public final OpcionApuestaEncoder getOpcionApuestaEncoder() {
    return new OpcionApuestaEncoder(userService);
}

/**
 * On validate from especificar ganadoras form.
 *
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
final void onValidateFromEspecificarGanadorasForm()
        throws InstanceNotFoundException {

    try {
        List<Long> ids = new ArrayList<Long>();
        if (!tipoApuesta.isMultiplesGanadoras()) {
            ids.add(opcionApuesta.getIdOpcionApuesta());
            userService.EspecificarGanadoras(idTipoApuesta, ids);
            ids.clear();
            return;
        }
        for (OpcionApuesta opcion : ganadoras) {
            ids.add(opcion.getIdOpcionApuesta());
        }
        userService.EspecificarGanadoras(idTipoApuesta, ids);
        ids.clear();
        ganadoras.clear();

    } catch (EventoNotStartedException e) {
        especificarGanadorasForm.recordError(messages.format(
                "error-eventoNotStarted", tipoApuesta.getEvento().getNombre()));
    } catch (OpcionApuestaAlreadySolvedException e) {
        especificarGanadorasForm.recordError(messages.format(
                "error-alreadySolved", tipoApuesta.getPregunta()));
    } catch (SimpleWinnerException e) {
        especificarGanadorasForm.recordError(messages.format(
                "error-simpleWinner", tipoApuesta.getPregunta()));
    } catch (InvalidOptionException e) {
        especificarGanadorasForm.recordError(messages.format(
                "error-invalidOption", tipoApuesta.getPregunta()));
    }

}

/**
 * On success.
 *
 * @return the object
 */
final Object onSuccess() {
    tipoApuestaDetails.setIdTipoApuesta(idTipoApuesta);
    return tipoApuestaDetails;
}

}
