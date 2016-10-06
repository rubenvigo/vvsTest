package es.udc.pa.pa002.practicapa.model.categoria;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@BatchSize(size=10)
public class Categoria {

	private Long idCategoria;
	private String nombre;
	
	public Categoria(){
	}
	
	public Categoria(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	@SequenceGenerator( // It only takes effect for
	name = "idCategoriaGenerator", // databases providing identifier
	sequenceName = "CategoriaSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "idCategoriaGenerator")
	public Long getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(Long idCategoria) {
		this.idCategoria = idCategoria;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}

