package com.atparui.rmsservice.web.rest;
import java.util.List;
import java.util.Optional;

import com.atparui.rmsservice.broker.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rmsservice-kafka")
public class RmsserviceKafkaResource {

    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private static final Logger LOG = LoggerFactory.getLogger(RmsserviceKafkaResource.class);
    private final KafkaConsumer kafkaConsumer;
    private final StreamBridge streamBridge;

    public RmsserviceKafkaResource(StreamBridge streamBridge, KafkaConsumer kafkaConsumer) {
        this.streamBridge = streamBridge;
        this.kafkaConsumer = kafkaConsumer;
    }

    @PostMapping("/publish")
    public ResponseEntity<Object> publish(@RequestParam("message") String message) {
        LOG.debug("REST request the message : {} to send to Kafka topic", message);
        streamBridge.send(PRODUCER_BINDING_NAME, message);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consume")
    public List<String> consume() {
        LOG.debug("REST request to consume records from Kafka topics");
        return this.kafkaConsumer.getMessages();
    }
}
