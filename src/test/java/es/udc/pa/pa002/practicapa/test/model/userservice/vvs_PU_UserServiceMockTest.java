package es.udc.pa.pa002.practicapa.test.model.userservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizadaDao;
import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.evento.EventoDao;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuestaDao;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuestaDao;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa002.practicapa.model.userservice.ApuestaBlock;
import es.udc.pa.pa002.practicapa.model.userservice.EventoBlock;
import es.udc.pa.pa002.practicapa.model.userservice.EventoNotStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.EventoStartedException;
import es.udc.pa.pa002.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa002.practicapa.model.userservice.InstanceAlreadyCreatedException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidDateException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidOptionException;
import es.udc.pa.pa002.practicapa.model.userservice.InvalidValueException;
import es.udc.pa.pa002.practicapa.model.userservice.OpcionApuestaAlreadySolvedException;
import es.udc.pa.pa002.practicapa.model.userservice.RepeatedOpcionApuestaException;
import es.udc.pa.pa002.practicapa.model.userservice.SimpleWinnerException;
import es.udc.pa.pa002.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa002.practicapa.model.userservice.UserServiceImpl;
import es.udc.pa.pa002.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class vvs_PU_UserServiceMockTest {

	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();

	@Mock
	private EventoDao eventoDaoMock;

	@Mock
	private TipoApuestaDao tipoApuestaDaoMock;

	@Mock
	private OpcionApuestaDao opcionApuestaDaoMock;

	@Mock
	private ApuestaRealizadaDao apuestaRealizadaDaoMock;

	@Mock
	private UserProfileDao userProfileDaoMock;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/*
	 * PR-UN-066
	 * 
	 * UserService
	 * 
	 * registerUser Aplicacion de particiones equivalentes -loginName del
	 * usuario -contraseña -UserProfilDetails del usuario UserProfile con los
	 * datos de registro Base de datos arrancada
	 */
	@Test
	public void registerUser() throws DuplicateInstanceException,
			InstanceNotFoundException {
		when(userProfileDaoMock.findByLoginName("user")).thenThrow(
				new InstanceNotFoundException("error", null));
		userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

	}

	/*
	 * PR-UN-067
	 * 
	 * UserService
	 * 
	 * registerUser Aplicacion de particiones equivalentes -loginName del
	 * usuario -contraseña -UserProfileDetails del usuario
	 * Excepcion:"DuplicateInstanceException" Base de datos arrancada con un
	 * userProfile coincidente con los datos de registro
	 */
	@Test
	public void registerRepeatedUser() throws DuplicateInstanceException {
		thrown.expect(DuplicateInstanceException.class);
		userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

	}

	/*
	 * PR-UN-068
	 * 
	 * UserService
	 * 
	 * login Aplicacion de particiones equivalentes -loginName del usuario
	 * -password en claro del usuario -false UserProfile del usuario autenticado
	 * Base de datos arrancada con un userProfile coincidente con los datos de
	 * login
	 */
	@Test
	public void loginWithPassword() throws InstanceNotFoundException,
			IncorrectPasswordException {
		UserProfile user = new UserProfile("name",
				PasswordEncrypter.crypt("pass"), "firstName", "lastName",
				"email");
		when(userProfileDaoMock.findByLoginName("name")).thenReturn(user);
		assertEquals(userService.login(user.getLoginName(), "pass", false),
				user);

	}

	/*
	 * PR-UN-069
	 * 
	 * UserService
	 * 
	 * login Aplicacion de particiones equivalentes -loginName del usuario
	 * -password encriptada del usuario -true UserProfile del usuario
	 * autenticado Base de datos arrancada con un userProfile coincidente con
	 * los datos de login
	 */
	@Test
	public void loginWithEncryptedPassword() throws InstanceNotFoundException,
			IncorrectPasswordException {
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		when(userProfileDaoMock.findByLoginName("name")).thenReturn(user);
		assertEquals(userService.login(user.getLoginName(), encPass, true),
				user);

	}

	/*
	 * PR-UN-070
	 * 
	 * UserService
	 * 
	 * login Aplicacion de particiones equivalentes -loginName de usuario no
	 * existente -password -true Excepcion:"InstanceNotFoundException" Base de
	 * datos arrancada
	 */
	@Test
	public void loginUserNotFound() throws InstanceNotFoundException,
			IncorrectPasswordException {
		thrown.expect(InstanceNotFoundException.class);
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		when(userProfileDaoMock.findByLoginName("name")).thenThrow(
				new InstanceNotFoundException("error", null));
		userService.login(user.getLoginName(), encPass, true);

	}

	/*
	 * PR-UN-072
	 * 
	 * UserService
	 * 
	 * login Aplicacion de particiones equivalentes -loginName del usuario
	 * existente -password encriptada incorrecta -true
	 * Excepcion:"IncorrectPasswordException" Base de datos arrancada
	 */
	@Test
	public void loginIncorrectPassword() throws InstanceNotFoundException,
			IncorrectPasswordException {
		thrown.expect(IncorrectPasswordException.class);
		thrown.expectMessage("name");
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		when(userProfileDaoMock.findByLoginName("name")).thenReturn(user);
		userService.login(user.getLoginName(), "passs", false);

	}

	/*
	 * PR-UN-073
	 * 
	 * UserService
	 * 
	 * login Aplicacion de particiones equivalentes -loginName del usuario
	 * existente -password en claro incorrecta -false
	 * Excepcion:"IncorrectPasswordException" Base de datos arrancada
	 */
	@Test
	public void loginIncorrectEncryptedPassword()
			throws InstanceNotFoundException, IncorrectPasswordException {
		thrown.expect(IncorrectPasswordException.class);
		thrown.expectMessage("name");
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		when(userProfileDaoMock.findByLoginName("name")).thenReturn(user);
		userService.login(user.getLoginName(),
				PasswordEncrypter.crypt("passsss"), true);

	}

	/*
	 * PR-UN-076
	 * 
	 * UserService
	 * 
	 * findUserProfile Comprobacion busqueda de un usuario por su userProfileId
	 * userProfileId de un usuario existente UserProfile correspondiente al
	 * userProfileId Base de datos arrancada con un userProfile insertado
	 */
	@Test
	public void findUserProfile() throws InstanceNotFoundException {
		UserProfile user = new UserProfile("name", "pass", "firstName",
				"lastName", "email");

		when(userProfileDaoMock.find(1L)).thenReturn(user);
		assertEquals(userService.findUserProfile(1L), user);

	}

	/*
	 * PR-UN-077
	 * 
	 * UserService
	 * 
	 * findUserProfile Comprobacion busqueda de un usuario inexistente por su
	 * userProfileId userProfileId inexistente en la base de datos
	 * Excepcion:"InstanceNotFoundException" Base de datos arrancada
	 */
	@Test
	public void findUserProfileNotFound() throws InstanceNotFoundException {
		thrown.expect(InstanceNotFoundException.class);

		when(userProfileDaoMock.find(1L)).thenThrow(
				new InstanceNotFoundException("error", null));
		userService.findUserProfile(1L);

	}

	/*
	 * PR-UN-078
	 * 
	 * UserService
	 * 
	 * updateUserProfileDetails Comprobacion actualización de un
	 * UserProfileDetails existente -userProfileId del usuario -UserDetails No
	 * salida Base de datos arrancada con un userProfile insertado
	 */
	@Test
	public void updateUserProfile() throws InstanceNotFoundException {
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");
		UserProfileDetails userDetails = new UserProfileDetails("newFirstName",
				"newLastname", "NewEmail");

		when(userProfileDaoMock.find(1L)).thenReturn(user);
		userService.updateUserProfileDetails(1L, userDetails);
		assertEquals(user.getFirstName(), userDetails.getFirstName());
		assertEquals(user.getLastName(), userDetails.getLastName());
		assertEquals(user.getEmail(), userDetails.getEmail());

	}

	/*
	 * PR-UN-079
	 * 
	 * UserService
	 * 
	 * updateUserProfileDetails Comprobacion actualización de un
	 * UserProfileDetails inexistente -userProfileId inexistente
	 * -UserProfileDetails detalles del ususario
	 * Excepcion:"InstanceNotFoundException" Base de datos arrancada
	 */
	@Test
	public void updateUserProfileNotFound() throws InstanceNotFoundException {
		thrown.expect(InstanceNotFoundException.class);

		when(userProfileDaoMock.find(1L)).thenThrow(
				new InstanceNotFoundException("error", null));

		userService.updateUserProfileDetails(1L, null);

	}

	/*
	 * PR-UN-080
	 * 
	 * UserService
	 * 
	 * changePassword Comprobacion cambio de contraseña -userProfileId del
	 * usuario -oldClearPassword contraseña antigua del usuario
	 * -newClearPassword nueva contraseña del usuario No salida Base de datos
	 * arrancada con un userProfie
	 */
	@Test
	public void changePassword() throws InstanceNotFoundException,
			IncorrectPasswordException {
		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(userProfileDaoMock.find(1L)).thenReturn(user);

		userService.changePassword(1L, "pass", "newPassword");
		assertTrue(PasswordEncrypter.isClearPasswordCorrect("newPassword",
				user.getEncryptedPassword()));

	}

	/*
	 * PR-UN-081
	 * 
	 * UserService
	 * 
	 * changePassword Aplicacion de particiones equivalentes en la entrada del
	 * metodo -userProfileId inexistente del usuario -oldClearPassword
	 * contraseña antigua del usuario -newClearPassword nueva contraseña del
	 * usuario Excepcion:"InstanceNotFoundException" Base de datos arrancada con
	 * un userProfile insertado
	 */
	@Test
	public void changePasswordNotFound() throws InstanceNotFoundException,
			IncorrectPasswordException {
		thrown.expect(InstanceNotFoundException.class);

		when(userProfileDaoMock.find(1L)).thenThrow(
				new InstanceNotFoundException("error", null));

		userService.changePassword(1L, "pass", "newPassword");

	}

	/*
	 * PR-UN-082
	 * 
	 * UserService
	 * 
	 * changePassword Aplicacion de particiones equivalentes en la entrada del
	 * metodo -userProfileId del usuario -oldClearPassword contraseña antigua
	 * del usuario -newClearPassword nueva contraseña invalida del usuario
	 * Excepcion:"IncorrectPasswordException" Base de datos arrancada con un
	 * userProfie
	 */
	@Test
	public void changePasswordIncorrectOld() throws InstanceNotFoundException,
			IncorrectPasswordException {
		thrown.expect(IncorrectPasswordException.class);

		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(userProfileDaoMock.find(1L)).thenReturn(user);

		userService.changePassword(1L, "incorrectPassword", "newPassword");

	}

	/*
	 * PR-UN-084
	 * 
	 * UserService
	 * 
	 * addEvento Aplicacion de particiones equivalentes en la entrada del metodo
	 * -Evento sin id con fecha posterior al instante actual No error Base de
	 * datos arrancada con una categoria insertada
	 */
	@Test
	public void addEvento() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		userService.addEvento(evento);
	}

	/*
	 * PR-UN-085
	 * 
	 * UserService
	 * 
	 * addEvento Aplicacion de particiones equivalentes en la entrada del metodo
	 * -Evento sin id con fecha anterior al instante actual
	 * Excepcion:InvalidDateException Base de datos arrancada con una categoria
	 * insertada
	 */
	@Test
	public void addPastEvento() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		thrown.expect(InvalidDateException.class);
		thrown.expectMessage("La fecha introducida no es correcta");
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		userService.addEvento(evento);
	}

	/*
	 * PR-UN-086
	 * 
	 * UserService
	 * 
	 * addEvento Aplicacion de particiones equivalentes en la entrada del metodo
	 * -Evento sin id con fecha, nombre y categoria coincidentes con un Evento
	 * en base de datos Excepcion:InstanceAlreadyCreatedException Base de datos
	 * arrancada con una categoria y un evento insertado
	 */
	@Test
	public void addRepeatedEvento() throws InvalidDateException,
			InstanceAlreadyCreatedException {
		thrown.expect(InstanceAlreadyCreatedException.class);
		thrown.expectMessage("El evento ya ha sido creado");
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);

		Evento evento = new Evento("nombre", fecha, categoria);
		when(
				eventoDaoMock.existsEvent(evento.getNombre(), evento
						.getCategoria().getIdCategoria(), evento.getFecha()))
				.thenReturn(true);

		userService.addEvento(evento);
	}

	/*
	 * PR-UN-087
	 * 
	 * UserService
	 * 
	 * findEventos Aplicacion de particiones equivalentes en la entrada del
	 * metodo -null -null -true -0 -10 EventoBlock (lista de 10 Eventos,TRUE)
	 * Base de datos arrancada con una categoria y al menos 11 Eventos
	 * insertados
	 */
	@Test
	public void findEventos() {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		List<Evento> listaEventos = new ArrayList<Evento>();
		Evento evento = new Evento("nombre", fecha, categoria);
		Evento evento2 = new Evento("nombre", fecha, categoria);
		Evento evento3 = new Evento("nombre", fecha, categoria);
		listaEventos.add(evento);
		listaEventos.add(evento2);
		listaEventos.add(evento3);

		when(eventoDaoMock.findByParameters(null, null, true, 0, 3))
				.thenReturn(listaEventos);

		EventoBlock eventoBlock = userService.findEventos(null, null, true, 0,
				2);
		assertTrue(eventoBlock.getExistMoreEventos());
		assertEquals(eventoBlock.getEventos().size(), 2);
	}

	/*
	 * PR-UN-088
	 * 
	 * UserService
	 * 
	 * findEventos Aplicacion de particiones equivalentes en la entrada del
	 * metodo -null -null -true -10 -10 EventoBlock (lista de 2 Eventos,FALSE)
	 * Base de datos arrancada con una categoria y 12 Eventos insertados
	 */
	@Test
	public void findEventosNotExistsMore() {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		List<Evento> listaEventos = new ArrayList<Evento>();
		Evento evento = new Evento("nombre", fecha, categoria);
		Evento evento2 = new Evento("nombre", fecha, categoria);
		listaEventos.add(evento);
		listaEventos.add(evento2);

		when(eventoDaoMock.findByParameters(null, null, true, 0, 3))
				.thenReturn(listaEventos);

		EventoBlock eventoBlock = userService.findEventos(null, null, true, 0,
				2);
		assertFalse(eventoBlock.getExistMoreEventos());
		assertEquals(eventoBlock.getEventos().size(), 2);
	}

	/*
	 * PR-UN-089
	 * 
	 * UserService
	 * 
	 * findEventos Aplicacion de particiones equivalentes en la entrada del
	 * metodo -null -null -true -0 -10 EventoBlock (lista vacia,FALSE) Base de
	 * datos arrancada
	 */
	@Test
	public void findEventosNotEventos() {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		List<Evento> listaEventos = new ArrayList<Evento>();
		Evento evento = new Evento("nombre", fecha, categoria);
		Evento evento2 = new Evento("nombre", fecha, categoria);
		listaEventos.add(evento);
		listaEventos.add(evento2);

		when(eventoDaoMock.findByParameters("notexists", null, true, 0, 3))
				.thenReturn(listaEventos);

		EventoBlock eventoBlock = userService.findEventos(null, null, true, 0,
				2);
		assertFalse(eventoBlock.getExistMoreEventos());
		assertTrue(eventoBlock.getEventos().isEmpty());
	}

	/*
	 * PR-UN-090
	 * 
	 * UserService
	 * 
	 * addTipoApuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idEvento de base de datos con fecha posterior al instante actual
	 * -TipoApuesta sin id cuya pregunta no este en el Evento y con una lista de
	 * OpcionApuesta sin id sin repetir. Mismo TipoApuesta con id y la lista de
	 * OpcionApuesta con ids Base de datos arrancada con una categoria y evento
	 * insertados
	 */
	@Test
	public void addTipoApuesta() throws InstanceNotFoundException,
			EventoStartedException, InstanceAlreadyCreatedException,
			RepeatedOpcionApuestaException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		when(eventoDaoMock.find(1L)).thenReturn(evento);

		userService.addTipoApuesta(1L, tipoApuesta);

	}

	/*
	 * PR-UN-091
	 * 
	 * UserService
	 * 
	 * addTipoApuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idEvento de base de datos con fecha anterior al instante actual
	 * -TipoApuesta sin id cuya pregunta no este en el Evento y con una lista de
	 * OpcionApuesta sin id sin repetir. Excepcion:EventoStartedException Base
	 * de datos arrancada con una categoria y evento insertados
	 */
	public void addTipoApuestaEventoNotFound()
			throws InstanceNotFoundException, EventoStartedException,
			InstanceAlreadyCreatedException, RepeatedOpcionApuestaException {
		thrown.expect(InstanceNotFoundException.class);
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		when(eventoDaoMock.find(1L)).thenThrow(
				new InstanceNotFoundException("error", null));

		userService.addTipoApuesta(1L, tipoApuesta);

	}

	/*
	 * PR-UN-093
	 * 
	 * UserService
	 * 
	 * addTipoApuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idEvento de base de datos con fecha posterior al instante actual
	 * -TipoApuesta sin id cuya pregunta coincida con la de otro TipoApuesta en
	 * el Evento Excepcion:InstanceAlreadyCreatedException Base de datos
	 * arrancada con una categoria, un evento y un tipoApuesta insertados
	 */
	@Test
	public void addTipoApuestaRepeatedTipoApuesta()
			throws InstanceNotFoundException, EventoStartedException,
			InstanceAlreadyCreatedException, RepeatedOpcionApuestaException {
		thrown.expect(InstanceAlreadyCreatedException.class);
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		when(eventoDaoMock.find(1L)).thenReturn(evento);

		userService.addTipoApuesta(1L, tipoApuesta);

		userService.addTipoApuesta(1L, tipoApuesta);

	}

	/*
	 * PR-UN-094
	 * 
	 * UserService
	 * 
	 * addTipoApuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idEvento de base de datos con fecha posterior al instante actual
	 * -TipoApuesta sin id cuya pregunta no este en el Evento y con una lista de
	 * OpcionApuesta en la que dos opcionesApuesta coincidan en su respuesta
	 * Excepcion:RepeatedOpcionApuestaException Base de datos arrancada con una
	 * categoria y un evento insertados
	 */
	@Test
	public void addTipoApuestaRepeatedOpcionApuesta()
			throws InstanceNotFoundException, EventoStartedException,
			InstanceAlreadyCreatedException, RepeatedOpcionApuestaException {
		thrown.expect(RepeatedOpcionApuestaException.class);
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 2, null);
		OpcionApuesta opcionApuesta3 = new OpcionApuesta("1", 2, null);
		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		opcionesApuesta.add(opcionApuesta3);
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		when(eventoDaoMock.find(1L)).thenReturn(evento);

		userService.addTipoApuesta(1L, tipoApuesta);

	}

	/*
	 * PR-UN-095
	 * 
	 * UserService
	 * 
	 * apostar Aplicacion de particiones equivalentes en la entrada del metodo
	 * -idOpcionApuesta de base de datos -idUsuario de base datos -cantidad a
	 * apostar ApuestaRealizada(con el usuario correspondiente al idUsuario,con
	 * la OpcionApuesta correspondiente al idOpcionApuesta y la cantidad de
	 * entrada) Base de datos arrancada con una categoria, un evento, un
	 * TipoApuesta, una OpcionApuesta y un UserProfile insertados
	 */
	@Test
	public void apostar() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		when(userProfileDaoMock.find(1L)).thenReturn(user);
		ApuestaRealizada apuesta = userService.apostar(1L, 5, 1L);

		assertEquals(apuesta.getUsuario(), user);
		assertTrue(apuesta.getCantidadApostada() == 5);
		assertEquals(apuesta.getOpcionApuesta(), opcionApuesta);

	}

	/*
	 * PR-UN-096
	 * 
	 * UserService
	 * 
	 * apostar Aplicacion de particiones equivalentes en la entrada del metodo
	 * -idOpcionApuesta de base de datos -idUsuario de base datos -cantidad a
	 * apostar negativa Excepcion:InvalidValueException Base de datos arrancada
	 */
	@Test(expected = InvalidValueException.class)
	public void apostarFailTest() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		when(userProfileDaoMock.find(1L)).thenReturn(user);
		ApuestaRealizada apuesta = userService.apostar(1L, -5, 1L);

	}

	/*
	 * PR-UN-097
	 * 
	 * UserService
	 * 
	 * apostar Aplicacion de particiones equivalentes en la entrada del metodo
	 * -idOpcionApuesta de base de datos -idUsuario de base datos -cantidad a
	 * apostar Excepcion:EventoStartedException Base de datos arrancada con una
	 * categoria, un evento(con fecha anterior al instante actual), un
	 * TipoApuesta y una OpcionApuesta insertados
	 */
	@Test
	public void apostarStartedEvent() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		thrown.expect(EventoStartedException.class);

		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		userService.apostar(1L, 5, 1L);

	}

	/*
	 * PR-UN-098
	 * 
	 * UserService
	 * 
	 * apostar Aplicacion de particiones equivalentes en la entrada del metodo
	 * -idOpcionApuesta de base de datos -idUsuario -cantidad a apostar
	 * Excepcion:InstanceNotFoundException Base de datos arrancada con una
	 * categoria, un evento(con fecha anterior al instante actual), un
	 * TipoApuesta y una OpcionApuesta insertados
	 */
	@Test
	public void apostarOpcionNotFound() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		thrown.expect(InstanceNotFoundException.class);

		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2014, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(opcionApuestaDaoMock.find(1L)).thenThrow(
				new InstanceNotFoundException("error", null));
		userService.apostar(1L, 5, 1L);

	}

	/*
	 * PR-UN-099
	 * 
	 * UserService
	 * 
	 * apostar Aplicacion de particiones equivalentes en la entrada del metodo
	 * -idOpcionApuesta -idUsuario de base datos -cantidad a apostar
	 * Excepcion:InstanceNotFoundException Base de datos arrancada
	 */
	@Test
	public void apostarUserNotFound() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		thrown.expect(InstanceNotFoundException.class);

		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		when(userProfileDaoMock.find(1L)).thenThrow(
				new InstanceNotFoundException("error", null));
		userService.apostar(1L, 5, 1L);

	}

	/*
	 * PR-UN-100
	 * 
	 * UserService
	 * 
	 * especificarganadoras Aplicacion de particiones equivalentes en la entrada
	 * del metodo -idTipoApuesta -Lista de idOpcionApuesta de dicho TipoApuesta
	 * No salida Base de datos arrancada con una categoria, un evento(con fecha
	 * anterior al instante actual), un TipoApuesta y una OpcionApuesta
	 * insertados
	 */
	@Test
	public void especificarGanadoras() throws InstanceNotFoundException,
			EventoNotStartedException, OpcionApuestaAlreadySolvedException,
			SimpleWinnerException, InvalidOptionException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2012, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setIdOpcionApuesta(1L);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 20, null);
		opcionApuesta2.setIdOpcionApuesta(2L);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);

		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(tipoApuestaDaoMock.find(1L)).thenReturn(tipoApuesta);

		List<Long> ganadoras = new ArrayList<Long>();
		ganadoras.add(opcionApuesta.getIdOpcionApuesta());
		userService.EspecificarGanadoras(1L, ganadoras);

		assertTrue(opcionApuesta.getEstado());
		assertFalse(opcionApuesta2.getEstado());
	}

	/*
	 * PR-UN-101
	 * 
	 * UserService
	 * 
	 * especificarganadoras Aplicacion de particiones equivalentes en la entrada
	 * del metodo -idTipoApuesta -Lista de idOpcionApuesta de dicho TipoApuesta
	 * Excepcion:EventoNotStartedException Base de datos arrancada con una
	 * categoria, un evento(con fecha posterior al instante actual), un
	 * TipoApuesta y una OpcionApuesta insertados
	 */
	@Test
	public void especificarGanadorasEventoNotStarted()
			throws InstanceNotFoundException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, SimpleWinnerException,
			InvalidOptionException {
		thrown.expect(EventoNotStartedException.class);

		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setIdOpcionApuesta(1L);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 20, null);
		opcionApuesta2.setIdOpcionApuesta(2L);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);

		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(tipoApuestaDaoMock.find(1L)).thenReturn(tipoApuesta);

		List<Long> ganadoras = new ArrayList<Long>();
		userService.EspecificarGanadoras(1L, ganadoras);
	}

	/*
	 * PR-UN-103
	 * 
	 * UserService
	 * 
	 * especificarganadoras Aplicacion de particiones equivalentes en la entrada
	 * del metodo -idTipoApuesta con las opcionesApuesta resueltas -Lista de
	 * idOpcionApuesta de dicho TipoApuesta
	 * Excepcion:OpcionApuestaAlreadySolvedException Base de datos arrancada con
	 * una categoria, un evento(con fecha posterior al instante actual), un
	 * TipoApuesta y una/s OpcionApuesta(resueltas) insertados
	 */
	@Test
	public void especificarGanadorasAlreadySolved()
			throws InstanceNotFoundException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, SimpleWinnerException,
			InvalidOptionException {
		thrown.expect(OpcionApuestaAlreadySolvedException.class);
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2012, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setIdOpcionApuesta(1L);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 20, null);
		opcionApuesta2.setIdOpcionApuesta(2L);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);

		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(tipoApuestaDaoMock.find(1L)).thenReturn(tipoApuesta);

		List<Long> ganadoras = new ArrayList<Long>();
		ganadoras.add(opcionApuesta.getIdOpcionApuesta());
		userService.EspecificarGanadoras(1L, ganadoras);

		userService.EspecificarGanadoras(1L, ganadoras);

	}

	/*
	 * 
	 * PR-UN-104
	 * 
	 * UserService
	 * 
	 * especificarganadoras Aplicacion de particiones equivalentes en la entrada
	 * del metodo -idTipoApuesta de TipoApuesta(simple ganadora) con las
	 * opcionesApuesta -Lista con 1 o más idOpcionApuesta de dicho TipoApuesta
	 * Excepcion:SimpleWinnerException Base de datos arrancada con una
	 * categoria, un evento(con fecha posterior al instante actual), un
	 * TipoApuesta(con simple ganadora) y unas OpcionApuesta insertados
	 */
	@Test
	public void especificarGanadorasSimpleWinnerException()
			throws InstanceNotFoundException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, SimpleWinnerException,
			InvalidOptionException {
		thrown.expect(SimpleWinnerException.class);

		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2012, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setIdOpcionApuesta(1L);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 20, null);
		opcionApuesta2.setIdOpcionApuesta(2L);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		opcionesApuesta.add(opcionApuesta);
		opcionesApuesta.add(opcionApuesta2);
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);

		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(tipoApuestaDaoMock.find(1L)).thenReturn(tipoApuesta);

		List<Long> ganadoras = new ArrayList<Long>();
		ganadoras.add(opcionApuesta.getIdOpcionApuesta());
		ganadoras.add(opcionApuesta2.getIdOpcionApuesta());

		userService.EspecificarGanadoras(1L, ganadoras);

	}

	/*
	 * PR-UN-105
	 * 
	 * UserService
	 * 
	 * especificarganadoras Aplicacion de particiones equivalentes en la entrada
	 * del metodo -idTipoApuesta -Lista con varios idOpcionApuesta en la que uno
	 * no pertenece a dicho TipoApuesta Excepcion:InvalidOptionException Base de
	 * datos arrancada con una categoria, un evento(con fecha posterior al
	 * instante actual), un TipoApuesta y unas OpcionApuesta insertados
	 */
	@Test
	public void especificarGanadorasInvalidOptionException()
			throws InstanceNotFoundException, EventoNotStartedException,
			OpcionApuestaAlreadySolvedException, SimpleWinnerException,
			InvalidOptionException {
		thrown.expect(InvalidOptionException.class);

		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2012, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setIdOpcionApuesta(1L);
		OpcionApuesta opcionApuesta2 = new OpcionApuesta("2", 20, null);
		opcionApuesta2.setIdOpcionApuesta(2L);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		opcionesApuesta.add(opcionApuesta);
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);

		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		when(tipoApuestaDaoMock.find(1L)).thenReturn(tipoApuesta);

		List<Long> ganadoras = new ArrayList<Long>();
		ganadoras.add(opcionApuesta2.getIdOpcionApuesta());

		userService.EspecificarGanadoras(1L, ganadoras);

	}

	/*
	 * PR-UN-106
	 * 
	 * UserService
	 * 
	 * consultarapuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idUsuario -0 -10 ApuestaBlock (lista de 10
	 * ApuestaRealizadas,TRUE) Base de datos arrancada con una categoria, un
	 * Evento(con fecha anterior al instante actual), un TipoApuesta, unas
	 * OpcionApuesta, un UserProfile y 11 ApuestasRealizadas insertados
	 */
	@Test
	public void consultarapuesta() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		when(userProfileDaoMock.find(1L)).thenReturn(user);
		ApuestaRealizada apuesta1 = userService.apostar(1L, 1, 1L);
		ApuestaRealizada apuesta2 = userService.apostar(1L, 2, 1L);
		ApuestaRealizada apuesta3 = userService.apostar(1L, 3, 1L);
		ApuestaRealizada apuesta4 = userService.apostar(1L, 4, 1L);
		ApuestaRealizada apuesta5 = userService.apostar(1L, 5, 1L);
		ApuestaRealizada apuesta6 = userService.apostar(1L, 6, 1L);
		ApuestaRealizada apuesta7 = userService.apostar(1L, 7, 1L);
		ApuestaRealizada apuesta8 = userService.apostar(1L, 8, 1L);
		ApuestaRealizada apuesta9 = userService.apostar(1L, 9, 1L);
		ApuestaRealizada apuesta10 = userService.apostar(1L, 10, 1L);
		ApuestaRealizada apuesta11 = userService.apostar(1L, 11, 1L);

		List<ApuestaRealizada> apuestas = new ArrayList();
		apuestas.add(apuesta1);
		apuestas.add(apuesta2);
		apuestas.add(apuesta3);
		apuestas.add(apuesta4);
		apuestas.add(apuesta5);
		apuestas.add(apuesta6);
		apuestas.add(apuesta7);
		apuestas.add(apuesta8);
		apuestas.add(apuesta9);
		apuestas.add(apuesta10);
		apuestas.add(apuesta11);

		when(apuestaRealizadaDaoMock.findByUser(1L, 0, 11))
				.thenReturn(apuestas);
		ApuestaBlock apuestasList = userService.consultarApuestas(1L, 0, 10);
		assertTrue(apuestasList.getApuestas().size() == 10);
		assertTrue(apuestasList.getExistMoreApuestas());

	}

	/*
	 * PR-UN-107
	 * 
	 * UserService
	 * 
	 * consultarapuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idUsuario -10 -10 ApuestaBlock (lista de 2
	 * ApuestasRealizadas,FALSE) Base de datos arrancada con una categoria, un
	 * Evento(con fecha anterior al instante actual), un TipoApuesta, unas
	 * OpcionApuesta, un UserProfile y 12 ApuestasRealizadas insertados
	 */
	@Test
	public void consultarapuesta2() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		when(userProfileDaoMock.find(1L)).thenReturn(user);

		ApuestaRealizada apuesta11 = userService.apostar(1L, 11, 1L);
		ApuestaRealizada apuesta12 = userService.apostar(1L, 12, 1L);

		List<ApuestaRealizada> apuestas = new ArrayList();
		apuestas.add(apuesta11);
		apuestas.add(apuesta12);

		when(apuestaRealizadaDaoMock.findByUser(1L, 10, 11)).thenReturn(
				apuestas);
		ApuestaBlock apuestasList = userService.consultarApuestas(1L, 10, 10);
		assertTrue(apuestasList.getApuestas().size() == 2);
		assertFalse(apuestasList.getExistMoreApuestas());

	}

	/*
	 * PR-UN-108
	 * 
	 * UserService
	 * 
	 * consultarapuesta Aplicacion de particiones equivalentes en la entrada del
	 * metodo -idUsuario -0 -10 ApuestaBlock (lista de 2
	 * ApuestasRealizadas,FALSE) Base de datos arrancada con una categoria, un
	 * Evento(con fecha anterior al instante actual), un TipoApuesta, unas
	 * OpcionApuesta, un UserProfile y 2 ApuestasRealizadas insertados
	 */
	@Test
	public void consultarapuesta3() throws InstanceNotFoundException,
			EventoStartedException, InvalidValueException {
		Categoria categoria = new Categoria("Futbol");
		Calendar fecha = Calendar.getInstance();
		fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
		Evento evento = new Evento("nombre", fecha, categoria);

		Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
		TipoApuesta tipoApuesta = new TipoApuesta(evento, "pregunta",
				opcionesApuesta, false);
		OpcionApuesta opcionApuesta = new OpcionApuesta("1", 2, null);
		opcionApuesta.setTipoApuesta(tipoApuesta);
		tipoApuesta.setEvento(evento);

		String encPass = PasswordEncrypter.crypt("pass");
		UserProfile user = new UserProfile("name", encPass, "firstName",
				"lastName", "email");

		when(opcionApuestaDaoMock.find(1L)).thenReturn(opcionApuesta);
		when(userProfileDaoMock.find(1L)).thenReturn(user);
		ApuestaRealizada apuesta1 = userService.apostar(1L, 1, 1L);
		ApuestaRealizada apuesta2 = userService.apostar(1L, 2, 1L);

		List<ApuestaRealizada> apuestas = new ArrayList();
		apuestas.add(apuesta1);
		apuestas.add(apuesta2);

		when(apuestaRealizadaDaoMock.findByUser(1L, 0, 11))
				.thenReturn(apuestas);
		ApuestaBlock apuestasList = userService.consultarApuestas(1L, 0, 10);
		assertTrue(apuestasList.getApuestas().size() == 2);
		assertFalse(apuestasList.getExistMoreApuestas());

	}

}
