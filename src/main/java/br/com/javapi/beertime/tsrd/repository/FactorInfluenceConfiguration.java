
package br.com.javapi.beertime.tsrd.repository;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.entity.InstrumentPriceModifier;

@Configuration
public class FactorInfluenceConfiguration {

    @Autowired
    private InstrumentPriceModifierRepository repository;
    
    @PostConstruct
    public void init() {
        Optional.ofNullable(System.getenv(Constants.FACTOR_INFLUENCE_ENV_NAME))
                      .map(influence -> Arrays.stream(influence.split("[,]"))
                                                                .map(modifier -> {
                                                                    String[] entry = modifier.split("[\\=]");
                                                                    String instrument = entry[0];
                                                                    double factor = Double.valueOf(entry[1]);
                                                                    InstrumentPriceModifier ipm = new InstrumentPriceModifier();
                                                                    ipm.setModifier(factor);
                                                                    ipm.setName(instrument);
                                                                    return ipm;
                                                                }).collect(Collectors.toList()))
                      .ifPresent(repository::save);
    }
}
