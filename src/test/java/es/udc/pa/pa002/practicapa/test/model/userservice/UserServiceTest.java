package es.udc.pa.pa002.practicapa.test.model.userservice;

import static es.udc.pa.pa002.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa002.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
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
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserServiceTest {

private final long NON_EXISTENT_USER_PROFILE_ID = -1;
private final long NON_EXISTENT_ID = -1;

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

@Test
public void testRegisterUserAndFindUserProfile()
        throws DuplicateInstanceException, InstanceNotFoundException {

    /* Register user and find profile. */
    UserProfile userProfile = userService.registerUser("user", "userPassword",
            new UserProfileDetails("name", "lastName", "user@udc.es"));

    UserProfile userProfile2 = userService.findUserProfile(userProfile
            .getUserProfileId());

    /* Check data. */
    assertEquals(userProfile, userProfile2);

}

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

@Test
public void testLoginClearPassword() throws IncorrectPasswordException,
        InstanceNotFoundException {

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    UserProfile userProfile2 = userService.login(userProfile.getLoginName(),
            clearPassword, false);

    assertEquals(userProfile, userProfile2);

}

@Test
public void testLoginEncryptedPassword() throws IncorrectPasswordException,
        InstanceNotFoundException {

    UserProfile userProfile = registerUser("user", "clearPassword");

    UserProfile userProfile2 = userService.login(userProfile.getLoginName(),
            userProfile.getEncryptedPassword(), true);

    assertEquals(userProfile, userProfile2);

}

@Test(expected = IncorrectPasswordException.class)
public void testLoginIncorrectPasword() throws IncorrectPasswordException,
        InstanceNotFoundException {

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    userService.login(userProfile.getLoginName(), 'X' + clearPassword, false);

}

@Test(expected = InstanceNotFoundException.class)
public void testLoginWithNonExistentUser() throws IncorrectPasswordException,
        InstanceNotFoundException {

    userService.login("user", "userPassword", false);

}

@Test(expected = InstanceNotFoundException.class)
public void testFindNonExistentUser() throws InstanceNotFoundException {

    userService.findUserProfile(NON_EXISTENT_USER_PROFILE_ID);

}

@Test
public void testUpdate() throws InstanceNotFoundException,
        IncorrectPasswordException {

    /* Update profile. */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    UserProfileDetails newUserProfileDetails = new UserProfileDetails(
            'X' + userProfile.getFirstName(), 'X' + userProfile.getLastName(),
            'X' + userProfile.getEmail());

    userService.updateUserProfileDetails(userProfile.getUserProfileId(),
            newUserProfileDetails);

    /* Check changes. */
    userService.login(userProfile.getLoginName(), clearPassword, false);
    UserProfile userProfile2 = userService.findUserProfile(userProfile
            .getUserProfileId());

    assertEquals(newUserProfileDetails.getFirstName(),
            userProfile2.getFirstName());
    assertEquals(newUserProfileDetails.getLastName(),
            userProfile2.getLastName());
    assertEquals(newUserProfileDetails.getEmail(), userProfile2.getEmail());

}

@Test(expected = InstanceNotFoundException.class)
public void testUpdateWithNonExistentUser() throws InstanceNotFoundException {

    userService.updateUserProfileDetails(NON_EXISTENT_USER_PROFILE_ID,
            new UserProfileDetails("name", "lastName", "user@udc.es"));

}

@Test
public void testChangePassword() throws InstanceNotFoundException,
        IncorrectPasswordException {

    /* Change password. */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    String newClearPassword = 'X' + clearPassword;

    userService.changePassword(userProfile.getUserProfileId(), clearPassword,
            newClearPassword);

    /* Check new password. */
    userService.login(userProfile.getLoginName(), newClearPassword, false);

}

@Test(expected = IncorrectPasswordException.class)
public void testChangePasswordWithIncorrectPassword()
        throws InstanceNotFoundException, IncorrectPasswordException {

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    userService.changePassword(userProfile.getUserProfileId(),
            'X' + clearPassword, 'Y' + clearPassword);

}

@Test(expected = InstanceNotFoundException.class)
public void testChangePasswordWithNonExistentUser()
        throws InstanceNotFoundException, IncorrectPasswordException {

    userService.changePassword(NON_EXISTENT_USER_PROFILE_ID, "userPassword",
            "XuserPassword");

}

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

@Test
public void testAddEventoAndFindEventoById() throws InstanceNotFoundException,
        InvalidDateException, InstanceAlreadyCreatedException {
    Categoria categoria = new Categoria("Futbol");
    categoriaDao.save(categoria);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("nombre", fecha, categoria);
    Evento addedEvento = userService.addEvento(evento);
    Evento foundEvento = userService.findEventoById(addedEvento.getIdEvento());
    assertEquals(addedEvento, foundEvento);
}

@Test(expected = InvalidDateException.class)
public void testAddEventoPasado() throws InvalidDateException,
        InstanceAlreadyCreatedException {
    Categoria categoria = new Categoria("Futbol");
    categoriaDao.save(categoria);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2015, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("nombre", fecha, categoria);
    userService.addEvento(evento);
}

@Test(expected = InstanceAlreadyCreatedException.class)
public void testAddEventoDuplicado() throws InvalidDateException,
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

@Test(expected = InstanceNotFoundException.class)
public void testFindEventoInexistente() throws InstanceNotFoundException {
    userService.findEventoById(NON_EXISTENT_ID);
}

@Test
public void testFindEventosByCategoryAdmin() {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Categoria categoria2 = new Categoria("Baloncesto");
    categoriaDao.save(categoria2);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Calendar fecha2 = Calendar.getInstance();
    fecha2.set(2015, Calendar.JUNE, 11, 10, 30);
    Evento evento = new Evento("nombre", fecha, categoria1);
    eventoDao.save(evento);
    Evento evento2 = new Evento("nombre", fecha, categoria2);
    eventoDao.save(evento2);
    Evento evento3 = new Evento("nombre", fecha2, categoria2);
    eventoDao.save(evento3);
    List<Evento> eventos = new ArrayList<Evento>();
    eventos.add(evento3);
    eventos.add(evento2);
    EventoBlock foundEventos;
    foundEventos = userService.findEventos(null, categoria2.getIdCategoria(),
            true, 0, 10);
    assertEquals(eventos, foundEventos.getEventos());
}

@Test
public void testFindEventosByCategoryUser() {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Categoria categoria2 = new Categoria("Baloncesto");
    categoriaDao.save(categoria2);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Calendar fecha2 = Calendar.getInstance();
    fecha2.set(2015, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("nombre", fecha, categoria1);
    eventoDao.save(evento);
    Evento evento2 = new Evento("nombre", fecha2, categoria2);
    eventoDao.save(evento2);
    Evento evento3 = new Evento("nombre", fecha, categoria2);
    eventoDao.save(evento3);
    List<Evento> eventos = new ArrayList<Evento>();
    eventos.add(evento3);
    EventoBlock foundEventos;
    foundEventos = userService.findEventos(null, categoria2.getIdCategoria(),
            false, 0, 10);
    assertEquals(eventos, foundEventos.getEventos());
}

@Test
public void testFindEventosByKeywords() {

    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Categoria categoria2 = new Categoria("Baloncesto");
    categoriaDao.save(categoria2);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);

    Evento evento = new Evento("evento", fecha, categoria1);
    eventoDao.save(evento);
    Evento evento2 = new Evento("este es el nombre del evento", fecha,
            categoria1);
    eventoDao.save(evento2);
    Evento evento3 = new Evento("nombre del evento", fecha, categoria2);
    eventoDao.save(evento3);
    List<Evento> eventos = new ArrayList<Evento>();
    eventos.add(evento2);
    eventos.add(evento3);

    EventoBlock foundEventos;
    foundEventos = userService.findEventos("nombre evento", null, true, 0, 10);
    assertEquals(eventos, foundEventos.getEventos());
    assertEquals(false, foundEventos.getExistMoreEventos());
    foundEventos.getEventos().clear();

    // Uso de mayúsculas y minúsculas
    foundEventos = userService.findEventos("Nombre del EVENTO", null, true, 0,
            10);
    assertEquals(eventos, foundEventos.getEventos());
    assertEquals(false, foundEventos.getExistMoreEventos());
    foundEventos.getEventos().clear();

    // Palabras desordenadas
    foundEventos = userService.findEventos("evento nom", null, true, 0, 1);
    eventos.clear();
    eventos.add(evento2);
    assertEquals(true, foundEventos.getExistMoreEventos());
    assertEquals(eventos, foundEventos.getEventos());

}

@Test
public void testAddTipoApuesta() throws InstanceNotFoundException,
        EventoStartedException, InvalidDateException,
        InstanceAlreadyCreatedException, InstanceAlreadyCreatedException,
        RepeatedOpcionApuestaException {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addEvento = userService.addEvento(evento);

    // Comprobamos que el tipo de apuesta se inserta en base de datos
    TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta);
    tipoApuesta.setOpcionesApuesta(opcionesApuesta);
    TipoApuesta addedTipoApuesta = userService.addTipoApuesta(
            addEvento.getIdEvento(), tipoApuesta);
    TipoApuesta foundTipoApuesta = tipoApuestaDao.find(addedTipoApuesta
            .getIdTipoApuesta());
    assertEquals(addedTipoApuesta, foundTipoApuesta);
    assertEquals(foundTipoApuesta.getOpcionesApuesta(), opcionesApuesta);

    // Comprobamos que las Opciones de Apuesta se insertan correctamente
    OpcionApuesta foundOpcionApuesta;
    for (OpcionApuesta addedOpcionApuesta : tipoApuesta.getOpcionesApuesta()) {
        foundOpcionApuesta = opcionApuestaDao.find(addedOpcionApuesta
                .getIdOpcionApuesta());
        assertEquals(addedOpcionApuesta, foundOpcionApuesta);
    }

}

@Test(expected = EventoStartedException.class)
public void testAddTipoApuestaFailed() throws InstanceNotFoundException,
        InstanceAlreadyCreatedException, EventoStartedException,
        RepeatedOpcionApuestaException {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2014, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    eventoDao.save(evento);

    TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta);
    tipoApuesta.setOpcionesApuesta(opcionesApuesta);
    userService.addTipoApuesta(evento.getIdEvento(), tipoApuesta);
}

@Test(expected = InstanceAlreadyCreatedException.class)
public void testAddTipoApuestaFailed2() throws InstanceNotFoundException,
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

    TipoApuesta tipoApuesta2 = new TipoApuesta("1x2", null, false);
    Set<OpcionApuesta> opcionesApuesta2 = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta2 = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta2.add(opcionApuesta2);
    tipoApuesta.setOpcionesApuesta(opcionesApuesta2);
    userService.addTipoApuesta(addEvento.getIdEvento(), tipoApuesta2);
}

@Test(expected = InstanceNotFoundException.class)
public void testAddTipoApuestaFailed3() throws InstanceNotFoundException,
        InputValidationException, InstanceAlreadyCreatedException,
        EventoStartedException, RepeatedOpcionApuestaException {

    TipoApuesta tipoApuesta = new TipoApuesta("1x2", null, false);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta);
    tipoApuesta.setOpcionesApuesta(opcionesApuesta);
    userService.addTipoApuesta(-1L, tipoApuesta);
}

@Test(expected = RepeatedOpcionApuestaException.class)
public void testAddTipoApuestaFailed4() throws InstanceNotFoundException,
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
    OpcionApuesta opcionApuesta2 = new OpcionApuesta("X", 1.2F, null);
    OpcionApuesta opcionApuesta3 = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta);
    opcionesApuesta.add(opcionApuesta2);
    opcionesApuesta.add(opcionApuesta3);
    tipoApuesta.setOpcionesApuesta(opcionesApuesta);
    userService.addTipoApuesta(addEvento.getIdEvento(), tipoApuesta);
}

@Test
public void testApostar() throws InstanceNotFoundException,
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
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta("1X2",
            opcionesApuesta, false));
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    float cantidadApostada = 100;

    // Comprobamos que la apuesta se inserta en la base de datos
    ApuestaRealizada addapuestaRealizada = userService.apostar(
            opcionApuesta.getIdOpcionApuesta(), cantidadApostada,
            userProfile.getUserProfileId());
    ApuestaRealizada foundApuestaRealizada = apuestaRealizadaDao
            .find(addapuestaRealizada.getIdApuestaRealizada());
    assertEquals(addapuestaRealizada, foundApuestaRealizada);
}

@Test(expected = EventoStartedException.class)
public void testApostarFailed() throws EventoStartedException,
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
    OpcionApuesta opcionApuesta = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta);
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta("1X2",
            opcionesApuesta, false));
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    float cantidadApostada = 100;
    Calendar fechanueva = Calendar.getInstance();
    fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);

    addEvento.setFecha(fechanueva);
    eventoDao.save(addEvento);
    userService.apostar(opcionApuesta.getIdOpcionApuesta(), cantidadApostada,
            userProfile.getUserProfileId());
}

@Test
public void testEstablecerGanadoras() throws EventoStartedException,
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
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta("1X2",
            opcionesApuesta, false));

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
    userService.especificarGanadoras(opcionApuesta.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);

    OpcionApuesta foundOpcionApuesta = opcionApuestaDao.find(opcionApuesta
            .getIdOpcionApuesta());
    assertEquals(true, foundOpcionApuesta.getEstado());
    OpcionApuesta foundOpcionApuesta2 = opcionApuestaDao.find(opcionApuesta2
            .getIdOpcionApuesta());
    assertEquals(false, foundOpcionApuesta2.getEstado());

}

@Test
public void testEstablecerGanadoras2() throws InstanceNotFoundException,
        EventoStartedException, EventoNotStartedException,
        OpcionApuestaAlreadySolvedException, InputValidationException,
        SimpleWinnerException, InvalidDateException,
        InstanceAlreadyCreatedException, RepeatedOpcionApuestaException,
        InvalidOptionException, InvalidValueException {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta3 = new OpcionApuesta("ruben", 1.2F, null);
    OpcionApuesta opcionApuesta4 = new OpcionApuesta("raquel", 1.2F, null);
    OpcionApuesta opcionApuesta5 = new OpcionApuesta("felipe", 1.2F, null);
    opcionesApuesta.add(opcionApuesta3);
    opcionesApuesta.add(opcionApuesta4);
    opcionesApuesta.add(opcionApuesta5);
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
            "tarjetas", opcionesApuesta, true));

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    userService.apostar(opcionApuesta3.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());
    userService.apostar(opcionApuesta4.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());

    Calendar fechanueva = Calendar.getInstance();
    fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
    addEvento.setFecha(fechanueva);
    eventoDao.save(addEvento);

    List<Long> opcionesGanadoras = new ArrayList<Long>();
    opcionesGanadoras.add(opcionApuesta3.getIdOpcionApuesta());
    opcionesGanadoras.add(opcionApuesta5.getIdOpcionApuesta());
    userService.especificarGanadoras(opcionApuesta3.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);

    OpcionApuesta foundOpcionApuesta3 = opcionApuestaDao.find(opcionApuesta3
            .getIdOpcionApuesta());
    assertEquals(true, foundOpcionApuesta3.getEstado());
    OpcionApuesta foundOpcionApuesta4 = opcionApuestaDao.find(opcionApuesta4
            .getIdOpcionApuesta());
    assertEquals(false, foundOpcionApuesta4.getEstado());
    OpcionApuesta foundOpcionApuesta5 = opcionApuestaDao.find(opcionApuesta5
            .getIdOpcionApuesta());
    assertEquals(true, foundOpcionApuesta5.getEstado());

}

@Test(expected = EventoNotStartedException.class)
public void testEstablecerGanadorasFailed() throws EventoStartedException,
        EventoNotStartedException, OpcionApuestaAlreadySolvedException,
        InputValidationException, InvalidDateException,
        InstanceAlreadyCreatedException, InstanceNotFoundException,
        SimpleWinnerException, RepeatedOpcionApuestaException,
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
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta("1X2",
            opcionesApuesta, false));

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    userService.apostar(opcionApuesta.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());
    userService.apostar(opcionApuesta2.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());

    List<Long> opcionesGanadoras = new ArrayList<Long>();
    opcionesGanadoras.add(opcionApuesta.getIdOpcionApuesta());
    userService.especificarGanadoras(opcionApuesta.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);

}

@Test(expected = OpcionApuestaAlreadySolvedException.class)
public void testEstablecerGanadorasFailed2() throws EventoStartedException,
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
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta("1X2",
            opcionesApuesta, false));

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
    userService.especificarGanadoras(opcionApuesta.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);
    userService.especificarGanadoras(opcionApuesta.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);
}

@Test(expected = OpcionApuestaAlreadySolvedException.class)
public void testEstablecerGanadorasFailed3() throws InstanceNotFoundException,
        EventoStartedException, EventoNotStartedException,
        OpcionApuestaAlreadySolvedException, InputValidationException,
        SimpleWinnerException, InvalidDateException,
        InstanceAlreadyCreatedException, RepeatedOpcionApuestaException,
        InvalidOptionException, InvalidValueException {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta3 = new OpcionApuesta("ruben", 1.2F, null);
    OpcionApuesta opcionApuesta4 = new OpcionApuesta("raquel", 1.2F, null);
    OpcionApuesta opcionApuesta5 = new OpcionApuesta("felipe", 1.2F, null);
    opcionesApuesta.add(opcionApuesta3);
    opcionesApuesta.add(opcionApuesta4);
    opcionesApuesta.add(opcionApuesta5);
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
            "tarjetas", opcionesApuesta, true));

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    userService.apostar(opcionApuesta3.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());
    userService.apostar(opcionApuesta4.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());

    Calendar fechanueva = Calendar.getInstance();
    fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
    addEvento.setFecha(fechanueva);
    eventoDao.save(addEvento);

    List<Long> opcionesGanadoras = new ArrayList<Long>();
    opcionesGanadoras.add(opcionApuesta3.getIdOpcionApuesta());
    opcionesGanadoras.add(opcionApuesta5.getIdOpcionApuesta());
    userService.especificarGanadoras(opcionApuesta3.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);
    userService.especificarGanadoras(opcionApuesta3.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);

}

@Test(expected = SimpleWinnerException.class)
public void testEstablecerGanadorasFailed4() throws InstanceNotFoundException,
        EventoStartedException, EventoNotStartedException,
        OpcionApuestaAlreadySolvedException, InputValidationException,
        SimpleWinnerException, InvalidDateException,
        InstanceAlreadyCreatedException, RepeatedOpcionApuestaException,
        InvalidOptionException, InvalidValueException {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta3 = new OpcionApuesta("ruben", 1.2F, null);
    OpcionApuesta opcionApuesta4 = new OpcionApuesta("raquel", 1.2F, null);
    OpcionApuesta opcionApuesta5 = new OpcionApuesta("felipe", 1.2F, null);
    opcionesApuesta.add(opcionApuesta3);
    opcionesApuesta.add(opcionApuesta4);
    opcionesApuesta.add(opcionApuesta5);
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
            "tarjetas", opcionesApuesta, false));

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    userService.apostar(opcionApuesta3.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());
    userService.apostar(opcionApuesta4.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());

    Calendar fechanueva = Calendar.getInstance();
    fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
    addEvento.setFecha(fechanueva);
    eventoDao.save(addEvento);

    List<Long> opcionesGanadoras = new ArrayList<Long>();
    opcionesGanadoras.add(opcionApuesta3.getIdOpcionApuesta());
    opcionesGanadoras.add(opcionApuesta5.getIdOpcionApuesta());
    userService.especificarGanadoras(opcionApuesta3.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);

}

@Test(expected = InvalidOptionException.class)
public void testEstablecerGanadorasFailed5() throws InstanceNotFoundException,
        EventoStartedException, EventoNotStartedException,
        OpcionApuestaAlreadySolvedException, InputValidationException,
        SimpleWinnerException, InvalidDateException,
        InstanceAlreadyCreatedException, RepeatedOpcionApuestaException,
        InvalidOptionException, InvalidValueException {

    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta3 = new OpcionApuesta("ruben", 1.2F, null);
    OpcionApuesta opcionApuesta4 = new OpcionApuesta("raquel", 1.2F, null);
    OpcionApuesta opcionApuesta5 = new OpcionApuesta("felipe", 1.2F, null);
    opcionesApuesta.add(opcionApuesta3);
    opcionesApuesta.add(opcionApuesta4);
    opcionesApuesta.add(opcionApuesta5);
    userService.addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
            "tarjetas", opcionesApuesta, true));

    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    userService.apostar(opcionApuesta3.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());
    userService.apostar(opcionApuesta4.getIdOpcionApuesta(), 100,
            userProfile.getUserProfileId());

    Calendar fechanueva = Calendar.getInstance();
    fechanueva.set(2015, Calendar.AUGUST, 11, 10, 30);
    addEvento.setFecha(fechanueva);
    eventoDao.save(addEvento);

    List<Long> opcionesGanadoras = new ArrayList<Long>();
    opcionesGanadoras.add(opcionApuesta5.getIdOpcionApuesta());
    opcionesGanadoras.add(100L);
    userService.especificarGanadoras(opcionApuesta3.getTipoApuesta()
            .getIdTipoApuesta(), opcionesGanadoras);

}

@Test
public void testConsultarApuestas() throws EventoStartedException,
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
    TipoApuesta tipoApuesta1 = userService.addTipoApuesta(addEvento
            .getIdEvento(), new TipoApuesta("1X2", opcionesApuesta, false));

    opcionesApuesta.remove(opcionApuesta1);
    OpcionApuesta opcionApuesta2 = new OpcionApuesta("Messi", 1.2F, null);
    opcionesApuesta.add(opcionApuesta2);
    TipoApuesta tipoApuesta2 = userService
            .addTipoApuesta(addEvento.getIdEvento(), new TipoApuesta(
                    "Goleador", opcionesApuesta, false));
    String clearPassword1 = "userPassword";
    UserProfile userProfile1 = registerUser("user1", clearPassword1);
    String clearPassword2 = "userPassword";
    UserProfile userProfile2 = registerUser("user2", clearPassword2);

    ApuestaRealizada apuesta1 = userService.apostar(
            opcionApuesta1.getIdOpcionApuesta(), 100,
            userProfile1.getUserProfileId());
    ApuestaRealizada apuesta2 = userService.apostar(
            opcionApuesta2.getIdOpcionApuesta(), 100,
            userProfile1.getUserProfileId());
    ApuestaRealizada apuesta3 = userService.apostar(
            opcionApuesta1.getIdOpcionApuesta(), 100,
            userProfile2.getUserProfileId());

    List<ApuestaRealizada> apuestas = new ArrayList<ApuestaRealizada>();
    apuestas.add(apuesta1);
    apuestas.add(apuesta2);

    ApuestaBlock foundApuestas;
    foundApuestas = userService.consultarApuestas(
            userProfile1.getUserProfileId(), 0, 2);
    assertEquals(false, foundApuestas.getExistMoreApuestas());
    assertEquals(apuestas, foundApuestas.getApuestas());

    foundApuestas.getApuestas().clear();
    apuestas.clear();
    apuestas.add(apuesta1);

    foundApuestas = userService.consultarApuestas(
            userProfile1.getUserProfileId(), 0, 1);
    assertEquals(true, foundApuestas.getExistMoreApuestas());
    assertEquals(apuestas, foundApuestas.getApuestas());

}

@Test
public void testFindCategories() {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Categoria categoria2 = new Categoria("Baloncesto");
    categoriaDao.save(categoria2);
    Categoria categoria3 = new Categoria("Tenis");
    categoriaDao.save(categoria3);

    List<Categoria> categorias = new ArrayList<Categoria>();
    categorias.add(categoria1);
    categorias.add(categoria2);
    categorias.add(categoria3);

    List<Categoria> foundCategorias = categoriaDao.findAll();
    assertEquals(categorias, foundCategorias);
}

@Test
public void testFindCategoryById() throws InstanceNotFoundException {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Categoria foundCategoria = categoriaDao.find(categoria1.getIdCategoria());
    assertEquals(categoria1, foundCategoria);
}

@Test(expected = InstanceNotFoundException.class)
public void testFindCategoryInexistente() throws InstanceNotFoundException {
    categoriaDao.find(NON_EXISTENT_ID);

}

@Test
public void testFindTipoApuestaById() throws InvalidDateException,
        InstanceAlreadyCreatedException, InstanceNotFoundException,
        EventoStartedException, InstanceAlreadyCreatedException,
        RepeatedOpcionApuestaException {

    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addedEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta1 = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta1);
    TipoApuesta tipo = new TipoApuesta("Goleador", opcionesApuesta, false);
    userService.addTipoApuesta(addedEvento.getIdEvento(), tipo);
    TipoApuesta foundTipo = userService.findTipoApuestaById(tipo
            .getIdTipoApuesta());
    assertEquals(foundTipo, tipo);

}

@Test(expected = InstanceNotFoundException.class)
public void testFindTipoApuestaInexistente() throws InstanceNotFoundException {
    userService.findTipoApuestaById(NON_EXISTENT_ID);
}

@Test
public void testFindOpcionApuestaById() throws InstanceNotFoundException,
        InvalidDateException, InstanceAlreadyCreatedException,
        EventoStartedException, InstanceAlreadyCreatedException,
        RepeatedOpcionApuestaException {

    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addedEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta1 = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta1);
    TipoApuesta tipo = new TipoApuesta("Goleador", opcionesApuesta, false);
    userService.addTipoApuesta(addedEvento.getIdEvento(), tipo);
    OpcionApuesta foundOpcion = userService
            .findOpcionApuestaById(opcionApuesta1.getIdOpcionApuesta());
    assertEquals(opcionApuesta1, foundOpcion);
}

@Test(expected = InstanceNotFoundException.class)
public void testFindOpcionApuestaInexistente() throws InstanceNotFoundException {
    userService.findOpcionApuestaById(NON_EXISTENT_ID);
}

@Test
public void testFindApuestaById() throws InstanceNotFoundException,
        InvalidDateException, InstanceAlreadyCreatedException,
        EventoStartedException, InstanceAlreadyCreatedException,
        RepeatedOpcionApuestaException, InvalidValueException {

    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);
    Evento evento = new Evento("evento", fecha, categoria1);
    Evento addedEvento = userService.addEvento(evento);
    Set<OpcionApuesta> opcionesApuesta = new HashSet<OpcionApuesta>();
    OpcionApuesta opcionApuesta1 = new OpcionApuesta("1", 1.2F, null);
    opcionesApuesta.add(opcionApuesta1);
    TipoApuesta tipo = new TipoApuesta("Goleador", opcionesApuesta, false);
    userService.addTipoApuesta(addedEvento.getIdEvento(), tipo);
    String clearPassword1 = "userPassword";
    UserProfile userProfile1 = registerUser("user1", clearPassword1);
    ApuestaRealizada apuesta1 = userService.apostar(
            opcionApuesta1.getIdOpcionApuesta(), 100,
            userProfile1.getUserProfileId());
    ApuestaRealizada foundApuesta = userService.findApuestaById(apuesta1
            .getIdApuestaRealizada());
    assertEquals(apuesta1, foundApuesta);

}

@Test(expected = InstanceNotFoundException.class)
public void testFindApuestaInexistente() throws InstanceNotFoundException {
    userService.findApuestaById(NON_EXISTENT_ID);
}

@Test
public void testGetNumberOfEventos() {
    Categoria categoria1 = new Categoria("Futbol");
    categoriaDao.save(categoria1);
    Categoria categoria2 = new Categoria("Baloncesto");
    categoriaDao.save(categoria2);
    Calendar fecha = Calendar.getInstance();
    fecha.set(2017, Calendar.AUGUST, 11, 10, 30);

    Evento evento = new Evento("evento", fecha, categoria1);
    eventoDao.save(evento);
    Evento evento2 = new Evento("este es el nombre del evento", fecha,
            categoria1);
    eventoDao.save(evento2);
    Evento evento3 = new Evento("nombre del evento", fecha, categoria2);
    eventoDao.save(evento3);

    EventoBlock foundEventos;
    int numberEvents = userService.getNumberOfEventos("nombre evento", null,
            true);
    assertEquals(2, numberEvents);

    numberEvents = userService.getNumberOfEventos(null,
            categoria1.getIdCategoria(), true);
    assertEquals(2, numberEvents);

    numberEvents = userService.getNumberOfEventos("evento", null, true);
    assertEquals(3, numberEvents);

}
}
