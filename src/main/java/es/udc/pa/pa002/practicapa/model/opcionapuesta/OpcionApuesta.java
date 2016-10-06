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

@Entity
@BatchSize(size=10)
public class OpcionApuesta {
	private Long idOpcionApuesta;
	private String respuesta;
	private float cuota;
	private Boolean estado;
	private TipoApuesta tipoApuesta;
	
	public OpcionApuesta(){
		
	}
	

	public OpcionApuesta(String respuesta, float cuota, Boolean estado) {
		super();
		this.respuesta = respuesta;
		this.cuota = cuota;
		this.estado = estado;
	}


	@SequenceGenerator( 					  // It only takes effect for
	name = "OpcionApuestaIdGenerator",// databases providing identifier
	sequenceName = "OpcionApuestaSeq")// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "OpcionApuestaIdGenerator")
	public Long getIdOpcionApuesta() {
		return idOpcionApuesta;
	}
	public void setIdOpcionApuesta(Long idOpcionApuesta) {
		this.idOpcionApuesta = idOpcionApuesta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public float getCuota() {
		return cuota;
	}
	public void setCuota(float cuota) {
		this.cuota = cuota;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	
	@ManyToOne(optional=false)
	@JoinColumn(name="idTipoApuesta")
	public TipoApuesta getTipoApuesta() {
		return tipoApuesta;
	}

	public void setTipoApuesta(TipoApuesta tipoApuesta) {
		this.tipoApuesta = tipoApuesta;
	}
	
	
}
