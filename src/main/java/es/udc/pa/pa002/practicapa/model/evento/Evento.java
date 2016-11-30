package es.udc.pa.pa002.practicapa.model.evento;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;

/**
 * The Class Evento.
 */
@Entity
@BatchSize(size = 10)
public class Evento {

/** The id evento. */
private Long idEvento;

/** The nombre. */
private String nombre;

/** The fecha. */
private Calendar fecha;

/** The categoria. */
private Categoria categoria;

/** The tipo apuesta. */
private Set<TipoApuesta> tipoApuesta = new HashSet<>();

/**
 * Instantiates a new evento.
 */
public Evento() {
}

/**
 * Instantiates a new evento.
 *
 * @param nombre
 *            the nombre
 * @param fecha
 *            the fecha
 * @param categoria
 *            the categoria
 */
public Evento(String nombre, Calendar fecha, Categoria categoria) {
    super();
    this.nombre = nombre;
    if (fecha != null) {
        fecha.set(Calendar.SECOND, 0);
        fecha.set(Calendar.MILLISECOND, 0);
    }
    this.fecha = fecha;
    this.categoria = categoria;
}

/**
 * Gets the id evento.
 *
 * @return the id evento
 */
@SequenceGenerator(// It only takes effect for
name = "idEventoGenerator", // databases providing identifier
sequenceName = "EventoSeq")
// generators.
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "idEventoGenerator")
public final Long getIdEvento() {
    return idEvento;
}

/**
 * Sets the id evento.
 *
 * @param idEvento
 *            the new id evento
 */
public final void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

/**
 * Gets the nombre.
 *
 * @return the nombre
 */
public final String getNombre() {
    return nombre;
}

/**
 * Sets the nombre.
 *
 * @param nombre
 *            the new nombre
 */
public final void setNombre(String nombre) {
    this.nombre = nombre;
}

/**
 * Gets the fecha.
 *
 * @return the fecha
 */
@Temporal(TemporalType.TIMESTAMP)
public final Calendar getFecha() {
    return fecha;
}

/**
 * Sets the fecha.
 *
 * @param fecha
 *            the new fecha
 */
public final void setFecha(Calendar fecha) {
    if (fecha != null) {
        fecha.set(Calendar.SECOND, 0);
        fecha.set(Calendar.MILLISECOND, 0);
    }
    this.fecha = fecha;
}

/**
 * Gets the categoria.
 *
 * @return the categoria
 */
@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "idCategoria")
public final Categoria getCategoria() {
    return categoria;
}

/**
 * Sets the categoria.
 *
 * @param categoria
 *            the new categoria
 */
public final void setCategoria(Categoria categoria) {
    this.categoria = categoria;
}

/**
 * Gets the tipo apuesta.
 *
 * @return the tipo apuesta
 */
@OneToMany(mappedBy = "evento")
public final Set<TipoApuesta> getTipoApuesta() {
    return tipoApuesta;
}

/**
 * Sets the tipo apuesta.
 *
 * @param tipoApuesta
 *            the new tipo apuesta
 */
public final void setTipoApuesta(Set<TipoApuesta> tipoApuesta) {
    this.tipoApuesta = tipoApuesta;
}

/**
 * Adds the tipo apuesta.
 *
 * @param tipoApuesta
 *            the tipo apuesta
 */
public final void addTipoApuesta(TipoApuesta tipoApuesta) {
    this.tipoApuesta.add(tipoApuesta);
}

/**
 * Exists tipo apuesta.
 *
 * @param nombre
 *            the nombre
 * @return true, if successful
 */
public final boolean existsTipoApuesta(String nombre) {
    for (TipoApuesta tA : tipoApuesta) {
        if (tA.getPregunta().equals(nombre)) {
            return true;
        }
    }
    return false;
}

}
