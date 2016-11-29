package es.udc.pa.pa002.practicapa.model.categoria;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;

/**
 * The Class Categoria.
 */
@Entity
@Immutable
@BatchSize(size = 10)
public class Categoria {

/** The id categoria. */
private Long idCategoria;

/** The nombre. */
private String nombre;

/**
 * Instantiates a new categoria.
 */
public Categoria() {
}

/**
 * Instantiates a new categoria.
 *
 * @param nombre
 *            the nombre
 */
public Categoria(String nombre) {
    super();
    this.nombre = nombre;
}

/**
 * Gets the id categoria.
 *
 * @return the id categoria
 */
@SequenceGenerator(// It only takes effect for
name = "idCategoriaGenerator", // databases providing identifier
sequenceName = "CategoriaSeq")
// generators.
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "idCategoriaGenerator")
public Long getIdCategoria() {
    return idCategoria;
}

/**
 * Sets the id categoria.
 *
 * @param idCategoria
 *            the new id categoria
 */
public void setIdCategoria(Long idCategoria) {
    this.idCategoria = idCategoria;
}

/**
 * Gets the nombre.
 *
 * @return the nombre
 */
public String getNombre() {
    return nombre;
}

/**
 * Sets the nombre.
 *
 * @param nombre
 *            the new nombre
 */
public void setNombre(String nombre) {
    this.nombre = nombre;
}

}
