package es.udc.pa.pa002.practicapa.model.apuestarealizada;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class ApuestaRealizadaDaoHibernate.
 */
@Repository("apuestaRealizadaDao")
public class ApuestaRealizadaDaoHibernate extends
        GenericDaoHibernate<ApuestaRealizada, Long> implements
        ApuestaRealizadaDao {

@SuppressWarnings("unchecked")
@Override
public final List<ApuestaRealizada> findByUser(final Long userId,
        final int startIndex, final int count) {
    return getSession()
            .createQuery(
                    "SELECT a FROM ApuestaRealizada a "
                            + "WHERE a.usuario.userProfileId = :userId ORDER BY a.fecha DESC")
            .setLong("userId", userId).setFirstResult(startIndex)
            .setMaxResults(count).list();
}

}
