package br.com.javapi.beertime.tsrd.algorithm;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;
import br.com.javapi.beertime.tsrd.entity.InstrumentPriceModifier;
import br.com.javapi.beertime.tsrd.repository.InstrumentPriceModifierRepository;

@RunWith(MockitoJUnitRunner.class)
public class FactorInfluencerTest {

    @InjectMocks
    private FactorInfluencer influencer;
    
    @Mock
    private InstrumentPriceModifierRepository repository;

    @Test
    public void testInfluence() {
        InstrumentPriceModifier modifier = new InstrumentPriceModifier();
        modifier.setId(1);
        modifier.setName("Instrument1");
        modifier.setModifier(3.0);
        BDDMockito.given(repository.findAll()).willReturn(Arrays.asList(modifier));
        
        influencer.refreshInstumentPricesFactors();
        
        InstrumentPriceDTO dto1 = influencer.influence(InstrumentPriceDTO.fromCSV("Instrument1,02-Jan-1996,33.0")),
                                           dto2 = influencer.influence(InstrumentPriceDTO.fromCSV("Instrument2,02-Jan-1996,33.0"));
        
        Assert.assertEquals(new Double(99.0), Double.valueOf(dto1.getValue()));
        Assert.assertEquals(new Double(33.0), Double.valueOf(dto2.getValue()));
    }
}
