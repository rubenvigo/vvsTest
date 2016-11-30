package es.udc.pa.pa002.practicapa.web.pages.useroperations;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.userservice.ApuestaBlock;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;

/**
 * The Class FindApuestasUser.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class FindApuestasUser {

/** The Constant APUESTAS_PER_PAGE. */
private final static int APUESTAS_PER_PAGE = 10;

/** The apuesta block. */
private ApuestaBlock apuestaBlock;

/** The start index. */
private int startIndex = 0;

/** The user service. */
@Inject
private UserService userService;

/** The user session. */
@Property
@SessionState(create = false)
private UserSession userSession;

/** The apuesta. */
@Property
private ApuestaRealizada apuesta;

/**
 * Gets the checks if is pendiente.
 *
 * @return the checks if is pendiente
 */
public final boolean getIsPendiente() {
    return apuesta.getOpcionApuesta().getEstado() == null;
}

/**
 * Gets the checks if is winner.
 *
 * @return the checks if is winner
 */
public final boolean getIsWinner() {
    return apuesta.getOpcionApuesta().getEstado() != null
            && apuesta.getOpcionApuesta().getEstado();
}

/**
 * Gets the data evento.
 *
 * @return the data evento
 */
public final String getDataEvento() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    return sdf.format(apuesta.getOpcionApuesta().getTipoApuesta().getEvento()
            .getFecha().getTime());
}

/**
 * Gets the ganancia.
 *
 * @return the ganancia
 */
public final float getGanancia() {
    float ganancia = apuesta.getCantidadApostada()
            * apuesta.getOpcionApuesta().getCuota();

    return ganancia;
}

/**
 * Gets the fecha apuesta.
 *
 * @return the fecha apuesta
 */
public final String getFechaApuesta() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
    return sdf.format(apuesta.getFecha().getTime());
}

/**
 * Gets the apuestas user.
 *
 * @return the apuestas user
 */
public final List<ApuestaRealizada> getApuestasUser() {
    return apuestaBlock.getApuestas();
}

/**
 * Gets the apuestas per page.
 *
 * @return the apuestas per page
 */
public static int getApuestasPerPage() {
    return APUESTAS_PER_PAGE;
}

/**
 * Gets the previous link context.
 *
 * @return the previous link context
 */
public final Object[] getPreviousLinkContext() {

    if (startIndex - APUESTAS_PER_PAGE >= 0) {
        return new Object[] {startIndex - APUESTAS_PER_PAGE };
    } else {
        return null;
    }

}

/**
 * Gets the next link context.
 *
 * @return the next link context
 */
public final Object[] getNextLinkContext() {

    if (apuestaBlock.getExistMoreApuestas()) {
        return new Object[] {startIndex + APUESTAS_PER_PAGE };
    } else {
        return null;
    }

}

/**
 * On activate.
 *
 * @param startIndex
 *            the start index
 */
final void onActivate(int startIndex) {
    this.startIndex = startIndex;
    apuestaBlock = userService.consultarApuestas(
            userSession.getUserProfileId(), startIndex, APUESTAS_PER_PAGE);
}

}
