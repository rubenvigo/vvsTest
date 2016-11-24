package es.udc.pa.pa002.practicapa.model.opcionapuesta;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("opcionApuestaDao")
public class OpcionApuestaDaoHibernate extends
        GenericDaoHibernate<OpcionApuesta, Long> implements OpcionApuestaDao {

}
