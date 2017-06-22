package br.com.javapi.beertime.tsrd.algorithm;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;
import br.com.javapi.beertime.tsrd.repository.InstrumentPriceModifierRepository;

@Component
public class FactorInfluencer {

    private Map<String, Double> modifiers = Collections.emptyMap();
    
    @Autowired
    private InstrumentPriceModifierRepository repository;
    
    public InstrumentPriceDTO influence(InstrumentPriceDTO dto) {
        if(modifiers.containsKey(dto.getName().toUpperCase())) {
            dto.setValue(dto.getValue() * modifiers.get(dto.getName().toUpperCase()));
        }
        return dto;
    }
    
    @Scheduled(fixedRateString="${tsrd.modifier.update.rate.ms:5000}")
    public void refreshInstumentPricesFactors() {
        modifiers = StreamSupport.stream(repository.findAll().spliterator(), false)
                                                      .collect(Collectors.toMap(modifier -> modifier.getName().toUpperCase(), modifier -> modifier.getModifier()));
    }
}
