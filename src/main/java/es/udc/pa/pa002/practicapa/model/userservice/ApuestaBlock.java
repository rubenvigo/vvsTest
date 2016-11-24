package es.udc.pa.pa002.practicapa.model.userservice;

import java.util.List;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;

public class ApuestaBlock {
private List<ApuestaRealizada> apuestas;
private boolean existMoreApuestas;

public ApuestaBlock(List<ApuestaRealizada> apuestas, boolean existMoreApuestas) {

    this.apuestas = apuestas;
    this.existMoreApuestas = existMoreApuestas;

}

public List<ApuestaRealizada> getApuestas() {
    return apuestas;
}

public boolean getExistMoreApuestas() {
    return existMoreApuestas;
}

}
