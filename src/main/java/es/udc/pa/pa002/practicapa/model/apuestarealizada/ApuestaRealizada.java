package es.udc.pa.pa002.practicapa.model.apuestarealizada;

import java.util.Calendar;

import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class ApuestaRealizada {
	private Long idApuestaRealizada;
	private UserProfile usuario;
	private OpcionApuesta opcionApuesta;
	private float cantidadApostada;
	private Calendar fecha;
	
	public ApuestaRealizada(){
		
	}
	
	

	public ApuestaRealizada(UserProfile usuario, OpcionApuesta opcionApuesta,
			float cantidadApostada, Calendar fecha) {
		super();
		this.usuario = usuario;
		this.opcionApuesta = opcionApuesta;
		this.cantidadApostada = cantidadApostada;
		this.fecha = fecha;
	}



	@SequenceGenerator( 					     // It only takes effect for
			name = "ApuestaRealizadaIdGenerator",// databases providing identifier
			sequenceName = "ApuestaRealizadaSeq")// generators.
			@Id
			@GeneratedValue(strategy = GenerationType.AUTO, generator = "ApuestaRealizadaIdGenerator")
	
	public Long getIdApuestaRealizada() {
		return idApuestaRealizada;
	}

	public void setIdApuestaRealizada(Long idApuestaRealizada) {
		this.idApuestaRealizada = idApuestaRealizada;
	}

	@ManyToOne(optional= false, fetch = FetchType.LAZY)
	@JoinColumn(name="idUsuario")
	public UserProfile getUsuario() {
		return usuario;
	}

	public void setUsuario(UserProfile usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne(optional= false, fetch = FetchType.LAZY)
	@JoinColumn(name="idOpcionApuesta")
	public OpcionApuesta getOpcionApuesta() {
		return opcionApuesta;
	}

	public void setOpcionApuesta(OpcionApuesta opcionApuesta) {
		this.opcionApuesta = opcionApuesta;
	}

	public float getCantidadApostada() {
		return cantidadApostada;
	}

	public void setCantidadApostada(float cantidadApostada) {
		this.cantidadApostada = cantidadApostada;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	
	

}
