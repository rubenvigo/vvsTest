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

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class FindApuestasUser {

private final static int APUESTAS_PER_PAGE = 10;

private ApuestaBlock apuestaBlock;

private int startIndex = 0;

@Inject
private UserService userService;

@Property
@SessionState(create = false)
private UserSession userSession;

@Property
private ApuestaRealizada apuesta;

public boolean getIsPendiente() {
    return apuesta.getOpcionApuesta().getEstado() == null;
}

public boolean getIsWinner() {
    return apuesta.getOpcionApuesta().getEstado() != null
            && apuesta.getOpcionApuesta().getEstado() == true;
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

public List<ApuestaRealizada> getApuestasUser() {
    return apuestaBlock.getApuestas();
}

public static int getApuestasPerPage() {
    return APUESTAS_PER_PAGE;
}

public Object[] getPreviousLinkContext() {

    if (startIndex - APUESTAS_PER_PAGE >= 0) {
        return new Object[] {startIndex - APUESTAS_PER_PAGE };
    } else {
        return null;
    }

}

public Object[] getNextLinkContext() {

    if (apuestaBlock.getExistMoreApuestas()) {
        return new Object[] {startIndex + APUESTAS_PER_PAGE };
    } else {
        return null;
    }

}

void onActivate(int startIndex) {
    this.startIndex = startIndex;
    apuestaBlock = userService.consultarApuestas(
            userSession.getUserProfileId(), startIndex, APUESTAS_PER_PAGE);
}

}
