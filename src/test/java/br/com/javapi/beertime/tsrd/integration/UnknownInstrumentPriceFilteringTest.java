package br.com.javapi.beertime.tsrd.integration;

import org.junit.Assert;
import org.junit.Test;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

public class UnknownInstrumentPriceFilteringTest {

    @Test
    public void testUnknownINstruments() {
        InstrumentPriceFilter filter  = new UnknownInstrumentPriceConfiguration().filterUnknown();
        
        Assert.assertTrue(filter.reject(InstrumentPriceDTO.fromCSV("INSTRUMENT3")));
        Assert.assertTrue(filter.reject(InstrumentPriceDTO.fromCSV("INSTRUMENT3,25-Jun-2017")));
        Assert.assertFalse(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,25-Jun-2017,33.0")));
    }
}
