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

public interface UserService {

    public UserProfile registerUser(String loginName, String clearPassword,
            UserProfileDetails userProfileDetails)
            throws DuplicateInstanceException;

    public UserProfile login(String loginName, String password,
            boolean passwordIsEncrypted) throws InstanceNotFoundException,
            IncorrectPasswordException;

    public UserProfile findUserProfile(Long userProfileId)
            throws InstanceNotFoundException;

    public void updateUserProfileDetails(Long userProfileId,
            UserProfileDetails userProfileDetails)
            throws InstanceNotFoundException;

    public void changePassword(Long userProfileId, String oldClearPassword,
            String newClearPassword) throws IncorrectPasswordException,
            InstanceNotFoundException;
    
    public List<Categoria> findCategories();
    
    public Categoria findCategoryById(Long idCategoria)throws  InstanceNotFoundException;
    
    public Evento addEvento(Evento evento)throws InvalidDateException, InstanceAlreadyCreatedException;
    
    public Evento findEventoById (Long idEvento) throws InstanceNotFoundException;
    
    public EventoBlock findEventos(String keywords, Long idCategoria, boolean admin, int startIndex, int count);
    
    public TipoApuesta addTipoApuesta(Long idEvento,TipoApuesta tipoApuesta) throws EventoStartedException,InstanceNotFoundException,
    	InstanceAlreadyCreatedException,RepeatedOpcionApuestaException;
    
    public TipoApuesta findTipoApuestaById(Long idTipoApuesta) throws InstanceNotFoundException;
    
    public OpcionApuesta findOpcionApuestaById(Long idOpcionApuesta) throws InstanceNotFoundException;
    
    public int getNumberOfEventos(String keywords, Long idCategoria,boolean admin);
    
    public ApuestaRealizada apostar(Long idOpcionApuesta, float cantidad, Long userId) throws EventoStartedException, InstanceNotFoundException;

    public void EspecificarGanadoras(Long tipoApuestaId, List<Long> ganadoras) throws EventoNotStartedException, OpcionApuestaAlreadySolvedException, 
    InstanceNotFoundException, SimpleWinnerException, InvalidOptionException;
    
    public ApuestaBlock consultarApuestas (Long idUsuario,int startIndex, int count);
    
    public ApuestaRealizada findApuestaById (Long idApuesta)throws InstanceNotFoundException;
}
