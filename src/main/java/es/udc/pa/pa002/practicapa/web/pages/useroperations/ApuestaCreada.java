package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ApuestaCreada {

private Long idApuesta;

@Property
private ApuestaRealizada apuesta;

@Inject
private UserService userService;

public Long getIdApuesta() {
    return idApuesta;
}

public void setIdApuesta(Long idApuesta) {
    this.idApuesta = idApuesta;
}

public String getDataEvento() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    return sdf.format(apuesta.getOpcionApuesta().getTipoApuesta().getEvento()
            .getFecha().getTime());
}

public float getGanancia() {
    float ganancia = apuesta.getCantidadApostada()
            * apuesta.getOpcionApuesta().getCuota();

    return ganancia;
}

public String getFechaApuesta() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    return sdf.format(apuesta.getFecha().getTime());
}

Long onPassivate() {
    return idApuesta;
}

void onActivate(Long idApuesta) {
    this.idApuesta = idApuesta;
    try {
        apuesta = userService.findApuestaById(idApuesta);
    } catch (InstanceNotFoundException e) {

    }
}

}
