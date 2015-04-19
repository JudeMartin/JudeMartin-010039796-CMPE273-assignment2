package poll.Model;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class ProducerModel {

    private static Producer<String, String> producer;
    private final Properties properties = new Properties();

    public ProducerModel() {
        producerAdd();
    }

    private void producerAdd() {
        createProducer();
        updateProducer();
    }

    private void createProducer() {
        properties.put("metadata.broker.list", "54.149.84.25:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
    }

    private void updateProducer() {
        producer = new Producer<>(new ProducerConfig(properties));
    }

    public void sendMessage(String result) {
        producerSend(result);

    }

    private void producerSend(String result) {
        String topic = "cmpe273-topic";
        KeyedMessage<String, String> data = createMessageToSend(result, topic);
        sendProducerData(data);
    }

    private KeyedMessage<String, String> createMessageToSend(String result, String topic) {
        return (KeyedMessage<String, String>) new KeyedMessage<>(topic, result);
    }

    private void sendProducerData(KeyedMessage<String, String> data) {
        producer.send(data);
    }
}
