package es.udc.pa.pa002.practicapa.model.userservice;

@SuppressWarnings("serial")
public class OpcionApuestaAlreadySolvedException extends Exception {

public OpcionApuestaAlreadySolvedException(String mensaje) {
    super(mensaje);
}
}
