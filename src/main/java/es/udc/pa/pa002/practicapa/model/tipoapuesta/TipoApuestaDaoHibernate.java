package es.udc.pa.pa002.practicapa.model.tipoapuesta;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class TipoApuestaDaoHibernate.
 */
@Repository("tipoApuestaDao")
public class TipoApuestaDaoHibernate extends
        GenericDaoHibernate<TipoApuesta, Long> implements TipoApuestaDao {

}
