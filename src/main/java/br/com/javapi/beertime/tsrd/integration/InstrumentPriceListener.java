package br.com.javapi.beertime.tsrd.integration;

import java.util.List;

import org.springframework.messaging.MessageHandler;

import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

public interface InstrumentPriceListener extends MessageHandler {
    
    List<String> getSubscribingChannels();
    
    boolean isListenerFor(InstrumentPriceDTO dto);
}
