package com.bank.operation.redis.service;

import com.bank.operation.constant.Constant;
import com.bank.operation.domain.Account;
import com.bank.operation.domain.Transaction;
import com.bank.operation.service.AccountService;
import com.bank.operation.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final TransactionService transactionService;
    private final AccountService accountService;

    public void writeTransferOtp(Long transactionId, Integer otp) {
        String redisKey = Constant.Redis.TRANSACTION_OTP_KEY.concat(transactionId.toString());
        redisTemplate.opsForValue().set(redisKey, otp);
        redisTemplate.expire(redisKey, Duration.ofMinutes(2));
    }

    public Integer getTransferOtpById(Long transactionId) {
        return (Integer) redisTemplate.opsForValue()
                .get(Constant.Redis.TRANSACTION_OTP_KEY.concat(transactionId.toString()));
    }

    public void blockSenderAccountAmount(Long transactionId, BigDecimal amount) {
        redisTemplate.opsForValue().set(Constant.Redis.SENDER_ACCOUNT_TRANSACTION_KEY
                .concat(transactionId.toString()), amount);
    }


    public void deleteKey(String deleteKey) {
        redisTemplate.delete(deleteKey);
    }

    public void manualReturnBlockAmount(String key) {
        String[] keyParts = key.split("-");
        Long transactionId = Long.valueOf(keyParts[keyParts.length - 1]);

        Transaction transaction = transactionService
                .findTransactionById(transactionId);

        returnAmountToAccount(transaction);
        deleteKey(Constant.Redis.SENDER_ACCOUNT_TRANSACTION_KEY
                .concat(transactionId.toString()));
        deleteKey(key);
    }

    private void returnAmountToAccount(Transaction transaction) {
        Account senderAccount = accountService.findAccountByNumber(transaction.getSenderAccountNumber());
        senderAccount.setAmount(senderAccount.getAmount().add(transaction.getAmount()));
        accountService.updateAccount(senderAccount);
    }
}
