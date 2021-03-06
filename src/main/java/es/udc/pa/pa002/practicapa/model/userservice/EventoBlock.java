package es.udc.pa.pa002.practicapa.model.userservice;

import java.util.List;

import es.udc.pa.pa002.practicapa.model.evento.Evento;

/**
 * The Class EventoBlock.
 */
public class EventoBlock {

/** The eventos. */
private List<Evento> eventos;

/** The exist more eventos. */
private boolean existMoreEventos;

/**
 * Instantiates a new evento block.
 *
 * @param eventos
 *            the eventos
 * @param existMoreEventos
 *            the exist more eventos
 */
public EventoBlock(final List<Evento> eventos, final boolean existMoreEventos) {

    this.eventos = eventos;
    this.existMoreEventos = existMoreEventos;

}

/**
 * Gets the eventos.
 *
 * @return the eventos
 */
public final List<Evento> getEventos() {
    return eventos;
}

/**
 * Gets the exist more eventos.
 *
 * @return the exist more eventos
 */
public final boolean getExistMoreEventos() {
    return existMoreEventos;
}

}
