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

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class EspecificarGanadoras {

@Component
private Form especificarGanadorasForm;

private Long idTipoApuesta;

private TipoApuesta tipoApuesta;

@Property
private OpcionApuesta opcionApuesta;

@Property
private List<OpcionApuesta> ganadoras;

@Property
private SelectModel opcionApuestaSelectModel;

@Inject
SelectModelFactory selectModelFactory;

@Inject
private Messages messages;

@Inject
private UserService userService;

@InjectPage
private TipoApuestaDetails tipoApuestaDetails;

@Property
@SessionState(create = false)
private UserSession userSession;

public Evento getEvento() {
    return tipoApuesta.getEvento();
}

public List<OpcionApuesta> getOpcionesApuesta() {
    List<OpcionApuesta> opciones = new ArrayList<OpcionApuesta>();

    Set<OpcionApuesta> opcionesSet = tipoApuesta.getOpcionesApuesta();
    for (OpcionApuesta opcion : opcionesSet) {
        opciones.add(opcion);
    }
    return opciones;
}

public TipoApuesta getTipoApuesta() {
    return tipoApuesta;
}

void onActivate(Long idTipoApuesta) {
    opcionApuesta = new OpcionApuesta();
    this.idTipoApuesta = idTipoApuesta;
    try {
        tipoApuesta = userService.findTipoApuestaById(idTipoApuesta);
    } catch (InstanceNotFoundException e) {

    }
    if (tipoApuesta != null)
        opcionApuestaSelectModel = selectModelFactory.create(
                this.getOpcionesApuesta(), "respuesta");

}

public void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

Long onPassivate() {
    return idTipoApuesta;
}

public OpcionApuestaEncoder getOpcionApuestaEncoder() {
    return new OpcionApuestaEncoder(userService);
}

void onValidateFromEspecificarGanadorasForm() {

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

    } catch (InstanceNotFoundException e) {

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

Object onSuccess() {
    tipoApuestaDetails.setIdTipoApuesta(idTipoApuesta);
    return tipoApuestaDetails;
}

}
