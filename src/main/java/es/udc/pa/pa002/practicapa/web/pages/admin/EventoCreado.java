package es.udc.pa.pa002.practicapa.web.pages.admin;

import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;

/**
 * The Class EventoCreado.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class EventoCreado {

/** The id evento. */
private Long idEvento;

/**
 * Gets the id evento.
 *
 * @return the id evento
 */
public Long getIdEvento() {
    return idEvento;
}

/**
 * Sets the id evento.
 *
 * @param idEvento
 *            the new id evento
 */
public void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

/**
 * On passivate.
 *
 * @return the long
 */
Long onPassivate() {
    return idEvento;
}

/**
 * On activate.
 *
 * @param idEvento
 *            the id evento
 */
void onActivate(Long idEvento) {
    this.idEvento = idEvento;
}
}
