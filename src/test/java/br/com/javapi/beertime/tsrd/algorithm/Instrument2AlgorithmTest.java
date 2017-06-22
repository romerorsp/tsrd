package br.com.javapi.beertime.tsrd.algorithm;

import org.junit.Assert;
import org.junit.Before;
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
public class Instrument2AlgorithmTest {

    @InjectMocks
    private Instrument2Algorithm algorithm;
    
    @Mock
    private ApplicationContext context;

    @Before
    public void setup() {
        algorithm.setDate("2014-11-14");
    }
    
    @Test
    public void testMeanCalculation() {
        MeanOfValues mov = new MeanOfValues();
        BDDMockito.given(context.getBean(MeanOfValues.class)).willReturn(mov);
        
        InstrumentPriceDTO dto = InstrumentPriceDTO.fromCSV("Instrument2,15-Nov-2014,27.0");
        Assert.assertTrue(algorithm.isListenerFor(dto));        

        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,16-Nov-2014,33.0");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,17-Nov-2014,33.9");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        
        Assert.assertEquals(3, mov.getCount());
        Assert.assertEquals(new Double(31.3), Double.valueOf(mov.getMeanValue()));
    }
    
    @Test
    public void testIsListenerFor() {
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("WRONGINSTR,01-Jan-1996,9.222")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT2")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument1,14-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument3,14-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2SUFFIX,14-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("PREFIXInstrument2,14-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,13-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,12-Nov-2014,33.0")));
        Assert.assertFalse(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,01-Jan-2014,33.0")));

        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,14-Nov-2014,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("InstrUmEnT2,02-Jan-2017,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT2,15-Nov-2014,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("instrument2,02-Jan-2017,33.0")));
    }
    
    @Test
    public void testSubscribingChannel() {
        Assert.assertArrayEquals(new String[] {"algorithm2PubSubChannel"}, algorithm.getSubscribingChannels().toArray());
    }
}
