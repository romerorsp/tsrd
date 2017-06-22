package br.com.javapi.beertime.tsrd.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@Configuration
public class UnknownInstrumentPriceConfiguration {
    
    @Bean
    public InstrumentPriceFilter filterUnknown() {
        return dto -> dto == InstrumentPriceDTO.UNKNOWN;
    }
}
