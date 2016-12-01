package es.udc.pa.pa002.practicapa.model.userservice;

import java.util.List;

import es.udc.pa.pa002.practicapa.model.apuestarealizada.ApuestaRealizada;

/**
 * The Class ApuestaBlock.
 */
public class ApuestaBlock {

/** The apuestas. */
private List<ApuestaRealizada> apuestas;

/** The exist more apuestas. */
private boolean existMoreApuestas;

/**
 * Instantiates a new apuesta block.
 *
 * @param apuestas
 *            the apuestas
 * @param existMoreApuestas
 *            the exist more apuestas
 */
public ApuestaBlock(final List<ApuestaRealizada> apuestas,
        final boolean existMoreApuestas) {

    this.apuestas = apuestas;
    this.existMoreApuestas = existMoreApuestas;

}

/**
 * Gets the apuestas.
 *
 * @return the apuestas
 */
public final List<ApuestaRealizada> getApuestas() {
    return apuestas;
}

/**
 * Gets the exist more apuestas.
 *
 * @return the exist more apuestas
 */
public final boolean getExistMoreApuestas() {
    return existMoreApuestas;
}

}
