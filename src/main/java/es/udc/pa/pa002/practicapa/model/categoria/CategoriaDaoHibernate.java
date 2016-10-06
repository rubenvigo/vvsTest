package es.udc.pa.pa002.practicapa.model.categoria;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("categoriaDao")
public class CategoriaDaoHibernate extends GenericDaoHibernate<Categoria,Long> implements CategoriaDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> findAll() {
		return getSession().createQuery("SELECT a FROM Categoria a ").list();
	}

}
