package es.udc.pa.pa002.practicapa.model.apuestarealizada;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;

/**
 * The Class ApuestaRealizada.
 */
@Entity
public class ApuestaRealizada {

/** The id apuesta realizada. */
private Long idApuestaRealizada;

/** The usuario. */
private UserProfile usuario;

/** The opcion apuesta. */
private OpcionApuesta opcionApuesta;

/** The cantidad apostada. */
private float cantidadApostada;

/** The fecha. */
private Calendar fecha;

/**
 * Instantiates a new apuesta realizada.
 */
public ApuestaRealizada() {

}

/**
 * Instantiates a new apuesta realizada.
 *
 * @param usuario
 *            the user
 * @param opcionApuesta
 *            the option bet
 * @param cantidadApostada
 *            the amount wagered
 * @param fecha
 *            the date
 */
public ApuestaRealizada(final UserProfile usuario,
        final OpcionApuesta opcionApuesta, final float cantidadApostada,
        final Calendar fecha) {
    super();
    this.usuario = usuario;
    this.opcionApuesta = opcionApuesta;
    this.cantidadApostada = cantidadApostada;
    this.fecha = fecha;
}

/**
 * Gets the id apuesta realizada.
 *
 * @return the id apuesta realizada
 */
@SequenceGenerator(// It only takes effect for
name = "ApuestaRealizadaIdGenerator", // databases providing identifier
sequenceName = "ApuestaRealizadaSeq")
// generators.
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "ApuestaRealizadaIdGenerator")
public final Long getIdApuestaRealizada() {
    return idApuestaRealizada;
}

/**
 * Sets the id apuesta realizada.
 *
 * @param idApuestaRealizada
 *            the new id apuesta realizada
 */
public final void setIdApuestaRealizada(final Long idApuestaRealizada) {
    this.idApuestaRealizada = idApuestaRealizada;
}

/**
 * Gets the user.
 *
 * @return the user
 */
@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "idUsuario")
public final UserProfile getUsuario() {
    return usuario;
}

/**
 * Sets the user.
 *
 * @param usuario
 *            the new user
 */
public final void setUsuario(final UserProfile usuario) {
    this.usuario = usuario;
}

/**
 * Gets the option bet.
 *
 * @return the option bet
 */
@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "idOpcionApuesta")
public final OpcionApuesta getOpcionApuesta() {
    return opcionApuesta;
}

/**
 * Sets the option bet.
 *
 * @param opcionApuesta
 *            the new option bet
 */
public final void setOpcionApuesta(final OpcionApuesta opcionApuesta) {
    this.opcionApuesta = opcionApuesta;
}

/**
 * Gets the amount wagered.
 *
 * @return the amount wagered
 */
public final float getCantidadApostada() {
    return cantidadApostada;
}

/**
 * Sets the amount wagered.
 *
 * @param cantidadApostada
 *            the new amount wagered
 */
public final void setCantidadApostada(final float cantidadApostada) {
    this.cantidadApostada = cantidadApostada;
}

/**
 * Gets the fecha.
 *
 * @return the date
 */
public final Calendar getFecha() {
    return fecha;
}

/**
 * Sets the fecha.
 *
 * @param fecha
 *            the new date
 */
public final void setFecha(final Calendar fecha) {
    this.fecha = fecha;
}

}
