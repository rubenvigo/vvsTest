package es.udc.pa.pa002.practicapa.model.evento;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface EventoDao.
 */
public interface EventoDao extends GenericDao<Evento, Long> {

/**
 * Find by parameters.
 *
 * @param keywords
 *            the keywords
 * @param categoriaId
 *            the categoria id
 * @param admin
 *            the admin
 * @param startIndex
 *            the start index
 * @param count
 *            the count
 * @return the list
 */
List<Evento> findByParameters(String keywords, Long categoriaId, boolean admin,
        int startIndex, int count);

/**
 * Exists event.
 *
 * @param nombre
 *            the nombre
 * @param idCategoria
 *            the id categoria
 * @param fecha
 *            the fecha
 * @return true, if successful
 */
boolean existsEvent(String nombre, Long idCategoria, Calendar fecha);

/**
 * Gets the number of eventos.
 *
 * @param keywords
 *            the keywords
 * @param idCategoria
 *            the id categoria
 * @param admin
 *            the admin
 * @return the number of eventos
 */
int getNumberOfEventos(String keywords, Long idCategoria, boolean admin);
}
