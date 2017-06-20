package br.com.javapi.beertime.tsrd.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class InstrumentPriceDTO {

    public static final InstrumentPriceDTO UNKNOWN = new InstrumentPriceDTO("UNKNOWN", LocalDate.now(), -1);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

    private String name;

    private LocalDate date;

    private double value;

    private InstrumentPriceDTO(String name, LocalDate date, double value){
        this.name = name;
        this.date = date;
        this.value = value;
    }

    public static InstrumentPriceDTO fromCSV(String csv) {
        String[] split = csv.split("[,]");
        return split.length < 3? InstrumentPriceDTO.UNKNOWN: new InstrumentPriceDTO(split[0], LocalDate.parse(split[1], FORMATTER), Double.valueOf(split[2]));
    }
}