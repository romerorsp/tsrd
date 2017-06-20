package br.com.javapi.beertime.tsrd.integration;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

public interface InstrumentPriceFilter {

    boolean reject(InstrumentPriceDTO dto);
}
