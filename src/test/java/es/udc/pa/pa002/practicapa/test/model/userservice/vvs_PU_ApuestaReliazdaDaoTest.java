package es.udc.pa.pa002.practicapa.test.model.userservice;

import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizadaDao;
import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.categoria.CategoriaDao;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.evento.EventoDao;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa002.practicapa.model.userservice.util.PasswordEncrypter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class vvs_PU_ApuestaReliazdaDaoTest {

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
	public void findApuestasUsuario() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		tipoApuesta.setEvento(evento);
		getSession().save(tipoApuesta);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 2, null);
		opcionApuesta2.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta2);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);
		ApuestaRealizada apuesta = new ApuestaRealizada(user, opcionApuesta, 2,
				fecha);
		getSession().save(apuesta);

		ApuestaRealizada apuesta2 = new ApuestaRealizada(user, opcionApuesta2,
				5, fecha);
		getSession().save(apuesta2);
		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta);
		apuestas.add(apuesta2);
		assertEquals(
				apuestaRealizadaDao.findByUser(user.getUserProfileId(), 0, 10),
				apuestas);

	}

	@Test
	public void findApuestasUsuarioPageStartIndex() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		tipoApuesta.setEvento(evento);
		getSession().save(tipoApuesta);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 2, null);
		opcionApuesta2.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta2);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);
		ApuestaRealizada apuesta = new ApuestaRealizada(user, opcionApuesta, 2,
				fecha);
		getSession().save(apuesta);

		ApuestaRealizada apuesta2 = new ApuestaRealizada(user, opcionApuesta2,
				5, fecha);
		getSession().save(apuesta2);
		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta2);
		assertEquals(
				apuestaRealizadaDao.findByUser(user.getUserProfileId(), 1, 10),
				apuestas);

	}

	@Test
	public void findApuestasUsuarioPageCount() {
		Categoria categoria = new Categoria("Futbol");
		getSession().save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		getSession().save(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		tipoApuesta.setEvento(evento);
		getSession().save(tipoApuesta);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 2, null);
		opcionApuesta2.setTipoApuesta(tipoApuesta);
		getSession().save(opcionApuesta2);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		getSession().save(user);
		ApuestaRealizada apuesta = new ApuestaRealizada(user, opcionApuesta, 2,
				fecha);
		getSession().save(apuesta);

		ApuestaRealizada apuesta2 = new ApuestaRealizada(user, opcionApuesta2,
				5, fecha);
		getSession().save(apuesta2);
		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta);
		assertEquals(
				apuestaRealizadaDao.findByUser(user.getUserProfileId(), 0, 1),
				apuestas);

	}

	@Test
	public void findApuestasUsuarioNotFound() {

		assertTrue(apuestaRealizadaDao.findByUser(1L, 0, 1).isEmpty());

	}

}
