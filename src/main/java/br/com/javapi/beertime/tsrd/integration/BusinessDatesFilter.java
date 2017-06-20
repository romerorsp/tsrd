package br.com.javapi.beertime.tsrd.integration;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@Component
public class BusinessDatesFilter implements InstrumentPriceFilter {

    @Override
    public boolean reject(InstrumentPriceDTO dto) {
        return dto.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || dto.getDate().getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
