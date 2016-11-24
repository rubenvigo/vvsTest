package es.udc.pa.pa002.practicapa.model.apuestarealizada;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface ApuestaRealizadaDao extends GenericDao<ApuestaRealizada, Long> {
public List<ApuestaRealizada> findByUser(Long userId, int startIndex, int count);
}
