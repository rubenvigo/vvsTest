package es.udc.pa.pa002.practicapa.model.tipoapuesta;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;

/**
 * The Class TipoApuesta.
 */
@Entity
@BatchSize(size = 10)
public class TipoApuesta {

/** The id tipo apuesta. */
private Long idTipoApuesta;

/** The pregunta. */
private String pregunta;

/** The evento. */
private Evento evento;

/** The opciones apuesta. */
private Set<OpcionApuesta> opcionesApuesta;

/** The multiples ganadoras. */
private boolean multiplesGanadoras;

/**
 * Instantiates a new tipo apuesta.
 */
public TipoApuesta() {

}

/**
 * Instantiates a new tipo apuesta.
 *
 * @param evento
 *            the evento
 * @param pregunta
 *            the pregunta
 * @param opcionesApuesta
 *            the opciones apuesta
 * @param multiplesGanadoras
 *            the multiples ganadoras
 */
public TipoApuesta(Evento evento, String pregunta,
        Set<OpcionApuesta> opcionesApuesta, boolean multiplesGanadoras) {

    this.pregunta = pregunta;
    this.opcionesApuesta = opcionesApuesta;
    this.multiplesGanadoras = multiplesGanadoras;
}

/**
 * Instantiates a new tipo apuesta.
 *
 * @param pregunta
 *            the pregunta
 * @param opcionesApuesta
 *            the opciones apuesta
 * @param multiplesGanadoras
 *            the multiples ganadoras
 */
public TipoApuesta(String pregunta, Set<OpcionApuesta> opcionesApuesta,
        boolean multiplesGanadoras) {

    this.pregunta = pregunta;
    this.opcionesApuesta = opcionesApuesta;
    this.multiplesGanadoras = multiplesGanadoras;
}

/**
 * Gets the id tipo apuesta.
 *
 * @return the id tipo apuesta
 */
@SequenceGenerator(name = "idTipoApuestaGenerator", sequenceName = "tipoApuestaSeq")
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "idTipoApuestaGenerator")
public final Long getIdTipoApuesta() {
    return idTipoApuesta;
}

/**
 * Sets the id tipo apuesta.
 *
 * @param idTipoApuesta
 *            the new id tipo apuesta
 */
public final void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

/**
 * Gets the pregunta.
 *
 * @return the pregunta
 */
public final String getPregunta() {
    return pregunta;
}

/**
 * Sets the pregunta.
 *
 * @param pregunta
 *            the new pregunta
 */
public final void setPregunta(String pregunta) {
    this.pregunta = pregunta;
}

/**
 * Gets the evento.
 *
 * @return the evento
 */
@ManyToOne(optional = false)
@JoinColumn(name = "idEvento")
public final Evento getEvento() {
    return evento;
}

/**
 * Sets the evento.
 *
 * @param evento
 *            the new evento
 */
public final void setEvento(Evento evento) {
    this.evento = evento;
}

/**
 * Gets the opciones apuesta.
 *
 * @return the opciones apuesta
 */
@OneToMany(mappedBy = "tipoApuesta")
public final Set<OpcionApuesta> getOpcionesApuesta() {
    return opcionesApuesta;
}

/**
 * Sets the opciones apuesta.
 *
 * @param opcionesApuesta
 *            the new opciones apuesta
 */
public final void setOpcionesApuesta(Set<OpcionApuesta> opcionesApuesta) {
    this.opcionesApuesta = opcionesApuesta;
}

/**
 * Checks if is multiples ganadoras.
 *
 * @return true, if is multiples ganadoras
 */
public final boolean isMultiplesGanadoras() {
    return multiplesGanadoras;
}

/**
 * Sets the multiples ganadoras.
 *
 * @param multiplesGanadoras
 *            the new multiples ganadoras
 */
public final void setMultiplesGanadoras(boolean multiplesGanadoras) {
    this.multiplesGanadoras = multiplesGanadoras;
}

/**
 * Exists opcion apuesta.
 *
 * @param nombre
 *            the nombre
 * @return true, if successful
 */
public final boolean existsOpcionApuesta(String nombre) {
    for (OpcionApuesta opcionApuesta : opcionesApuesta) {
        if (opcionApuesta.getRespuesta().equals(nombre)) {
            return true;
        }
    }
    return false;
}

}
