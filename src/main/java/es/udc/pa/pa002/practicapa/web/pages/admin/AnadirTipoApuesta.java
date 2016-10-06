package es.udc.pa.pa002.practicapa.web.pages.admin;


import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa002.practicapa.model.evento.Evento;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa002.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa002.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class AnadirTipoApuesta {

	private Long idEvento;
	private Evento evento;
	
	
	@Property
	private boolean multiplesganadoras;
	
	@Property
	private String pregunta;
	
	@InjectPage 
	private AnadirOpcionApuesta anadirOpcionApuesta;
	
	@Inject
	private UserService userService;
	
	@Property
	@SessionState(create=false)
	private UserSession userSession;
	
	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}
	
	public Long getIdEvento() {
		return idEvento;
	}

	public Evento getEvento() {
		return evento;
	}

	public boolean getIsAdmin(){
		return (userSession!= null) && (userSession.isAdmin());
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
		anadirOpcionApuesta.setIdEvento(idEvento);
		anadirOpcionApuesta.setPregunta(pregunta);
		anadirOpcionApuesta.setMultiplesganadoras(multiplesganadoras);
		anadirOpcionApuesta.setOpciones(null);
		return anadirOpcionApuesta;	
	}
	
	
}
