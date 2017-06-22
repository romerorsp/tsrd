package br.com.javapi.beertime.tsrd.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;
import br.com.javapi.beertime.tsrd.integration.InstrumentPriceListener;

@Component
public abstract class AbstractMeanValuesAlgorithm implements InstrumentPriceListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    protected ApplicationContext context;
    
    protected Map<String, MeanOfValues> history = new HashMap<>();
    
    @Override
    public abstract List<String> getSubscribingChannels();

    @Override
    public abstract boolean isListenerFor(InstrumentPriceDTO dto);
    
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        MessageHeaders headers = message.getHeaders();
        InstrumentPriceDTO payload = InstrumentPriceDTO.class.cast(message.getPayload());
        if(headers.containsKey(Constants.PROCESS_ID)) {
            if(!history.containsKey(headers.get(Constants.PROCESS_ID))) {
                history.put(headers.get(Constants.PROCESS_ID).toString(), context.getBean(MeanOfValues.class));
            }
            history.get(headers.get(Constants.PROCESS_ID)).compute(payload.getValue());
        } else {
            logger.error("There should be a header named [{}] in your message [{}]", Constants.PROCESS_ID, message);
        }
    }
    
    @Scheduled(fixedDelayString="${tsrd.output.schedule.ms:5000}")
    private void summarize() {
        synchronized(Constants.LOGGER_TOKEN) {
            logger.info("");
            logger.info("---------------------------------------------------------------------");
            logger.info("---------------------------------------------------------------------");
            logger.info("COMPUTED INSTRUMENT PRICES - HISTORY ({})", this.getClass().getSimpleName());
            logger.info("---------------------------------------------------------------------");
            
            if(history.isEmpty()) {
                logger.info("NO PROCESS STARTED YET!");
            } else {
                history.forEach((key, value) -> {
                    logger.info("[{}] has [{}] as mean value for [{}] processed messages...",  key, String.format("%,.4f", value.getMeanValue()), value.getCount());
                });
            }
    
            logger.info("---------------------------------------------------------------------");
            logger.info("---------------------------------------------------------------------");
            logger.info("");
        }
    }
}