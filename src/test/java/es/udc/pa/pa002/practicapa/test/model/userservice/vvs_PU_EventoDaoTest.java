package es.udc.pa.pa002.practicapa.test.model.userservice;

import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizadaDao;
import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.categoria.CategoriaDao;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.evento.EventoDao;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class vvs_PU_EventoDaoTest {

	@Autowired
	private EventoDao eventoDao;

	@Autowired
	private CategoriaDao categoriaDao;

	@Autowired
	private UserProfileDao userProfileDao;

	@Autowired
	private ApuestaRealizadaDao apuestaRealizadaDao;

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Test
	public void saveEvento() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		eventoDao.save(evento);
		assertNotNull(evento.getIdEvento());
	}

	@Test
	public void updateEvento() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		evento.setNombre("nuevoNombre");
		Evento saveEvento = evento;
		eventoDao.save(evento);
		assertEquals(evento, saveEvento);
	}

	@Test
	public void deleteEvento() throws InstanceNotFoundException {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		eventoDao.remove(evento.getIdEvento());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void deleteEventoNotFound() throws InstanceNotFoundException {

		eventoDao.remove(1L);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void findEventoByIdNotFound() throws InstanceNotFoundException {

		eventoDao.find(1L);

	}

	@Test
	public void findEventoById() throws InstanceNotFoundException {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);

		Evento foundEvento = eventoDao.find(evento.getIdEvento());

		assertEquals(foundEvento, evento);
	}

	@Test
	public void testFindEventosByKeywordsAdmin() {

		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("este es el nombre del evento", fecha,
				categoria1);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento3);
		eventos.add(evento2);
		List<Evento> foundEventos = eventoDao.findByParameters("eVeNT nomBRe",
				null, true, 0, 10);

		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByKeywords() {

		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("este es el nombre del evento", fecha,
				categoria1);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento2);
		List<Evento> foundEventos = eventoDao.findByParameters("eVeNT nomBRe",
				null, false, 0, 10);

		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByCategoryAdmin() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre", fecha2, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento3);
		eventos.add(evento2);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters(null,
				categoria2.getIdCategoria(), true, 0, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByCategory() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre", fecha, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento2);
		eventos.add(evento3);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters(null,
				categoria2.getIdCategoria(), false, 0, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByDateAdmin() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2015, Calendar.JUNE, 11, 10, 30);
		Evento evento = new Evento("nombre1", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre2", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre3", fecha2, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento3);
		eventos.add(evento);
		eventos.add(evento2);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters(null, null, true, 0, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByDateUser() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2015, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre1", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre2", fecha2, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre3", fecha, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento);
		eventos.add(evento3);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters(null, null, false, 0, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByKeywordsCategoryAdmin() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre del evento", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("evento", fecha2, categoria2);
		getSession().save(evento3);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento2);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters("eVENt NoMBRe",
				categoria2.getIdCategoria(), true, 0, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByKeywordsCategoryUser() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("evento", fecha2, categoria2);
		getSession().save(evento3);
		Evento evento4 = new Evento("este es el nombre de un evento", fecha,
				categoria2);
		getSession().save(evento4);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento4);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters("eVENt NoMBRe",
				categoria2.getIdCategoria(), false, 0, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByKeywordsCategoryUserPageStartIndex() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("evento", fecha2, categoria2);
		getSession().save(evento3);
		Evento evento4 = new Evento("este es el nombre de un evento", fecha,
				categoria2);
		getSession().save(evento4);
		Evento evento5 = new Evento("este es el nombre de un evento", fecha,
				categoria2);
		getSession().save(evento5);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento5);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters("eVENt NoMBRe",
				categoria2.getIdCategoria(), false, 1, 10);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void testFindEventosByKeywordsCategoryUserPageCount() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("evento", fecha2, categoria2);
		getSession().save(evento3);
		Evento evento4 = new Evento("este es el nombre de un evento", fecha,
				categoria2);
		getSession().save(evento4);
		Evento evento5 = new Evento("este es el nombre de un evento", fecha,
				categoria2);
		getSession().save(evento5);
		List<Evento> eventos = new ArrayList<Evento>();
		eventos.add(evento4);
		List<Evento> foundEventos;
		foundEventos = eventoDao.findByParameters("eVENt NoMBRe",
				categoria2.getIdCategoria(), false, 0, 1);
		assertEquals(eventos, foundEventos);
	}

	@Test
	public void existsEvento() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		assertTrue(eventoDao.existsEvent("nombre", categoria.getIdCategoria(),
				fecha));
	}

	@Test
	public void NotExistsCategoryEvento() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Categoria categoria2 = new Categoria("Tenis");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		assertFalse(eventoDao.existsEvent("nombre",
				categoria2.getIdCategoria(), fecha));
	}

	@Test
	public void NotExistsDateEvento() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2017, Calendar.AUGUST, 12, 10, 30);
		assertFalse(eventoDao.existsEvent("nombre", categoria.getIdCategoria(),
				fecha2));
	}

	@Test
	public void NotExistsNameEvento() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		assertFalse(eventoDao.existsEvent("OtroNombre",
				categoria.getIdCategoria(), fecha));
	}

	@Test
	public void testGetNumberOfEventosByKeywordsAdmin() {

		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("este es el nombre del evento", fecha,
				categoria1);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento3);
		int foundEventos = eventoDao.getNumberOfEventos("eVeNT nomBRe", null,
				true);

		assertEquals(2, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByKeywords() {

		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("este es el nombre del evento", fecha,
				categoria1);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento3);
		int foundEventos = eventoDao.getNumberOfEventos("eVeNT nomBRe", null,
				false);

		assertEquals(1, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByCategoryAdmin() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre", fecha2, categoria2);
		getSession().save(evento3);
		int foundEventos;
		foundEventos = eventoDao.getNumberOfEventos(null,
				categoria2.getIdCategoria(), true);
		assertEquals(2, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByCategory() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre", fecha, categoria2);
		getSession().save(evento3);
		int foundEventos;
		foundEventos = eventoDao.getNumberOfEventos(null,
				categoria2.getIdCategoria(), false);
		assertEquals(2, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByDateAdmin() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2015, Calendar.JUNE, 11, 10, 30);
		Evento evento = new Evento("nombre1", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre2", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre3", fecha2, categoria2);
		getSession().save(evento3);
		int foundEventos;
		foundEventos = eventoDao.getNumberOfEventos(null, null, true);
		assertEquals(3, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByDateUser() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2015, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre1", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre2", fecha2, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("nombre3", fecha, categoria2);
		getSession().save(evento3);
		int foundEventos;
		foundEventos = eventoDao.getNumberOfEventos(null, null, false);
		assertEquals(2, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByKeywordsCategoryAdmin() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre del evento", fecha, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("evento", fecha2, categoria2);
		getSession().save(evento3);
		int foundEventos;
		foundEventos = eventoDao.getNumberOfEventos("eVENt NoMBRe",
				categoria2.getIdCategoria(), true);
		assertEquals(1, foundEventos);
	}

	@Test
	public void testGetNumberOfEventosByKeywordsCategoryUser() {
		Categoria categoria1 = new Categoria("Futbol");
		getSession().save(categoria1);
		Categoria categoria2 = new Categoria("Baloncesto");
		getSession().save(categoria2);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Calendar fecha2 = Calendar.getInstance();
		fecha2.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre evento", fecha, categoria1);
		getSession().save(evento);
		Evento evento2 = new Evento("nombre del evento", fecha2, categoria2);
		getSession().save(evento2);
		Evento evento3 = new Evento("evento", fecha2, categoria2);
		getSession().save(evento3);
		Evento evento4 = new Evento("este es el nombre de un evento", fecha,
				categoria2);
		getSession().save(evento4);
		int foundEventos;
		foundEventos = eventoDao.getNumberOfEventos("eVENt NoMBRe",
				categoria2.getIdCategoria(), false);
		assertEquals(1, foundEventos);
	}

}
