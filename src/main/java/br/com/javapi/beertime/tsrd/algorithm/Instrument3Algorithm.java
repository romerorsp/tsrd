package br.com.javapi.beertime.tsrd.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;
import br.com.javapi.beertime.tsrd.integration.InstrumentPriceListener;

@Component
public class Instrument3Algorithm implements InstrumentPriceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Instrument3Algorithm.class);

    private Map<String, InstrumentPriceDTO> history = new HashMap<>();

    @Override
    public List<String> getSubscribingChannels() {
        return Arrays.asList("algorithm3PubSubChannel");
    }

    @Override
    public boolean isListenerFor(InstrumentPriceDTO dto) {
        return "INSTRUMENT3".equalsIgnoreCase(dto.getName());
    }
    
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        MessageHeaders headers = message.getHeaders();
        InstrumentPriceDTO payload = InstrumentPriceDTO.class.cast(message.getPayload());
        if(headers.containsKey(Constants.PROCESS_ID)) {
            if((!history.containsKey(headers.get(Constants.PROCESS_ID))) || history.get(headers.get(Constants.PROCESS_ID).toString()).getValue() < payload.getValue()){
                history.put(headers.get(Constants.PROCESS_ID).toString(), payload);
            }
        } else {
            LOGGER.error("There should be a header named [{}] in your message [{}]", Constants.PROCESS_ID, message);
        }
    }
    
    @Scheduled(fixedDelayString="${tsrd.output.schedule.ms:5000}")
    private void summarize() {
        synchronized(Constants.LOGGER_TOKEN) {
            LOGGER.info("");
            LOGGER.info("---------------------------------------------------------------------");
            LOGGER.info("---------------------------------------------------------------------");
            LOGGER.info("COMPUTED INSTRUMENT PRICES - HISTORY ({})", this.getClass().getSimpleName());
            LOGGER.info("---------------------------------------------------------------------");
            
            if(history.isEmpty()) {
                LOGGER.info("NO PROCESS STARTED YET!");
            } else {
                history.forEach((key, value) -> {
                    LOGGER.info("[{}] has [{}] as the biggest price processed in this batch...",  key, value.getValue());
                });
            }
    
            LOGGER.info("---------------------------------------------------------------------");
            LOGGER.info("---------------------------------------------------------------------");
            LOGGER.info("");
        }
    }
}