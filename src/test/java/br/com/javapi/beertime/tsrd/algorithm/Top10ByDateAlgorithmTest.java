package br.com.javapi.beertime.tsrd.algorithm;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.integration.support.MessageBuilder;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@RunWith(MockitoJUnitRunner.class)
public class Top10ByDateAlgorithmTest {

    @InjectMocks
    private Top10ByDateAlgorithm algorithm;
    
    @Test
    public void testTop10Prices() {
        
        InstrumentPriceDTO dto = InstrumentPriceDTO.fromCSV("Instrument3,15-Dec-2013,34.0");
        Assert.assertTrue(algorithm.isListenerFor(dto));        

        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,16-Dec-2013,35.0");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        InstrumentPriceDTO Smallest = dto = InstrumentPriceDTO.fromCSV("Instrument2,17-Dec-2013,33.9");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,18-Dec-2013,33.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,19-Dec-2013,43.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,20-Dec-2013,53.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,21-Dec-2013,63.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,22-Dec-2013,73.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,23-Dec-2013,83.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,24-Dec-2013,93.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,25-Dec-2013,94.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        dto = InstrumentPriceDTO.fromCSV("Instrument2,26-Dec-2013,37.99");
        algorithm.handleMessage(MessageBuilder.withPayload(dto).setHeader(Constants.PROCESS_ID, "example_input.txt-UUID").build());
        
        Assert.assertFalse(algorithm.getHistory().values().contains(Smallest));
        List<InstrumentPriceDTO> list = algorithm.getHistory().values().stream().flatMap(set -> set.stream()).collect(Collectors.toList());
        Assert.assertTrue(list.contains(dto));
    }
    
    @Test
    public void testIsListenerFor() {
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("WRONGINSTR,01-Jan-1996,9.222")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT3")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument1,04-Nov-2014,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument2,14-Nov-2014,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument3SUFFIX,04-Nov-2014,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("PREFIXInstrument4,04-Nov-2014,33.0")));

        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("Instrument3,14-Nov-1914,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("InstrUmEnT3,02-Jan-1917,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("INSTRUMENT3,15-Nov-1914,33.0")));
        Assert.assertTrue(algorithm.isListenerFor(InstrumentPriceDTO.fromCSV("instrument3,02-Jan-1917,33.0")));
    }
    
    @Test
    public void testSubscribingChannel() {
        Assert.assertArrayEquals(new String[] {"timeSeriesInputChannel"}, algorithm.getSubscribingChannels().toArray());
    }
}
