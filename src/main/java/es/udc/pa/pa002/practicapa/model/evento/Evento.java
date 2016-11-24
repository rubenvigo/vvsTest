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

@Entity
@BatchSize(size = 10)
public class Evento {

private Long idEvento;
private String nombre;

private Calendar fecha;
private Categoria categoria;
private Set<TipoApuesta> tipoApuesta = new HashSet<>();

public Evento() {
}

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

@SequenceGenerator(// It only takes effect for
name = "idEventoGenerator", // databases providing identifier
sequenceName = "EventoSeq")
// generators.
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "idEventoGenerator")
public Long getIdEvento() {
    return idEvento;
}

public void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

@Temporal(TemporalType.TIMESTAMP)
public Calendar getFecha() {
    return fecha;
}

public void setFecha(Calendar fecha) {
    if (fecha != null) {
        fecha.set(Calendar.SECOND, 0);
        fecha.set(Calendar.MILLISECOND, 0);
    }
    this.fecha = fecha;
}

@ManyToOne(optional = false, fetch = FetchType.LAZY)
@JoinColumn(name = "idCategoria")
public Categoria getCategoria() {
    return categoria;
}

public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
}

@OneToMany(mappedBy = "evento")
public Set<TipoApuesta> getTipoApuesta() {
    return tipoApuesta;
}

public void setTipoApuesta(Set<TipoApuesta> tipoApuesta) {
    this.tipoApuesta = tipoApuesta;
}

public void addTipoApuesta(TipoApuesta tipoApuesta) {
    this.tipoApuesta.add(tipoApuesta);
}

public boolean existsTipoApuesta(String nombre) {
    for (TipoApuesta tA : tipoApuesta) {
        if (tA.getPregunta().equals(nombre))
            return true;
    }
    return false;
}

}
