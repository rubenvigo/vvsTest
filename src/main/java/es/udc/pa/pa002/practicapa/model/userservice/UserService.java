package es.udc.pa.pa002.practicapa.model.userservice;

import java.util.List;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;
import es.udc.pa.pa002.practicapa.model.categoria.Categoria;
import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Interface UserService.
 */
public interface UserService {

/**
 * Register user.
 *
 * @param loginName
 *            the login name
 * @param clearPassword
 *            the clear password
 * @param userProfileDetails
 *            the user profile details
 * @return the user profile
 * @throws DuplicateInstanceException
 *             the duplicate instance exception
 */
public UserProfile registerUser(String loginName, String clearPassword,
        UserProfileDetails userProfileDetails)
        throws DuplicateInstanceException;

/**
 * Login.
 *
 * @param loginName
 *            the login name
 * @param password
 *            the password
 * @param passwordIsEncrypted
 *            the password is encrypted
 * @return the user profile
 * @throws InstanceNotFoundException
 *             the instance not found exception
 * @throws IncorrectPasswordException
 *             the incorrect password exception
 */
public UserProfile login(String loginName, String password,
        boolean passwordIsEncrypted) throws InstanceNotFoundException,
        IncorrectPasswordException;

/**
 * Find user profile.
 *
 * @param userProfileId
 *            the user profile id
 * @return the user profile
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public UserProfile findUserProfile(Long userProfileId)
        throws InstanceNotFoundException;

/**
 * Update user profile details.
 *
 * @param userProfileId
 *            the user profile id
 * @param userProfileDetails
 *            the user profile details
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public void updateUserProfileDetails(Long userProfileId,
        UserProfileDetails userProfileDetails) throws InstanceNotFoundException;

/**
 * Change password.
 *
 * @param userProfileId
 *            the user profile id
 * @param oldClearPassword
 *            the old clear password
 * @param newClearPassword
 *            the new clear password
 * @throws IncorrectPasswordException
 *             the incorrect password exception
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public void changePassword(Long userProfileId, String oldClearPassword,
        String newClearPassword) throws IncorrectPasswordException,
        InstanceNotFoundException;

/**
 * Find categories.
 *
 * @return the list
 */
public List<Categoria> findCategories();

/**
 * Find category by id.
 *
 * @param idCategoria
 *            the id categoria
 * @return the categoria
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public Categoria findCategoryById(Long idCategoria)
        throws InstanceNotFoundException;

/**
 * Adds the evento.
 *
 * @param evento
 *            the evento
 * @return the evento
 * @throws InvalidDateException
 *             the invalid date exception
 * @throws InstanceAlreadyCreatedException
 *             the instance already created exception
 */
public Evento addEvento(Evento evento) throws InvalidDateException,
        InstanceAlreadyCreatedException;

/**
 * Find evento by id.
 *
 * @param idEvento
 *            the id evento
 * @return the evento
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public Evento findEventoById(Long idEvento) throws InstanceNotFoundException;

/**
 * Find eventos.
 *
 * @param keywords
 *            the keywords
 * @param idCategoria
 *            the id categoria
 * @param admin
 *            the admin
 * @param startIndex
 *            the start index
 * @param count
 *            the count
 * @return the evento block
 */
public EventoBlock findEventos(String keywords, Long idCategoria,
        boolean admin, int startIndex, int count);

/**
 * Adds the tipo apuesta.
 *
 * @param idEvento
 *            the id evento
 * @param tipoApuesta
 *            the tipo apuesta
 * @return the tipo apuesta
 * @throws EventoStartedException
 *             the evento started exception
 * @throws InstanceNotFoundException
 *             the instance not found exception
 * @throws InstanceAlreadyCreatedException
 *             the instance already created exception
 * @throws RepeatedOpcionApuestaException
 *             the repeated opcion apuesta exception
 */
public TipoApuesta addTipoApuesta(Long idEvento, TipoApuesta tipoApuesta)
        throws EventoStartedException, InstanceNotFoundException,
        InstanceAlreadyCreatedException, RepeatedOpcionApuestaException;

/**
 * Find tipo apuesta by id.
 *
 * @param idTipoApuesta
 *            the id tipo apuesta
 * @return the tipo apuesta
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public TipoApuesta findTipoApuestaById(Long idTipoApuesta)
        throws InstanceNotFoundException;

/**
 * Find opcion apuesta by id.
 *
 * @param idOpcionApuesta
 *            the id opcion apuesta
 * @return the opcion apuesta
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public OpcionApuesta findOpcionApuestaById(Long idOpcionApuesta)
        throws InstanceNotFoundException;

/**
 * Gets the number of eventos.
 *
 * @param keywords
 *            the keywords
 * @param idCategoria
 *            the id categoria
 * @param admin
 *            the admin
 * @return the number of eventos
 */
public int getNumberOfEventos(String keywords, Long idCategoria, boolean admin);

/**
 * Apostar.
 *
 * @param idOpcionApuesta
 *            the id opcion apuesta
 * @param cantidad
 *            the cantidad
 * @param userId
 *            the user id
 * @return the apuesta realizada
 * @throws EventoStartedException
 *             the evento started exception
 * @throws InstanceNotFoundException
 *             the instance not found exception
 * @throws InvalidValueException
 *             the invalid value exception
 */
public ApuestaRealizada apostar(Long idOpcionApuesta, float cantidad,
        Long userId) throws EventoStartedException, InstanceNotFoundException,
        InvalidValueException;

/**
 * Especificar ganadoras.
 *
 * @param tipoApuestaId
 *            the tipo apuesta id
 * @param ganadoras
 *            the ganadoras
 * @throws EventoNotStartedException
 *             the evento not started exception
 * @throws OpcionApuestaAlreadySolvedException
 *             the opcion apuesta already solved exception
 * @throws InstanceNotFoundException
 *             the instance not found exception
 * @throws SimpleWinnerException
 *             the simple winner exception
 * @throws InvalidOptionException
 *             the invalid option exception
 */
public void EspecificarGanadoras(Long tipoApuestaId, List<Long> ganadoras)
        throws EventoNotStartedException, OpcionApuestaAlreadySolvedException,
        InstanceNotFoundException, SimpleWinnerException,
        InvalidOptionException;

/**
 * Consultar apuestas.
 *
 * @param idUsuario
 *            the id usuario
 * @param startIndex
 *            the start index
 * @param count
 *            the count
 * @return the apuesta block
 */
public ApuestaBlock consultarApuestas(Long idUsuario, int startIndex, int count);

/**
 * Find apuesta by id.
 *
 * @param idApuesta
 *            the id apuesta
 * @return the apuesta realizada
 * @throws InstanceNotFoundException
 *             the instance not found exception
 */
public ApuestaRealizada findApuestaById(Long idApuesta)
        throws InstanceNotFoundException;
}
