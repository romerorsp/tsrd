package br.com.javapi.beertime.tsrd.integration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import br.com.javapi.beertime.tsrd.Constants;
import br.com.javapi.beertime.tsrd.algorithm.FactorInfluencer;
import br.com.javapi.beertime.tsrd.dto.InstrumentPriceDTO;

@Configuration
public class IntegrationFlowConfiguration {

    private static final String TIME_SERIES_OUTPUT_CHANNEL_NAME = Constants.TIME_SERIES_OUTPUT_CHANNEL_NAME;

    private static final String TIME_SERIES_INPUT_CHANNEL_NAME = Constants.TIME_SERIES_INPUT_CHANNEL_NAME;
    
    @Value("${tsrd.consumer.poll.period:100}")
    private long period;

    @Autowired(required = false)
    private List<InstrumentPriceFilter> filters = Collections.emptyList();
    
    @Autowired(required = false)
    private List<InstrumentPriceListener> listeners = Collections.emptyList();
    
    @Autowired
    private ConfigurableBeanFactory beanFactory;
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private FactorInfluencer influencer;
    
    @PostConstruct
    public void registerChannels() {
        listeners.forEach(listener -> {
            Optional.ofNullable(listener.getSubscribingChannels())
                           .orElseGet(Collections::emptyList)
                           .forEach(subscribingChannel -> {
                                if(!(context.containsBean(subscribingChannel) || context.containsBeanDefinition(subscribingChannel))) {
                                    PublishSubscribeChannel channel = MessageChannels.publishSubscribe(subscribingChannel).minSubscribers(1).get();
                                    channel.subscribe(listener);
                                    beanFactory.registerSingleton(subscribingChannel, channel);
                                } else {
                                    context.getBean(subscribingChannel, PublishSubscribeChannel.class).subscribe(listener);
                                }
                           });
        });
    }
    
    @Bean
    public InstrumentPriceFilter filterUnknown() {
        return dto -> dto == InstrumentPriceDTO.UNKNOWN;
    }
    
    @Bean(name = TIME_SERIES_OUTPUT_CHANNEL_NAME)
    public MessageChannel createTimeSeriesOutputChannel() {
        return MessageChannels.queue().get();
    }
    
    @Bean(name = TIME_SERIES_INPUT_CHANNEL_NAME)
    public MessageChannel createTimeSeriesInputChannel() {
        return MessageChannels.publishSubscribe().get();
    }
    
    @Bean
    public IntegrationFlow timeSeriesProducer(@Autowired @Qualifier(TIME_SERIES_OUTPUT_CHANNEL_NAME) final MessageChannel outputChannel,
                                                                              @Autowired @Qualifier(TIME_SERIES_INPUT_CHANNEL_NAME) final MessageChannel inputChannel) {
        return IntegrationFlows.from(outputChannel)
                                                .transform(InstrumentPriceDTO::fromCSV)
                                                .filter(InstrumentPriceDTO.class, dto -> filters.stream().noneMatch(filter -> filter.reject(dto)))
                                                .transform(InstrumentPriceDTO.class, influencer::influence)
                                                .routeToRecipients(spec -> {
                                                    spec.defaultOutputChannel(inputChannel);
                                                    for(InstrumentPriceListener listener: listeners) {
                                                        Optional.ofNullable(listener.getSubscribingChannels())
                                                                      .orElseGet(Collections::emptyList)
                                                                      .forEach(subscribingChannel -> {
                                                                          spec.recipient(subscribingChannel, value -> InstrumentPriceDTO.class.isInstance(value.getPayload()) &&
                                                                                                                                                      listener.isListenerFor(InstrumentPriceDTO.class.cast(value.getPayload())));
                                                                      });
                                                    }
                                                }).get();
    }
    
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata poller = new PollerMetadata();
        poller.setTrigger(new PeriodicTrigger(period, TimeUnit.MILLISECONDS));
        return poller;
    }
}
