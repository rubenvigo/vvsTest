package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class ApuestaCreada.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ApuestaCreada {

/** The id apuesta. */
private Long idApuesta;

/** The apuesta. */
@Property
private ApuestaRealizada apuesta;

/** The user service. */
@Inject
private UserService userService;

/**
 * Gets the id apuesta.
 *
 * @return the id apuesta
 */
public Long getIdApuesta() {
    return idApuesta;
}

/**
 * Sets the id apuesta.
 *
 * @param idApuesta
 *            the new id apuesta
 */
public void setIdApuesta(Long idApuesta) {
    this.idApuesta = idApuesta;
}

/**
 * Gets the data evento.
 *
 * @return the data evento
 */
public String getDataEvento() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    return sdf.format(apuesta.getOpcionApuesta().getTipoApuesta().getEvento()
            .getFecha().getTime());
}

/**
 * Gets the ganancia.
 *
 * @return the ganancia
 */
public float getGanancia() {
    float ganancia = apuesta.getCantidadApostada()
            * apuesta.getOpcionApuesta().getCuota();

    return ganancia;
}

/**
 * Gets the fecha apuesta.
 *
 * @return the fecha apuesta
 */
public String getFechaApuesta() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    return sdf.format(apuesta.getFecha().getTime());
}

/**
 * On passivate.
 *
 * @return the long
 */
Long onPassivate() {
    return idApuesta;
}

/**
 * On activate.
 *
 * @param idApuesta
 *            the id apuesta
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
void onActivate(Long idApuesta) throws InstanceNotFoundException {
    this.idApuesta = idApuesta;
    apuesta = userService.findApuestaById(idApuesta);
}

}
