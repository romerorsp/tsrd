package br.com.javapi.beertime.tsrd.algorithm;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@Component
public class Instrument2Algorithm  extends AbstractMeanValuesAlgorithm {

    @Override
    public List<String> getSubscribingChannels() {
        return Arrays.asList("algorithm2PubSubChannel");
    }

    @Override
    public boolean isListenerFor(InstrumentPriceDTO dto) {
        return "INSTRUMENT2".equalsIgnoreCase(dto.getName()) && dto.getDate().isAfter(LocalDate.of(2014, 11, 1).minusDays(1));
    }
}