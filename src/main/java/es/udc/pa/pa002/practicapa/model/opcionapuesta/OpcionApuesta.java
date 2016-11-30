package es.udc.pa.pa002.practicapa.model.opcionapuesta;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;

/**
 * The Class OpcionApuesta.
 */
@Entity
@BatchSize(size = 10)
public class OpcionApuesta {

/** The id opcion apuesta. */
private Long idOpcionApuesta;

/** The respuesta. */
private String respuesta;

/** The cuota. */
private float cuota;

/** The estado. */
private Boolean estado;

/** The tipo apuesta. */
private TipoApuesta tipoApuesta;

/**
 * Instantiates a new opcion apuesta.
 */
public OpcionApuesta() {

}

/**
 * Instantiates a new opcion apuesta.
 *
 * @param respuesta
 *            the respuesta
 * @param cuota
 *            the cuota
 * @param estado
 *            the estado
 */
public OpcionApuesta(String respuesta, float cuota, Boolean estado) {
    super();
    this.respuesta = respuesta;
    this.cuota = cuota;
    this.estado = estado;
}

/**
 * Gets the id opcion apuesta.
 *
 * @return the id opcion apuesta
 */
@SequenceGenerator(// It only takes effect for
name = "OpcionApuestaIdGenerator", // databases providing identifier
sequenceName = "OpcionApuestaSeq")
// generators.
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "OpcionApuestaIdGenerator")
public final Long getIdOpcionApuesta() {
    return idOpcionApuesta;
}

/**
 * Sets the id opcion apuesta.
 *
 * @param idOpcionApuesta
 *            the new id opcion apuesta
 */
public final void setIdOpcionApuesta(Long idOpcionApuesta) {
    this.idOpcionApuesta = idOpcionApuesta;
}

/**
 * Gets the respuesta.
 *
 * @return the respuesta
 */
public final String getRespuesta() {
    return respuesta;
}

/**
 * Sets the respuesta.
 *
 * @param respuesta
 *            the new respuesta
 */
public final void setRespuesta(String respuesta) {
    this.respuesta = respuesta;
}

/**
 * Gets the cuota.
 *
 * @return the cuota
 */
public final float getCuota() {
    return cuota;
}

/**
 * Sets the cuota.
 *
 * @param cuota
 *            the new cuota
 */
public final void setCuota(float cuota) {
    this.cuota = cuota;
}

/**
 * Gets the estado.
 *
 * @return the estado
 */
public final Boolean getEstado() {
    return estado;
}

/**
 * Sets the estado.
 *
 * @param estado
 *            the new estado
 */
public final void setEstado(Boolean estado) {
    this.estado = estado;
}

/**
 * Gets the tipo apuesta.
 *
 * @return the tipo apuesta
 */
@ManyToOne(optional = false)
@JoinColumn(name = "idTipoApuesta")
public final TipoApuesta getTipoApuesta() {
    return tipoApuesta;
}

/**
 * Sets the tipo apuesta.
 *
 * @param tipoApuesta
 *            the new tipo apuesta
 */
public final void setTipoApuesta(TipoApuesta tipoApuesta) {
    this.tipoApuesta = tipoApuesta;
}

}
