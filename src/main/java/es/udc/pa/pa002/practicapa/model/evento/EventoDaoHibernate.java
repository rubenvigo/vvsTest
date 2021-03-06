package es.udc.pa.pa002.practicapa.model.evento;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class EventoDaoHibernate.
 */
@Repository("eventoDao")
public class EventoDaoHibernate extends GenericDaoHibernate<Evento, Long>
        implements EventoDao {

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
@SuppressWarnings("unchecked")
@Override
public final List<Evento> findByParameters(final String keywords,
        final Long idCategoria, final boolean admin, final int startIndex,
        final int count) {

    String query = "SELECT a FROM Evento a";

    String[] words = null;
    if (keywords != null) {
        words = keywords.split(" ");
    }

    if (keywords != null || idCategoria != null || !admin) {
        query = query + " WHERE";
    }
    String nombre;
    if (words != null && words.length > 0) {
        for (int j = 0; j < words.length; j++) {
            nombre = "nombre";
            nombre += j;
            if (j > 0) {
                query += " AND";
            }
            query += " LOWER(a.nombre) LIKE LOWER(:" + nombre + ")";
        }
    }
    if (idCategoria != null) {
        if (keywords != null) {
            query += " AND";
        }
        query += " a.categoria.idCategoria = :idCategoria";
    }
    if (!admin) {
        if (keywords != null || idCategoria != null) {
            query += " AND";
        }
        query += " a.fecha > :fecha";
    }
    query += " ORDER BY a.fecha";

    if (keywords == null && idCategoria == null && admin) {
        return getSession().createQuery(query).setFirstResult(startIndex)
                .setMaxResults(count).list();
    }
    Session session = getSession();

    Query consulta = session.createQuery(query);
    if (words != null && words.length > 0) {
        for (int j = 0; j < words.length; j++) {
            nombre = "nombre";
            nombre += j;
            consulta.setString(nombre, "%" + words[j] + "%");
        }
    }
    if (idCategoria != null) {
        consulta.setLong("idCategoria", idCategoria);
    }
    if (!admin) {
        consulta.setCalendar("fecha", Calendar.getInstance());
    }

    return consulta.setFirstResult(startIndex).setMaxResults(count).list();
}

@Override
public final boolean existsEvent(final String nombre, final Long idCategoria,
        final Calendar fecha) {
    Session session = getSession();
    Query query = session.createQuery("SELECT COUNT(a) FROM Evento a "
            + "WHERE LOWER(a.nombre) LIKE LOWER(:nombre) "
            + "AND a.categoria.idCategoria = :idCategoria "
            + "AND a.fecha = :fecha");
    query.setString("nombre", nombre);
    query.setLong("idCategoria", idCategoria);
    query.setCalendar("fecha", fecha);
    // List<Evento> eventos = new ArrayList();
    int eventos = ((Long) query.uniqueResult()).intValue();
    return eventos > 0;
}

@Override
public final int getNumberOfEventos(final String keywords,
        final Long idCategoria, final boolean admin) {

    String query = "SELECT COUNT(a) FROM Evento a";

    String[] words = null;
    if (keywords != null) {
        words = keywords.split(" ");
    }

    if (keywords != null || idCategoria != null || !admin) {
        query = query + " WHERE";
    }
    String nombre;
    if (words != null && words.length > 0) {
        for (int j = 0; j < words.length; j++) {
            nombre = "nombre";
            nombre += j;
            if (j > 0) {
                query += " AND";
            }
            query += " LOWER(a.nombre) LIKE LOWER(:" + nombre + ")";
        }
    }
    if (idCategoria != null) {
        if (keywords != null) {
            query += " AND";
        }
        query += " a.categoria.idCategoria = :idCategoria";
    }
    if (!admin) {
        if (keywords != null || idCategoria != null) {
            query += " AND";
        }
        query += " a.fecha > :fecha";
    }

    if (keywords == null && idCategoria == null && admin) {
        long numEvents = (Long) getSession().createQuery(query).uniqueResult();
        return (int) numEvents;
    }
    Session session = getSession();

    Query consulta = session.createQuery(query);
    if (words != null && words.length > 0) {
        for (int j = 0; j < words.length; j++) {
            nombre = "nombre";
            nombre += j;
            consulta.setString(nombre, "%" + words[j] + "%");
        }
    }
    if (idCategoria != null) {
        consulta.setLong("idCategoria", idCategoria);
    }
    if (!admin) {
        consulta.setCalendar("fecha", Calendar.getInstance());
    }
    long numEvents = (Long) consulta.uniqueResult();
    return (int) numEvents;
}

}
