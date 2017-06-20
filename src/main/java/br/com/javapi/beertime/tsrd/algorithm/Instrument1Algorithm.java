package br.com.javapi.beertime.tsrd.algorithm;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@Component
public class Instrument1Algorithm extends AbstractMeanValuesAlgorithm {

    @Override
    public List<String> getSubscribingChannels() {
        return Arrays.asList("algorithm1PubSubChannel");
    }

    @Override
    public boolean isListenerFor(InstrumentPriceDTO dto) {
        return "INSTRUMENT1".equalsIgnoreCase(dto.getName());
    }
}
