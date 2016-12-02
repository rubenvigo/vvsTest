package es.udc.pa.pa002.practicapa.model.userservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa002.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class UserServiceImpl.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

/** The user profile dao. */
@Autowired
private UserProfileDao userProfileDao;

/** The evento dao. */
@Autowired
private EventoDao eventoDao;

/** The tipo apuesta dao. */
@Autowired
private TipoApuestaDao tipoApuestaDao;

/** The opcion apuesta dao. */
@Autowired
private OpcionApuestaDao opcionApuestaDao;

/** The apuesta realizada dao. */
@Autowired
private ApuestaRealizadaDao apuestaRealizadaDao;

/** The categoria dao. */
@Autowired
private CategoriaDao categoriaDao;

@Override
public final UserProfile registerUser(final String loginName,
        final String clearPassword, final UserProfileDetails userProfileDetails)
        throws DuplicateInstanceException {

    try {
        userProfileDao.findByLoginName(loginName);
        throw new DuplicateInstanceException(loginName,
                UserProfile.class.getName());
    } catch (InstanceNotFoundException e) {
        String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

        UserProfile userProfile = new UserProfile(loginName, encryptedPassword,
                userProfileDetails.getFirstName(),
                userProfileDetails.getLastName(), userProfileDetails.getEmail());

        userProfileDao.save(userProfile);
        return userProfile;
    }

}

@Override
@Transactional(readOnly = true)
public final UserProfile login(final String loginName, final String password,
        final boolean passwordIsEncrypted) throws InstanceNotFoundException,
        IncorrectPasswordException {

    UserProfile userProfile = userProfileDao.findByLoginName(loginName);
    String storedPassword = userProfile.getEncryptedPassword();

    if (passwordIsEncrypted) {
        if (!password.equals(storedPassword)) {
            throw new IncorrectPasswordException(loginName);
        }
    } else {
        if (!PasswordEncrypter.isClearPasswordCorrect(password, storedPassword)) {
            throw new IncorrectPasswordException(loginName);
        }
    }
    return userProfile;

}

@Override
@Transactional(readOnly = true)
public final UserProfile findUserProfile(final Long userProfileId)
        throws InstanceNotFoundException {

    return userProfileDao.find(userProfileId);
}

@Override
public final void updateUserProfileDetails(final Long userProfileId,
        final UserProfileDetails userProfileDetails)
        throws InstanceNotFoundException {

    UserProfile userProfile = userProfileDao.find(userProfileId);
    userProfile.setFirstName(userProfileDetails.getFirstName());
    userProfile.setLastName(userProfileDetails.getLastName());
    userProfile.setEmail(userProfileDetails.getEmail());

}

@Override
public final void changePassword(final Long userProfileId,
        final String oldClearPassword, final String newClearPassword)
        throws IncorrectPasswordException, InstanceNotFoundException {

    UserProfile userProfile;
    userProfile = userProfileDao.find(userProfileId);

    String storedPassword = userProfile.getEncryptedPassword();

    if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
            storedPassword)) {
        throw new IncorrectPasswordException(userProfile.getLoginName());
    }

    userProfile.setEncryptedPassword(PasswordEncrypter.crypt(newClearPassword));

}

/**
 * Validar evento.
 *
 * @param evento
 *            the evento
 * @throws InvalidDateException
 *             the invalid date exception
 * @throws InstanceAlreadyCreatedException
 *             the instance already created exception
 */
private void validarEvento(final Evento evento) throws InvalidDateException,
        InstanceAlreadyCreatedException {
    if (evento.getFecha().before(Calendar.getInstance())) {
        throw new InvalidDateException("La fecha introducida no es correcta");
    }
    if (eventoDao.existsEvent(evento.getNombre(), evento.getCategoria()
            .getIdCategoria(), evento.getFecha())) {
        throw new InstanceAlreadyCreatedException("El evento ya ha sido creado");
    }
}

@Override
public final Evento addEvento(final Evento evento) throws InvalidDateException,
        InstanceAlreadyCreatedException {
    validarEvento(evento);
    eventoDao.save(evento);
    return evento;
}

@Override
public final Evento findEventoById(final Long idEvento)
        throws InstanceNotFoundException {
    Evento evento = eventoDao.find(idEvento);
    return evento;
}

@Transactional(readOnly = true)
@Override
public final EventoBlock findEventos(final String keywords,
        final Long idCategoria, final boolean admin, final int startIndex,
        final int count) {
    List<Evento> listaEventos;

    listaEventos = eventoDao.findByParameters(keywords, idCategoria, admin,
            startIndex, count + 1);
    boolean existMoreEventos = listaEventos.size() == count + 1;

    if (existMoreEventos) {
        listaEventos.remove(listaEventos.size() - 1);
    }
    return new EventoBlock(listaEventos, existMoreEventos);
}

/**
 * Validar opciones apuesta.
 *
 * @param opcionesApuesta
 *            the opciones apuesta
 * @throws RepeatedOpcionApuestaException
 *             the repeated opcion apuesta exception
 */
private void validarOpcionesApuesta(final Set<OpcionApuesta> opcionesApuesta)
        throws RepeatedOpcionApuestaException {

    for (OpcionApuesta opcion : opcionesApuesta) {
        for (OpcionApuesta opcion2 : opcionesApuesta) {
            if (!opcion.equals(opcion2)
                    && opcion.getRespuesta().equals(opcion2.getRespuesta())) {
                throw new RepeatedOpcionApuestaException(
                        "No está permitido introducir opciones de apuesta repetidas");
            }
        }

    }

}

@Override
public final TipoApuesta addTipoApuesta(final Long idEvento,
        final TipoApuesta tipoApuesta) throws EventoStartedException,
        InstanceNotFoundException, InstanceAlreadyCreatedException,
        RepeatedOpcionApuestaException {
    Calendar hoy = Calendar.getInstance();
    Evento evento = eventoDao.find(idEvento);
    if (evento.getFecha().before(hoy)) {
        throw new EventoStartedException(
                "No es posible añadir tipos de apuesta a un evento"
                        + " que ya ha comenzado.");
    }
    if (evento.existsTipoApuesta(tipoApuesta.getPregunta())) {
        throw new InstanceAlreadyCreatedException(
                "Ya existe el tipo de apuesta.");
    }
    validarOpcionesApuesta(tipoApuesta.getOpcionesApuesta());
    tipoApuesta.setEvento(evento);
    tipoApuestaDao.save(tipoApuesta);
    for (OpcionApuesta opcionApuesta : tipoApuesta.getOpcionesApuesta()) {
        opcionApuesta.setTipoApuesta(tipoApuesta);
        opcionApuestaDao.save(opcionApuesta);
    }
    evento.addTipoApuesta(tipoApuesta);
    return tipoApuesta;
}

@Override
public final ApuestaRealizada apostar(final Long idOpcionApuesta,
        final float cantidadApostada, final Long userId)
        throws EventoStartedException, InstanceNotFoundException,
        InvalidValueException {

    Calendar hoy = Calendar.getInstance();
    if (cantidadApostada <= 0) {
        throw new InvalidValueException(
                "No es posible apostar una cantidad inferior o igual a cero");
    }
    OpcionApuesta opcionApuesta = opcionApuestaDao.find(idOpcionApuesta);
    if (opcionApuesta.getTipoApuesta().getEvento().getFecha().before(hoy)) {
        throw new EventoStartedException(
                "No es posible realizar apuestas sobre "
                        + "eventos que ya han comenzado.");
    }
    UserProfile user = userProfileDao.find(userId);
    ApuestaRealizada apuestaRealizada = new ApuestaRealizada(user,
            opcionApuesta, cantidadApostada, Calendar.getInstance());
    apuestaRealizadaDao.save(apuestaRealizada);
    return apuestaRealizada;

}

@Override
public final void especificarGanadoras(final Long tipoApuestaId,
        final List<Long> ganadoras) throws EventoNotStartedException,
        OpcionApuestaAlreadySolvedException, InstanceNotFoundException,
        SimpleWinnerException, InvalidOptionException {

    TipoApuesta tipoApuesta = this.findTipoApuestaById(tipoApuestaId);
    Calendar hoy = Calendar.getInstance();
    Set<OpcionApuesta> opciones = tipoApuesta.getOpcionesApuesta();

    if (tipoApuesta.getEvento().getFecha().after(hoy)) {
        throw new EventoNotStartedException(
                "No es posible establecer opciones ganadores "
                        + "sobre eventos que no han comenzado.");
    }

    List<Long> idsOpciones = new ArrayList<Long>();
    for (OpcionApuesta opcion : opciones) {
        idsOpciones.add(opcion.getIdOpcionApuesta());
    }
    for (Long id : ganadoras) {
        if (!idsOpciones.contains(id)) {
            throw new InvalidOptionException("La opción con id "
                    + id.toString()
                    + " no pertenece al tipo de apuesta con id "
                    + tipoApuestaId.toString());
        }
    }
    if (tipoApuesta.getOpcionesApuesta().iterator().next().getEstado() != null) {
        throw new OpcionApuestaAlreadySolvedException(
                "No es posible establecer opciones "
                        + "ganadoras sobre obciones ya resueltas.");
    }
    if (!tipoApuesta.isMultiplesGanadoras() && ganadoras.size() > 1) {
        throw new SimpleWinnerException(
                "No es posible establecer varias opciones "
                        + "ganadoras, la pregunta tiene una unica opcion ganadora.");
    }

    for (OpcionApuesta opcion : opciones) {
        if (ganadoras.contains(opcion.getIdOpcionApuesta())) {
            opcion.setEstado(true);
        } else {
            opcion.setEstado(false);
        }
        opcionApuestaDao.save(opcion);
    }
}

@Override
public final ApuestaBlock consultarApuestas(final Long idUsuario,
        final int startIndex, final int count) {
    List<ApuestaRealizada> apuestas;

    apuestas = apuestaRealizadaDao.findByUser(idUsuario, startIndex, count + 1);

    boolean existMoreApuestas = apuestas.size() == count + 1;

    if (existMoreApuestas) {
        apuestas.remove(apuestas.size() - 1);
    }

    return new ApuestaBlock(apuestas, existMoreApuestas);
}

@Override
public final List<Categoria> findCategories() {
    return categoriaDao.findAll();
}

@Override
public final TipoApuesta findTipoApuestaById(final Long idTipoApuesta)
        throws InstanceNotFoundException {
    TipoApuesta tipoApuesta = tipoApuestaDao.find(idTipoApuesta);
    return tipoApuesta;
}

@Override
public final OpcionApuesta findOpcionApuestaById(final Long idOpcionApuesta)
        throws InstanceNotFoundException {
    OpcionApuesta opcion = opcionApuestaDao.find(idOpcionApuesta);
    return opcion;
}

@Override
public final Categoria findCategoryById(final Long idCategoria)
        throws InstanceNotFoundException {
    Categoria categoria = categoriaDao.find(idCategoria);
    return categoria;
}

@Override
public final ApuestaRealizada findApuestaById(final Long idApuesta)
        throws InstanceNotFoundException {
    return apuestaRealizadaDao.find(idApuesta);
}

@Override
public final int getNumberOfEventos(final String keywords,
        final Long idCategoria, final boolean admin) {
    return eventoDao.getNumberOfEventos(keywords, idCategoria, admin);
}

}
