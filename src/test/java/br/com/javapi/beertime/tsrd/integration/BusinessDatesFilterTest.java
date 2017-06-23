package br.com.javapi.beertime.tsrd.integration;

import org.junit.Assert;
import org.junit.Test;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

public class BusinessDatesFilterTest {

    @Test
    public void testForDays() {
        BusinessDatesFilter filter  = new BusinessDatesFilter();
        
        Assert.assertTrue(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,24-Jun-2017,33.0")));
        Assert.assertTrue(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,25-Jun-2017,33.0")));
        Assert.assertFalse(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,26-Jun-2017,33.0")));
        Assert.assertFalse(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,27-Jun-2017,33.0")));
        Assert.assertFalse(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,28-Jun-2017,33.0")));
        Assert.assertFalse(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,29-Jun-2017,33.0")));
        Assert.assertFalse(filter.reject(InstrumentPriceDTO.fromCSV("Instrument1,30-Jun-2017,33.0")));
    }
}
