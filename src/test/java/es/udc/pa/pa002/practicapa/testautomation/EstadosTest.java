package es.udc.pa.pa002.practicapa.testautomation;

import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.graphwalker.core.condition.EdgeCoverage;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.graphwalker.java.test.TestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.categoria.CategoriaDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@GraphWalker(value = "random(edge_coverage(100))", start = "save")
public class EstadosTest extends ExecutionContext implements Estados {

	public final static Path MODEL_PATH = Paths
			.get("es/udc/pa/pa002/practicapa/testautomation/Estados.graphml");

	private Categoria categoria;

	@Autowired
	private CategoriaDao categoriaDao;

	private boolean NotFound = false;

	@Override
	public void Creado() {
		System.out.println("Objecto Creado");
		assertNotNull(categoria.getIdCategoria());
		assertFalse(NotFound);
	}

	@Override
	public void remove() {
		System.out.println("Borrando Objecto");
		try {
			categoriaDao.remove(categoria.getIdCategoria());
			NotFound = true;
		} catch (InstanceNotFoundException e) {
			NotFound = true;
		}
	}

	@Override
	public void saveOrUpdate() {
		System.out.println("Actualizando Objecto");
		categoriaDao.save(categoria);

	}

	@Override
	public void Eliminado() {
		System.out.println("Objecto Eliminado");
		assertTrue(NotFound);
	}

	@Override
	public void find() {
		NotFound = false;
		System.out.println("Buscando Objecto");
		try {
			Categoria foundCategoria = categoriaDao.find(categoria
					.getIdCategoria());
			assertEquals(categoria, foundCategoria);
		} catch (InstanceNotFoundException e) {
			NotFound = true;
		}
	}

	@Override
	public void save() {
		System.out.println("Inicializando Objecto");
		categoria = new Categoria("Futbol");
		categoriaDao.save(categoria);
		System.out.println("Inicializando Objecto3");

	}

	@Test
	public void runFunctionalTest() {
		new TestBuilder().setModel(MODEL_PATH).setContext(new EstadosTest())
				.setPathGenerator(new RandomPath(new EdgeCoverage(100)))
				.setStart("save").execute();
	}

}
