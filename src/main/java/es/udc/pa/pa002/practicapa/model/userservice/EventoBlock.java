package es.udc.pa.pa002.practicapa.model.userservice;

import java.util.List;
import es.udc.pa.pa002.practicapa.model.evento.Evento;


public class EventoBlock {
	
	
	 private List<Evento> eventos;
	    private boolean existMoreEventos;

	    public EventoBlock(List<Evento> eventos, boolean existMoreEventos) {
	        
	        this.eventos = eventos;
	        this.existMoreEventos = existMoreEventos;

	    }
	    
	    public List<Evento> getEventos() {
	        return eventos;
	    }
	    
	    public boolean getExistMoreEventos() {
	        return existMoreEventos;
	    }

}
