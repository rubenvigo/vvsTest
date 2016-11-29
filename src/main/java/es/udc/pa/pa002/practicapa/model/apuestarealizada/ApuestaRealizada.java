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
public ApuestaRealizada(UserProfile usuario, OpcionApuesta opcionApuesta,
        float cantidadApostada, Calendar fecha) {
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
public Long getIdApuestaRealizada() {
    return idApuestaRealizada;
}

/**
 * Sets the id apuesta realizada.
 *
 * @param idApuestaRealizada
 *            the new id apuesta realizada
 */
public void setIdApuestaRealizada(Long idApuestaRealizada) {
    this.idApuestaRealizada = idApuestaRealizada;
}

/**
 * Gets the user.
 *
 * @return the user
 */
@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "idUsuario")
public UserProfile getUsuario() {
    return usuario;
}

/**
 * Sets the user.
 *
 * @param usuario
 *            the new user
 */
public void setUsuario(UserProfile usuario) {
    this.usuario = usuario;
}

/**
 * Gets the option bet.
 *
 * @return the option bet
 */
@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "idOpcionApuesta")
public OpcionApuesta getOpcionApuesta() {
    return opcionApuesta;
}

/**
 * Sets the option bet.
 *
 * @param opcionApuesta
 *            the new option bet
 */
public void setOpcionApuesta(OpcionApuesta opcionApuesta) {
    this.opcionApuesta = opcionApuesta;
}

/**
 * Gets the amount wagered.
 *
 * @return the amount wagered
 */
public float getCantidadApostada() {
    return cantidadApostada;
}

/**
 * Sets the amount wagered.
 *
 * @param cantidadApostada
 *            the new amount wagered
 */
public void setCantidadApostada(float cantidadApostada) {
    this.cantidadApostada = cantidadApostada;
}

/**
 * Gets the fecha.
 *
 * @return the date
 */
public Calendar getFecha() {
    return fecha;
}

/**
 * Sets the fecha.
 *
 * @param fecha
 *            the new date
 */
public void setFecha(Calendar fecha) {
    this.fecha = fecha;
}

}
