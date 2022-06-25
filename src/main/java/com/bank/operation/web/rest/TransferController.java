package com.bank.operation.web.rest;

import com.bank.operation.dto.request.TransferRequest;
import com.bank.operation.dto.response.TransferResponse;
import com.bank.operation.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public TransferResponse acceptTransfer(@RequestBody TransferRequest transferRequest) {
        return transferService.acceptTransfer(transferRequest);
    }

    @PutMapping("/do/{transactionId}")
    public String doTransfer(@PathVariable("transactionId") Long transactionId, @RequestParam Integer otpCode) {
        return transferService.doTransfer(transactionId, otpCode);
    }

}
