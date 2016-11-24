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

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class TipoApuestaCreada {

private Long idTipoApuesta;

private TipoApuesta tipoApuesta;

@Inject
private UserService userService;

@Property
@SessionState(create = false)
private UserSession userSession;

@Property
private OpcionApuesta opcion;

public Long getIdTipoApuesta() {
    return idTipoApuesta;
}

public void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

public Set<OpcionApuesta> getOpciones() {
    return tipoApuesta.getOpcionesApuesta();
}

public TipoApuesta getTipoApuesta() {
    return tipoApuesta;
}

public void setTipoApuesta(TipoApuesta tipoApuesta) {
    this.tipoApuesta = tipoApuesta;
}

public boolean getIsAdmin() {
    return userSession != null && userSession.isAdmin();
}

void onActivate(Long idTipoApuesta) throws InstanceNotFoundException {
    this.idTipoApuesta = idTipoApuesta;
    tipoApuesta = userService.findTipoApuestaById(idTipoApuesta);
}

Long onPassivate() {
    return idTipoApuesta;
}

}
