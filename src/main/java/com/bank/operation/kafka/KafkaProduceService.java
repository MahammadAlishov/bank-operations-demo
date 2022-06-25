package com.bank.operation.kafka;

import com.bank.operation.constant.Constant;
import com.bank.operation.domain.Transaction;
import com.bank.operation.dto.request.MailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProduceService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @SneakyThrows
    public void sendTransferTopic(Transaction transaction) {
        kafkaTemplate.send(Constant.Kafka.TRANSACTION_KAFKA_TOPIC, objectMapper.writeValueAsString(transaction));
    }

    @SneakyThrows
    public void sendMailTopic(MailRequest mailRequest) {
        kafkaTemplate.send(Constant.Kafka.MAIL_KAFKA_TOPIC, objectMapper.writeValueAsString(mailRequest));
    }
}
