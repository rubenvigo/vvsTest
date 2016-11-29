package es.udc.pa.pa002.practicapa.model.apuestarealizada;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface ApuestaRealizadaDao.
 */
public interface ApuestaRealizadaDao extends GenericDao<ApuestaRealizada, Long> {

/**
 * Find by user.
 *
 * @param userId
 *            the user id
 * @param startIndex
 *            the start index
 * @param count
 *            the count
 * @return the list
 */
public List<ApuestaRealizada> findByUser(Long userId, int startIndex, int count);
}
