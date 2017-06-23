package br.com.javapi.beertime.tsrd.algorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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
import lombok.Data;

@Data
@Component
public class Top10ByDateAlgorithm implements InstrumentPriceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Top10ByDateAlgorithm.class);
    
    private static final Comparator<InstrumentPriceDTO> COMPARATOR = (dto1, dto2) -> dto1.getDate().compareTo(dto2.getDate());

    private Map<String, TreeSet<InstrumentPriceDTO>> history = new HashMap<>();

    @Override
    public List<String> getSubscribingChannels() {
        return Arrays.asList("timeSeriesInputChannel");
    }

    @Override
    public boolean isListenerFor(InstrumentPriceDTO dto) {
        return true;
    }
    
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        MessageHeaders headers = message.getHeaders();
        InstrumentPriceDTO payload = InstrumentPriceDTO.class.cast(message.getPayload());
        if(headers.containsKey(Constants.PROCESS_ID)) {
            if(!history.containsKey(headers.get(Constants.PROCESS_ID))) {
                history.put(headers.get(Constants.PROCESS_ID).toString(), new TreeSet<>(COMPARATOR));
            }
            TreeSet<InstrumentPriceDTO> top10 = history.get(headers.get(Constants.PROCESS_ID).toString());
            if(top10.size() < 10) {
                top10.add(payload);
            } else {
                InstrumentPriceDTO smaller = top10.first();
                if(smaller.getValue() <= payload.getValue()) {
                    top10.remove(smaller);
                    top10.add(payload);
                }
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
                    LOGGER.info("[{}] has [{}] as sum value for the latest 10 processed (any other) instruments...",  key, value.stream().map(InstrumentPriceDTO::getValue).reduce((d1, d2) -> d1 + d2).get());
                });
            }
    
            LOGGER.info("---------------------------------------------------------------------");
            LOGGER.info("---------------------------------------------------------------------");
            LOGGER.info("");
        }
    }
}