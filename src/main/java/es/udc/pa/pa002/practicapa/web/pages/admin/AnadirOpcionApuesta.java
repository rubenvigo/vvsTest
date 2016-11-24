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

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class AnadirOpcionApuesta {

@Persist
Set<OpcionApuesta> opciones;

@InjectComponent
private Zone zonaOpcionesRegistradas;

@Inject
private Request request;

@Inject
private AjaxResponseRenderer ajaxResponseRenderer;

private Evento evento;

private OpcionApuesta opcionApuesta;

private TipoApuesta tipoApuesta;

private String pregunta;

@Persist
private boolean clickFinalizar;

private Long idEvento;

private boolean multiplesganadoras;

@Component
private Form anadirOpcionApuestaForm;

@Inject
private UserService userService;

@Property
private String respuesta;

@Property
private float cuota = 1F;

@Property
@SessionState(create = false)
private UserSession userSession;

@InjectPage
private TipoApuestaCreada tipoApuestaCreada;

@Inject
private Messages messages;

public Set<OpcionApuesta> getOpciones() {
    return opciones;
}

public void setOpciones(Set<OpcionApuesta> opciones) {
    this.opciones = opciones;
}

public int getNumOpciones() {
    return opciones.size();
}

public void setPregunta(String pregunta) {
    this.pregunta = pregunta;
}

public String getPregunta() {
    return pregunta;
}

public OpcionApuesta getOpcionApuesta() {
    return opcionApuesta;
}

public void setOpcionApuesta(OpcionApuesta opcionApuesta) {
    this.opcionApuesta = opcionApuesta;
}

public Evento getEvento() {
    return evento;
}

public void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

public void setMultiplesganadoras(boolean multiplesganadoras) {
    this.multiplesganadoras = multiplesganadoras;
}

public boolean getIsAdmin() {
    return (userSession != null) && (userSession.isAdmin());
}

void onActivate(Long idEvento, String pregunta, boolean multiplesganadoras) {
    this.idEvento = idEvento;
    this.pregunta = pregunta;
    this.multiplesganadoras = multiplesganadoras;

    try {
        evento = userService.findEventoById(idEvento);
    } catch (InstanceNotFoundException e) {

    }
}

Object[] onPassivate() {
    return new Object[] {idEvento, pregunta, multiplesganadoras };
}

@OnEvent(value = "validate", component = "anadirOpcionApuestaForm")
void OnValidateFromAnadirOpcionApuestaForm() {

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

Object onSuccess() {
    if (clickFinalizar) {
        clickFinalizar = false;
        return tipoApuestaCreada;
    }

    return request.isXHR() ? zonaOpcionesRegistradas.getBody() : null;
}

Object onFailure() {
    return request.isXHR() ? zonaOpcionesRegistradas.getBody() : null;
}

void onSelectedFromFinalizar() {
    clickFinalizar = true;

}
}
