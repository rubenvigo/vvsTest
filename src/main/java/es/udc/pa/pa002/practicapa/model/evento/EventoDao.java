package es.udc.pa.pa002.practicapa.model.evento;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface EventoDao extends GenericDao<Evento, Long> {
public List<Evento> findByParameters(String keywords, Long categoriaId,
        boolean admin, int startIndex, int count);

public boolean existsEvent(String nombre, Long idCategoria, Calendar fecha);

public int getNumberOfEventos(String keywords, Long idCategoria, boolean admin);
}
