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

@Entity
@BatchSize(size = 10)
public class TipoApuesta {

private Long idTipoApuesta;
private String pregunta;
private Evento evento;
private Set<OpcionApuesta> opcionesApuesta;
private boolean multiplesGanadoras;

public TipoApuesta() {

}

public TipoApuesta(Evento evento, String pregunta,
        Set<OpcionApuesta> opcionesApuesta, boolean multiplesGanadoras) {

    this.pregunta = pregunta;
    this.opcionesApuesta = opcionesApuesta;
    this.multiplesGanadoras = multiplesGanadoras;
}

public TipoApuesta(String pregunta, Set<OpcionApuesta> opcionesApuesta,
        boolean multiplesGanadoras) {

    this.pregunta = pregunta;
    this.opcionesApuesta = opcionesApuesta;
    this.multiplesGanadoras = multiplesGanadoras;
}

@SequenceGenerator(name = "idTipoApuestaGenerator", sequenceName = "tipoApuestaSeq")
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "idTipoApuestaGenerator")
public Long getIdTipoApuesta() {
    return idTipoApuesta;
}

public void setIdTipoApuesta(Long idTipoApuesta) {
    this.idTipoApuesta = idTipoApuesta;
}

public String getPregunta() {
    return pregunta;
}

public void setPregunta(String pregunta) {
    this.pregunta = pregunta;
}

@ManyToOne(optional = false)
@JoinColumn(name = "idEvento")
public Evento getEvento() {
    return evento;
}

public void setEvento(Evento evento) {
    this.evento = evento;
}

@OneToMany(mappedBy = "tipoApuesta")
public Set<OpcionApuesta> getOpcionesApuesta() {
    return opcionesApuesta;
}

public void setOpcionesApuesta(Set<OpcionApuesta> opcionesApuesta) {
    this.opcionesApuesta = opcionesApuesta;
}

public boolean isMultiplesGanadoras() {
    return multiplesGanadoras;
}

public void setMultiplesGanadoras(boolean multiplesGanadoras) {
    this.multiplesGanadoras = multiplesGanadoras;
}

public boolean existsOpcionApuesta(String nombre) {
    for (OpcionApuesta opcionApuesta : opcionesApuesta) {
        if (opcionApuesta.getRespuesta().equals(nombre))
            return true;
    }
    return false;
}

}
