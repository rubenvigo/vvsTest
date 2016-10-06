package es.udc.pa.pa002.practicapa.web.util;
import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa002.practicapa.model.opcionapuesta.OpcionApuesta;
import es.udc.pa.pa002.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


public class OpcionApuestaEncoder implements ValueEncoder<OpcionApuesta>{
	
	private UserService userService;
	
	public OpcionApuestaEncoder(UserService userService){
		this.userService = userService;
	}
	
	@Override
	public String toClient(OpcionApuesta value){
		return String.valueOf(value.getIdOpcionApuesta());
	}
	
	@Override
	public OpcionApuesta toValue(String id){
		try {
			return userService.findOpcionApuestaById(Long.parseLong(id));
		} catch (NumberFormatException e) {
			
		} catch (InstanceNotFoundException e) {
			
		} return null;
	}

}
