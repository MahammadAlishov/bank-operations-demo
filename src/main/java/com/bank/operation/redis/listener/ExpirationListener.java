package com.bank.operation.redis.listener;

import com.bank.operation.constant.Constant;
import com.bank.operation.domain.Account;
import com.bank.operation.domain.Transaction;
import com.bank.operation.redis.service.RedisService;
import com.bank.operation.service.AccountService;
import com.bank.operation.service.TransactionService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class ExpirationListener implements MessageListener {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final RedisService redisService;

    public ExpirationListener(TransactionService transactionService,
                              AccountService accountService,
                              RedisService redisService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.redisService = redisService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String[] keys = new String(message.getBody()).split("-");
        Long transactionId = Long.valueOf(keys[keys.length - 1]);

        Transaction transaction = transactionService
                .findTransactionById(transactionId);

        returnAmountToAccount(transaction);
        redisService.deleteKey(Constant.Redis.SENDER_ACCOUNT_TRANSACTION_KEY
                .concat(transactionId.toString()));
    }


    private void returnAmountToAccount(Transaction transaction) {
        Account senderAccount = accountService.findAccountByNumber(transaction.getSenderAccountNumber());
        senderAccount.setAmount(senderAccount.getAmount().add(transaction.getAmount()));
        accountService.updateAccount(senderAccount);
    }
}
