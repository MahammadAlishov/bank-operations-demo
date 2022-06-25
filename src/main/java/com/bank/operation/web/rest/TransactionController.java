package com.bank.operation.web.rest;

import com.bank.operation.dto.response.TransactionResponse;
import com.bank.operation.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


//    @GetMapping("/history/out-flow/{accountNumber}")
//    public List<TransactionResponse> getOutFlowHistoryByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
//        return transactionService.getOutFlowHistoryByAccountNumber(accountNumber, 0, 100);
//    }

}
