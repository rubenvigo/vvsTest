package es.udc.pa.pa002.practicapa.web.pages.useroperations;


import java.util.Calendar;
import java.util.Set;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.tipoapuesta.TipoApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.pages.admin.AnadirTipoApuesta;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventoDetails {

	private Long idEvento;
	
	private Evento evento;
	
	@Property
	private TipoApuesta tipoApuesta;
	
	@InjectPage 
	private AnadirTipoApuesta anadirTipoApuesta;
	
	@Inject
	private UserService userService;
	
	@Property
	@SessionState(create=false)
	private UserSession userSession;
	
	public Set<TipoApuesta> getTiposApuesta(){
		return evento.getTipoApuesta();
	}
	
	public boolean getIsResolved(){
		boolean resuelta = false;
		for(OpcionApuesta opcion:tipoApuesta.getOpcionesApuesta()){
			resuelta = (opcion.getEstado()!=null);
		}
		return resuelta;
	}
	
	public void setIdEvento(Long idEvento){
		this.idEvento=idEvento;
	}

	public Evento getEvento() {
		return evento;
	}
	
	public boolean getIsAdmin(){
		return (userSession!= null) && (userSession.isAdmin());
	}
	
	public boolean getEventoStart(){
		Calendar now=Calendar.getInstance();
		return(evento.getFecha().before(now));
	}
	
	void onActivate(Long idEvento){
		this.idEvento=idEvento;
		try {
			evento=userService.findEventoById(idEvento);
		} catch (InstanceNotFoundException e) {
			
		}
	}
	
	Long onPassivate() {
		return idEvento;
	}
	
	Object onSuccess(){
		anadirTipoApuesta.setIdEvento(idEvento);
		return anadirTipoApuesta;	
	}
	
}
