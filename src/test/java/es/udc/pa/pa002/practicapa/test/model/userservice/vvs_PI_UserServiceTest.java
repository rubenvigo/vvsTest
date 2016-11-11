package es.udc.pa.pa002.practicapa.test.model.userservice;

import static com.pholser.junit.quickcheck.internal.Ranges.checkRange;
import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuestaDao;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuestaDao;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userservice.ApuestaBlock;
import es.udc.pa.pa002.practicapa.model.userservice.EventoBlock;
import es.udc.pa.pa002.practicapa.model.userservice.EventoNotStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.EventoStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa002.practicapa.model.userservice.InputValidationException;
import es.udc.pa.pa002.practicapa.model.userservice.InstanceAlreadyCreatedException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidDateException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidOptionException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidValueException;
import es.udc.pa.pa002.practicapa.model.userservice.OpcionApuestaAlreadySolvedException;
import es.udc.pa.pa002.practicapa.model.userservice.RepeatedOpcionApuestaException;
import es.udc.pa.pa002.practicapa.model.userservice.SimpleWinnerException;
import es.udc.pa.pa002.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class vvs_PI_UserServiceTest {

	@Autowired
	private UserService userService;
	@Autowired
	private EventoDao eventoDao;
	@Autowired
	private CategoriaDao categoriaDao;
	@Autowired
	private TipoApuestaDao tipoApuestaDao;
	@Autowired
	private OpcionApuestaDao opcionApuestaDao;
	@Autowired
	private ApuestaRealizadaDao apuestaRealizadaDao;

	private Random delegate = new Random();

	/*
	 * Antes de cada método de test se encuentra su identificador, mediante el
	 * cual podemos visualizar el diseño de dicho metodo. Los ficheros de diseño
	 * se encuentran en el directorio doc del proyecto.
	 */

	// Metodo auxiliar
	private UserProfile registerUser(String loginName, String clearPassword) {

		UserProfileDetails userProfileDetails = new UserProfileDetails("name",
				"lastName", "user@udc.es");

		try {

			return userService.registerUser(loginName, clearPassword,
					userProfileDetails);

		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

	}

	public double nextDouble() {
		return delegate.nextDouble();
	}

	public double nextDouble(double min, double max) {
		int comparison = checkRange(null, min, max);
		return comparison == 0 ? min : min + (max - min) * nextDouble();
	}

	/*
	 * PR-IN-001
	 */

	@Test
	public void testRegisterUser() throws DuplicateInstanceException,
			InstanceNotFoundException {
		UserProfile userProfile = userService.registerUser("user",
				"userPassword", new UserProfileDetails("name", "lastName",
						"user@udc.es"));
		assertNotNull(userProfile.getUserProfileId());

	}

	/*
	 * PR-IN-002
	 */
	@Test(expected = DuplicateInstanceException.class)
	public void testRegisterDuplicatedUser() throws DuplicateInstanceException,
			InstanceNotFoundException {

		String loginName = "user";
		String clearPassword = "userPassword";
		UserProfileDetails userProfileDetails = new UserProfileDetails("name",
				"lastName", "user@udc.es");

		userService.registerUser(loginName, clearPassword, userProfileDetails);

		userService.registerUser(loginName, clearPassword, userProfileDetails);

	}

	/*
	 * PR-IN-003
	 */

	@Test
	public void login() throws InstanceNotFoundException,
			IncorrectPasswordException {
		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);

		UserProfile userProfile2 = userService.login(
				userProfile.getLoginName(), clearPassword, false);

		assertEquals(userProfile, userProfile2);

	}

	/*
	 * PR-IN-004
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testLoginWithNonExistentUser()
			throws IncorrectPasswordException, InstanceNotFoundException {

		userService.login("user", "userPassword", false);

	}

	/*
	 * PR-IN-005
	 */
	@Test
	public void testFindUser() throws InstanceNotFoundException {
		UserProfile userProfile = registerUser("user", "userPassword");
		assertEquals(userProfile,
				userService.findUserProfile(userProfile.getUserProfileId()));

	}

	/*
	 * PR-IN-006
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void testFindUserNotFound() throws InstanceNotFoundException {

		userService.findUserProfile((long) nextDouble(-100000, 1000000));

	}

	/*
	 * PR-IN-007
	 */
	@Test
	public void testUpdate() throws InstanceNotFoundException,
			IncorrectPasswordException {

		UserProfile userProfile = registerUser("user", "userPassword");

		UserProfileDetails newUserProfileDetails = new UserProfileDetails(
				"newName", "newLastName", "newEmail");

		userService.updateUserProfileDetails(userProfile.getUserProfileId(),
				newUserProfileDetails);

		userService.login(userProfile.getLoginName(), "userPassword", false);
		UserProfile userProfile2 = userService.findUserProfile(userProfile
				.getUserProfileId());

		assertEquals(newUserProfileDetails.getFirstName(),
				userProfile2.getFirstName());
		assertEquals(newUserProfileDetails.getLastName(),
				userProfile2.getLastName());
		assertEquals(newUserProfileDetails.getEmail(), userProfile2.getEmail());

	}

	/*
	 * PR-IN-008
	 */
	@Test
	public void changePassword() throws InstanceNotFoundException,
			IncorrectPasswordException {

		UserProfile userProfile = registerUser("user", "pass");

		userService.changePassword(userProfile.getUserProfileId(), "pass",
				"newPassword");
		assertTrue(PasswordEncrypter.isClearPasswordCorrect("newPassword",
				userProfile.getEncryptedPassword()));

	}

	/*
	 * PR-IN-009
	 */

	@Test(expected = IncorrectPasswordException.class)
	public void changePasswordIncorrectOld() throws InstanceNotFoundException,
			IncorrectPasswordException {

		UserProfile userProfile = registerUser("user", "userPassword");
		userService.changePassword(userProfile.getUserProfileId(),
				"incorrectPassword", "newPassword");

	}

	/*
	 * PR-IN-010
	 */
	@Test
	public void addEvento() throws InstanceNotFoundException,
			InvalidDateException, InstanceAlreadyCreatedException {
		Categoria categoria = new Categoria("Futbol");
		categoriaDao.save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		userService.addEvento(evento);

	}

	/*
	 * PR-IN-011
	 */
	@Test(expected = InvalidDateException.class)
	public void addPastEvent() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		Categoria categoria = new Categoria("Futbol");
		categoriaDao.save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2015, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		userService.addEvento(evento);
	}

	/*
	 * PR-IN-012
	 */
	@Test(expected = InstanceAlreadyCreatedException.class)
	public void addExistentEvent() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		Categoria categoria = new Categoria("Futbol");
		categoriaDao.save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		userService.addEvento(evento);
		Evento evento2 = new Evento("NOMBRE", fecha, categoria);
		userService.addEvento(evento2);
	}

	/*
	 * PR-IN-013
	 */
	@Test
	public void findEventosExistsMore() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		Categoria categoria = new Categoria("Futbol");
		categoriaDao.save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		List<Evento> listaEventos = new ArrayList<Evento>();
		Evento evento = new Evento("nombre1", fecha, categoria);
		userService.addEvento(evento);
		Evento evento2 = new Evento("nombre2", fecha, categoria);
		userService.addEvento(evento2);
		Evento evento3 = new Evento("nombre3", fecha, categoria);
		userService.addEvento(evento3);
		listaEventos.add(evento);
		listaEventos.add(evento2);

		EventoBlock eventoBlock = userService.findEventos(null, null, true, 0,
				2);
		assertTrue(eventoBlock.getExistMoreEventos());
		assertEquals(eventoBlock.getEventos(), listaEventos);
	}

	/*
	 * PR-IN-014
	 */
	@Test
	public void findEventosNotEventos() {
		EventoBlock eventoBlock = userService.findEventos(null, null, true, 0,
				2);
		assertFalse(eventoBlock.getExistMoreEventos());
		assertTrue(eventoBlock.getEventos().isEmpty());
	}

	/*
	 * PR-IN-015
	 */
	@Test
	public void findEventosNotExistsMore() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		Categoria categoria = new Categoria("Futbol");
		categoriaDao.save(categoria);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		List<Evento> listaEventos = new ArrayList<Evento>();
		Evento evento = new Evento("nombre1", fecha, categoria);
		userService.addEvento(evento);
		Evento evento2 = new Evento("nombre2", fecha, categoria);
		userService.addEvento(evento2);
		listaEventos.add(evento);
		listaEventos.add(evento2);

		EventoBlock eventoBlock = userService.findEventos(null, null, true, 0,
				2);
		assertFalse(eventoBlock.getExistMoreEventos());
		assertEquals(eventoBlock.getEventos(), listaEventos);
	}

	/*
	 * PR-IN-016
	 */
	@Test
	public void addTipoApuesta() throws InstanceNotFoundException,
			EventoStartedException, InvalidDateException,
			InstanceAlreadyCreatedException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		tipoApuesta.setOpcionesApuesta(opcionesApuesta);
		TipoApuesta addedTipoApuesta = userService.addTipoApuesta(
				addEvento.getIdEvento(), tipoApuesta);
		assertNotNull(addedTipoApuesta.getIdTipoApuesta());

		for (OpcionApuesta addedOpcionApuesta : tipoApuesta
				.getOpcionesApuesta()) {
			assertNotNull(addedOpcionApuesta.getIdOpcionApuesta());
		}

	}

	/*
	 * PR-IN-017
	 */
	@Test(expected = InstanceNotFoundException.class)
	public void addTipoApuestaNotFoundEvent() throws InstanceNotFoundException,
			InputValidationException, InstanceAlreadyCreatedException,
			EventoStartedException, RepeatedOpcionApuestaException {

		TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		tipoApuesta.setOpcionesApuesta(opcionesApuesta);
		userService.addTipoApuesta(-1L, tipoApuesta);
	}

	/*
	 * PR-IN-018
	 */
	@Test(expected = InstanceAlreadyCreatedException.class)
	public void addRepeatedTipoApuesta() throws InstanceNotFoundException,
			InvalidDateException, InstanceAlreadyCreatedException,
			InstanceAlreadyCreatedException, EventoStartedException,
			RepeatedOpcionApuestaException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		tipoApuesta.setOpcionesApuesta(opcionesApuesta);
		userService.addTipoApuesta(addEvento.getIdEvento(), tipoApuesta);

		userService.addTipoApuesta(addEvento.getIdEvento(), tipoApuesta);
	}

	/*
	 * PR-IN-019
	 */
	@Test(expected = RepeatedOpcionApuestaException.class)
	public void addTipoApuestaRepeatedOpcion()
			throws InstanceNotFoundException, InvalidDateException,
			InstanceAlreadyCreatedException, InstanceAlreadyCreatedException,
			EventoStartedException, RepeatedOpcionApuestaException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("X", 1.2F, null);
		OpcionApuesta opcionApuesta3 = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		opcionesApuesta.add(opcionApuesta3);
		tipoApuesta.setOpcionesApuesta(opcionesApuesta);
		userService.addTipoApuesta(addEvento.getIdEvento(), tipoApuesta);
	}

	/*
	 * PR-IN-020
	 */

	@Test
	public void apostar() throws InstanceNotFoundException,
			EventoStartedException, InvalidDateException,
			InstanceAlreadyCreatedException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException, InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));
		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		float cantidadApostada = (float) nextDouble(0, 1000);

		ApuestaRealizada apuestaRealizada = userService.apostar(
				opcionApuesta.getIdOpcionApuesta(), cantidadApostada,
				userProfile.getUserProfileId());
		assertTrue(apuestaRealizada.getCantidadApostada() == cantidadApostada);
		assertEquals(apuestaRealizada.getOpcionApuesta(), opcionApuesta);
		assertEquals(apuestaRealizada.getUsuario(), userProfile);

	}

	/*
	 * PR-IN-021
	 */
	@Test(expected = InvalidValueException.class)
	public void apostarInvalidValue() throws InstanceNotFoundException,
			EventoStartedException, InvalidDateException,
			InstanceAlreadyCreatedException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException, InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));
		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		float cantidadApostada = (float) nextDouble(-10000, 0);

		userService.apostar(opcionApuesta.getIdOpcionApuesta(),
				cantidadApostada, userProfile.getUserProfileId());

	}

	/*
	 * PR-IN-022
	 */
	@Test(expected = EventoStartedException.class)
	public void apostarEventoStarted() throws InstanceNotFoundException,
			EventoStartedException, InvalidDateException,
			InstanceAlreadyCreatedException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException, InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));
		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		float cantidadApostada = (float) nextDouble(0, 10000);
		Calendar fechanueva = Calendar.getInstance();
		fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);

		addEvento.setFecha(fechanueva);
		eventoDao.save(addEvento);

		userService.apostar(opcionApuesta.getIdOpcionApuesta(),
				cantidadApostada, userProfile.getUserProfileId());

	}

	/*
	 * PR-IN-023
	 */
	@Test
	public void especificarGanadoras() throws EventoStartedException,
			EventoNotStartedException, OpcionApuestaAlreadySolvedException,
			InputValidationException, SimpleWinnerException,
			InstanceAlreadyCreatedException, InstanceNotFoundException,
			InvalidDateException, RepeatedOpcionApuestaException,
			InvalidOptionException, InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));

		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		userService.apostar(opcionApuesta.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());
		userService.apostar(opcionApuesta2.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());

		Calendar fechanueva = Calendar.getInstance();
		fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
		addEvento.setFecha(fechanueva);
		eventoDao.save(addEvento);

		List<Long> opcionesGanadoras = new ArrayList<Long>();
		opcionesGanadoras.add(opcionApuesta.getIdOpcionApuesta());
		userService.EspecificarGanadoras(opcionApuesta.getTipoApuesta()
				.getIdTipoApuesta(), opcionesGanadoras);

	}

	/*
	 * PR-IN-024
	 */
	@Test(expected = EventoNotStartedException.class)
	public void especificarGanadorasNotStartedEvento()
			throws EventoStartedException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, InputValidationException,
			SimpleWinnerException, InstanceAlreadyCreatedException,
			InstanceNotFoundException, InvalidDateException,
			RepeatedOpcionApuestaException, InvalidOptionException,
			InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));

		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		userService.apostar(opcionApuesta.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());
		userService.apostar(opcionApuesta2.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());

		List<Long> opcionesGanadoras = new ArrayList<Long>();
		opcionesGanadoras.add(opcionApuesta.getIdOpcionApuesta());
		userService.EspecificarGanadoras(opcionApuesta.getTipoApuesta()
				.getIdTipoApuesta(), opcionesGanadoras);

	}

	/*
	 * PR-IN-025
	 */
	@Test(expected = OpcionApuestaAlreadySolvedException.class)
	public void especificarGanadorasAlreadySolved()
			throws EventoStartedException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, InputValidationException,
			SimpleWinnerException, InstanceAlreadyCreatedException,
			InstanceNotFoundException, InvalidDateException,
			RepeatedOpcionApuestaException, InvalidOptionException,
			InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));

		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		userService.apostar(opcionApuesta.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());
		userService.apostar(opcionApuesta2.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());

		Calendar fechanueva = Calendar.getInstance();
		fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
		addEvento.setFecha(fechanueva);
		eventoDao.save(addEvento);

		List<Long> opcionesGanadoras = new ArrayList<Long>();
		opcionesGanadoras.add(opcionApuesta.getIdOpcionApuesta());
		userService.EspecificarGanadoras(opcionApuesta.getTipoApuesta()
				.getIdTipoApuesta(), opcionesGanadoras);
		userService.EspecificarGanadoras(opcionApuesta.getTipoApuesta()
				.getIdTipoApuesta(), opcionesGanadoras);

	}

	/*
	 * PR-IN-026
	 */
	@Test(expected = SimpleWinnerException.class)
	public void especificarGanadorasSimpleWinner()
			throws EventoStartedException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, InputValidationException,
			SimpleWinnerException, InstanceAlreadyCreatedException,
			InstanceNotFoundException, InvalidDateException,
			RepeatedOpcionApuestaException, InvalidOptionException,
			InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 1.2F, null);
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));

		String clearPassword = "userPassword";
		UserProfile userProfile = registerUser("user", clearPassword);
		userService.apostar(opcionApuesta.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());
		userService.apostar(opcionApuesta2.getIdOpcionApuesta(), 100,
				userProfile.getUserProfileId());

		Calendar fechanueva = Calendar.getInstance();
		fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
		addEvento.setFecha(fechanueva);
		eventoDao.save(addEvento);

		List<Long> opcionesGanadoras = new ArrayList<Long>();
		opcionesGanadoras.add(opcionApuesta.getIdOpcionApuesta());

		opcionesGanadoras.add(opcionApuesta2.getIdOpcionApuesta());
		userService.EspecificarGanadoras(opcionApuesta.getTipoApuesta()
				.getIdTipoApuesta(), opcionesGanadoras);
	}

	/*
	 * PR-IN-027
	 */
	@Test
	public void consultarApuestasExistsMore() throws EventoStartedException,
			InvalidDateException, InstanceAlreadyCreatedException,
			InstanceNotFoundException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException, InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta1 = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta1);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));

		String clearPassword1 = "userPassword";
		UserProfile userProfile1 = registerUser("user1", clearPassword1);

		ApuestaRealizada apuesta1 = userService.apostar(
				opcionApuesta1.getIdOpcionApuesta(), 100,
				userProfile1.getUserProfileId());
		ApuestaRealizada apuesta2 = userService.apostar(
				opcionApuesta1.getIdOpcionApuesta(), 100,
				userProfile1.getUserProfileId());
		userService.apostar(opcionApuesta1.getIdOpcionApuesta(), 100,
				userProfile1.getUserProfileId());

		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta1);
		apuestas.add(apuesta2);

		ApuestaBlock foundApuestas;
		foundApuestas = userService.consultarApuestas(
				userProfile1.getUserProfileId(), 0, 2);
		assertEquals(true, foundApuestas.getExistMoreApuestas());
		assertEquals(apuestas, foundApuestas.getApuestas());

	}

	/*
	 * PR-IN-028
	 */
	@Test
	public void consultarApuestasNotExistsMore() throws EventoStartedException,
			InvalidDateException, InstanceAlreadyCreatedException,
			InstanceNotFoundException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException, InvalidValueException {
		Categoria categoria1 = new Categoria("Futbol");
		categoriaDao.save(categoria1);
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("evento", fecha, categoria1);
		Evento addEvento = userService.addEvento(evento);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		OpcionApuesta opcionApuesta1 = new OpcionApuesta("1", 1.2F, null);
		opcionesApuesta.add(opcionApuesta1);
		userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
				"1X2", opcionesApuesta, false));

		String clearPassword1 = "userPassword";
		UserProfile userProfile1 = registerUser("user1", clearPassword1);

		ApuestaRealizada apuesta1 = userService.apostar(
				opcionApuesta1.getIdOpcionApuesta(), 100,
				userProfile1.getUserProfileId());
		ApuestaRealizada apuesta2 = userService.apostar(
				opcionApuesta1.getIdOpcionApuesta(), 100,
				userProfile1.getUserProfileId());

		List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
		apuestas.add(apuesta1);
		apuestas.add(apuesta2);

		ApuestaBlock foundApuestas;
		foundApuestas = userService.consultarApuestas(
				userProfile1.getUserProfileId(), 0, 2);
		assertEquals(false, foundApuestas.getExistMoreApuestas());
		assertEquals(apuestas, foundApuestas.getApuestas());

	}
}
