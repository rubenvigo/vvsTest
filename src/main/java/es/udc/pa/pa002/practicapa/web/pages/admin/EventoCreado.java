package es.udc.pa.pa002.practicapa.web.pages.admin;

import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class EventoCreado {

private Long idEvento;

public Long getIdEvento() {
    return idEvento;
}

public void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

Long onPassivate() {
    return idEvento;
}

void onActivate(Long idEvento) {
    this.idEvento = idEvento;
}
}
