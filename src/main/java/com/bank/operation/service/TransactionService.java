package com.bank.operation.service;

import com.bank.operation.domain.Transaction;
import com.bank.operation.dto.request.TransferRequest;
import com.bank.operation.exception.NotFoundException;
import com.bank.operation.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              ModelMapper modelMapper,
                              ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    public Transaction createTransferTransaction(TransferRequest transferRequest) {
        return transactionRepository.save(Transaction.builder()
                .amount(transferRequest.getAmount())
                .receiverAccountNumber(transferRequest.getReceiverAccountNumber())
                .senderAccountNumber(transferRequest.getSenderAccountNumber())
                .build());
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }


    public Transaction findTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException(Transaction.class, transactionId));
    }
}
