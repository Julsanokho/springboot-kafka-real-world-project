package com.sanokho.kafka;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
public class WikimediaChangesProducer {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(WikimediaChangesProducer.class);
    private KafkaTemplate <String, String> kafkaTemplate;


    public void sendMessage() throws InterruptedException {

        String topic = "wikimedia_recentchange";

        // To read time stream data from wikimedia, we use event source
        EventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);
    }

}
