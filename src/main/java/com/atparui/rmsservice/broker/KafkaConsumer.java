package com.atparui.rmsservice.broker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer implements Consumer<String> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private List<String> messages = Collections.synchronizedList(new ArrayList<>());

    public List<String> getMessages() {
        return new ArrayList<>(this.messages);
    }

    @Override
    public void accept(String input) {
        LOG.debug("Got message from kafka stream: {}", input);
        messages.add(input);
    }
}
