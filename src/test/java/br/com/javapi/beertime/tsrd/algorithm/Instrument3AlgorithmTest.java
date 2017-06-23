package br.com.javapi.beertime.tsrd.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.integration.support.MessageBuilder;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@RunWith(MockitoJUnitRunner.class)
public class Instrument3AlgorithmTest {

    @InjectMocks
    private Instrument3Algorithm algorithm;
    
    @Test
    public void testBiggestPrice() {
        
        InstrumentPriceDTO dto = InstrumentPriceDTO.fromCSV("Instrument3,15-Dec-2013,27.0");
        Assert.assertTrue(algorithm.isListenerFor(dto));        

        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,16-Dec-2013,33.0");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        InstrumentPriceDTO biggest = dto = InstrumentPriceDTO.fromCSV("Instrument2,17-Dec-2013,33.9");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,17-Dec-2013,33.09");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        
        Assert.assertTrue(algorithm.getHistory().values().contains(biggest));
    }
    
    @Test
    public void testIsListenerFor() {
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("WRONGINSTR,01-Jan-1996,9.222")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT3")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument1,04-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,14-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument3SUFFIX,04-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("PREFIXInstrument4,04-Nov-2014,33.0")));

        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument3,14-Nov-1914,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("InstrUmEnT3,02-Jan-1917,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT3,15-Nov-1914,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("instrument3,02-Jan-1917,33.0")));
    }
    
    @Test
    public void testSubscribingChannel() {
        Assert.assertArrayEquals(new String[] {"algorithm3PubSubChannel"}, algorithm.getSubscribingChannels().toArray());
    }
}
