package com.bank.operation.service;

import com.bank.operation.constant.Constant;
import com.bank.operation.constant.TransactionStatus;
import com.bank.operation.domain.Account;
import com.bank.operation.domain.Transaction;
import com.bank.operation.dto.request.MailRequest;
import com.bank.operation.dto.request.TransferRequest;
import com.bank.operation.dto.response.TransferResponse;
import com.bank.operation.exception.GeneralException;
import com.bank.operation.kafka.KafkaProduceService;
import com.bank.operation.redis.service.RedisService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public class TransferService {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final KafkaProduceService kafkaProduceService;
    private final RedisService redisService;


    public TransferService(AccountService accountService,
                           TransactionService transactionService,
                           KafkaProduceService kafkaProduceService,
                           RedisService redisService, MailService mailService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.kafkaProduceService = kafkaProduceService;
        this.redisService = redisService;
    }


    public TransferResponse acceptTransfer(TransferRequest transferRequest) {
        Transaction transferTransaction = transactionService.createTransferTransaction(transferRequest);
        try {
            Account senderAccount = verifyAccountsAndGetSenderAccount(transferRequest);
            Integer otp = otpGenerator();

            kafkaProduceService.sendMailTopic(MailRequest.builder()
                    .mail(senderAccount.getMail())
                    .subject(Constant.Message.OTP_MAIL_MESSAGE_SUBJECT)
                    .data(otp.toString())
                    .build());

            redisService.writeTransferOtp(transferTransaction.getId(), otp);

            blockSenderAmount(transferTransaction, senderAccount);
            transferTransaction.setStatus(TransactionStatus.OTP_SENT);
            transactionService.updateTransaction(transferTransaction);
            return TransferResponse.builder()
                    .transactionId(transferTransaction.getId())
                    .build();
        } catch (RuntimeException ex) {
            transferTransaction.setFailDescription(ex.getMessage());
            transferTransaction.setStatus(TransactionStatus.FAIL);
            transactionService.updateTransaction(transferTransaction);
            throw ex;
        }
    }

    private Account verifyAccountsAndGetSenderAccount(TransferRequest transferRequest) {
        Account senderAccount = accountService
                .findMakeTransferAccount(transferRequest.getSenderAccountNumber(),
                        transferRequest.getAmount());
        accountService
                .findTransferableAccount(transferRequest.getReceiverAccountNumber());

        return senderAccount;
    }

    public String doTransfer(Long transactionId, Integer otpCode) {
        Integer otp = redisService.getTransferOtpById(transactionId);
        Transaction transaction = transactionService.findTransactionById(transactionId);

        if (Objects.isNull(otp)) {
            updateFailTransaction(transaction, "Otp Expired", TransactionStatus.EXPIRE_OTP);
        } else if (!otp.equals(otpCode)) {
            redisService.manualReturnBlockAmount(Constant.Redis.TRANSACTION_OTP_KEY);
            updateFailTransaction(transaction,
                    Constant.Message.EXPIRE_OTP_MESSAGE.concat(otp.toString()),
                    TransactionStatus.WRONG_OTP);
        }
        redisService.deleteKey(Constant.Redis.TRANSACTION_OTP_KEY.concat(transactionId.toString()));
        transaction.setStatus(TransactionStatus.SENT_TO_KAFKA);
        transactionService.updateTransaction(transaction);
        kafkaProduceService.sendTransferTopic(transaction);
        return "Transfer will send";
    }

    private void updateFailTransaction(Transaction transaction, String failKeyword,
                                       TransactionStatus transactionStatus) {
        GeneralException exception = new GeneralException(failKeyword.concat(transaction.getId().toString()));
        transaction.setStatus(transactionStatus);
        transaction.setFailDescription(exception.getMessage());
        transactionService.updateTransaction(transaction);
        throw exception;
    }

    private Integer otpGenerator() {
        Random randomNum = new Random();
        return 1000 + randomNum.nextInt(4000);
    }

    private void blockSenderAmount(Transaction transaction, Account senderAccount) {
        senderAccount.setAmount(senderAccount.getAmount().subtract(transaction.getAmount()));
        redisService.blockSenderAccountAmount(transaction.getId(), transaction.getAmount());
        accountService.updateAccount(senderAccount);
    }
}
