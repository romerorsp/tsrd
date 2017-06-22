package br.com.javapi.beertime.tsrd.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.support.MessageBuilder;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@RunWith(MockitoJUnitRunner.class)
public class Instrument1AlgorithmTest {

    @InjectMocks
    private Instrument1Algorithm algorithm;
    
    @Mock
    private ApplicationContext context;

    @Test
    public void testMeanCalculation() {
        MeanOfValues mov = new MeanOfValues();
        BDDMockito.given(context.getBean(MeanOfValues.class)).willReturn(mov);
        
        InstrumentPriceDTO dto = InstrumentPriceDTO.fromCSV("Instrument1,01-Jan-1996,27.0");
        Assert.assertTrue(algorithm.isListenerFor(dto));        

        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument1,02-Jan-1996,33.0");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument1,03-Jan-1996,33.9");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        
        Assert.assertEquals(3, mov.getCount());
        Assert.assertEquals(new Double(31.3), Double.valueOf(mov.getMeanValue()));
    }
    
    @Test
    public void testIsListenerFor() {
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("WRONGINSTR,01-Jan-1996,9.222")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT1")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,02-Jan-1996,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument3,02-Jan-1996,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument1SUFFIX,02-Jan-1996,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("PREFIXInstrument1,02-Jan-1996,33.0")));

        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument1,02-Jan-1996,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("InstrUmEnT1,02-Jan-1996,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT1,02-Jan-1996,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("instrument1,02-Jan-1996,33.0")));
    }
    
    @Test
    public void testSubscribingChannel() {
        Assert.assertArrayEquals(new String[] {"algorithm1PubSubChannel"}, algorithm.getSubscribingChannels().toArray());
    }
}
