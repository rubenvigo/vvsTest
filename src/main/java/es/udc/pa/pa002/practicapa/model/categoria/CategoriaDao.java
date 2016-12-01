package es.udc.pa.pa002.practicapa.model.categoria;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface CategoriaDao.
 */
public interface CategoriaDao extends GenericDao<Categoria, Long> {

/**
 * Find all categories.
 *
 * @return the list
 */
List<Categoria> findAll();
}