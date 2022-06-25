package com.bank.operation.kafka;

import com.bank.operation.constant.Constant;
import com.bank.operation.constant.TransactionStatus;
import com.bank.operation.domain.Account;
import com.bank.operation.domain.Transaction;
import com.bank.operation.dto.request.MailRequest;
import com.bank.operation.redis.service.RedisService;
import com.bank.operation.service.AccountService;
import com.bank.operation.service.MailService;
import com.bank.operation.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumeService {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private final RedisService redisService;
    private final MailService mailService;


    @SneakyThrows
    @KafkaListener(topics = Constant.Kafka.TRANSACTION_KAFKA_TOPIC, groupId = Constant.Kafka.TRANSACTION_KAFKA_GROUP_ID)
    public void receiveTransactionFromKafka(String transactionJson) {

        Transaction transaction = objectMapper.readValue(transactionJson, Transaction.class);
        addAmountToAccount(transaction);

        redisService.deleteKey(Constant.Redis.SENDER_ACCOUNT_TRANSACTION_KEY.concat(transaction.getId().toString()));
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionService.updateTransaction(transaction);
    }

    private void addAmountToAccount(Transaction transaction) {
        Account receiverAccount = accountService.findAccountByNumber(transaction.getReceiverAccountNumber());
        receiverAccount.setAmount(receiverAccount.getAmount().add(transaction.getAmount()));
        accountService.updateAccount(receiverAccount);
    }


    @SneakyThrows
    @KafkaListener(topics = Constant.Kafka.MAIL_KAFKA_TOPIC, groupId = Constant.Kafka.MAIL_KAFKA_GROUP_ID)
    public void receiveMailDataFromKafka(String mailJson) {
        MailRequest mailRequest = objectMapper.readValue(mailJson, MailRequest.class);
        mailService.sendMail(mailRequest);
    }
}
