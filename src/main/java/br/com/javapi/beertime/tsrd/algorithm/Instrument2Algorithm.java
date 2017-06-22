package br.com.javapi.beertime.tsrd.algorithm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Component
@EqualsAndHashCode(callSuper=false)
public class Instrument2Algorithm  extends AbstractMeanValuesAlgorithm {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Value("${tsrd.algorithm.n2.date:2014-11-01}")
    private String date;

    @Override
    public List<String> getSubscribingChannels() {
        return Arrays.asList("algorithm2PubSubChannel");
    }

    @Override
    public boolean isListenerFor(InstrumentPriceDTO dto) {
        return "INSTRUMENT2".equalsIgnoreCase(dto.getName()) && dto.getDate().isAfter(LocalDate.parse(date, FORMATTER).minusDays(1));
    }
}