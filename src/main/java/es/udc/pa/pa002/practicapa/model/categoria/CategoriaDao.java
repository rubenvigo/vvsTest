package es.udc.pa.pa002.practicapa.model.categoria;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface CategoriaDao extends GenericDao<Categoria,Long>{
	public List<Categoria> findAll();
}
