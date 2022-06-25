package com.bank.operation.service;

import com.bank.operation.domain.Account;
import com.bank.operation.dto.request.AccountRequest;
import com.bank.operation.dto.response.AccountResponse;
import com.bank.operation.exception.GeneralException;
import com.bank.operation.exception.NotFoundException;
import com.bank.operation.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    private static final String DEFAULT_FAIL_MESSAGE = "Account can't be transferable because ";

    public AccountService(AccountRepository accountRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }


    public List<AccountResponse> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .collect(Collectors.toList());
    }

    public AccountResponse createAccount(AccountRequest accountRequest) {
        return modelMapper.map(accountRepository
                .save(modelMapper.map(accountRequest, Account.class)), AccountResponse.class);
    }

    public AccountResponse blockAccount(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);

        if (account.getIsBlock()) {
            throw new GeneralException("Account has been already blocked");
        }

        account.setIsBlock(Boolean.TRUE);
        return modelMapper.map(accountRepository.save(account), AccountResponse.class);
    }

    public AccountResponse unBlockAccount(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);

        if (!account.getIsBlock()) {
            throw new GeneralException("Account has been already unBlocked");
        }

        account.setIsBlock(Boolean.FALSE);
        return modelMapper.map(accountRepository.save(account), AccountResponse.class);
    }

    public AccountResponse makeDeActive(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);

        if (!account.getIsActive()) {
            throw new GeneralException("Account has been already deactivated");
        }

        account.setIsActive(Boolean.FALSE);
        return modelMapper.map(accountRepository.save(account), AccountResponse.class);
    }

    public AccountResponse makeActive(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);

        if (account.getIsActive()) {
            throw new GeneralException("Account has been already active");
        }

        account.setIsActive(Boolean.TRUE);
        return modelMapper.map(accountRepository.save(account), AccountResponse.class);
    }


    public Account findTransferableAccount(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);
        checkAccountActiveAndNonBlocked(account);
        return account;
    }

    public Account findMakeTransferAccount(String accountNumber, BigDecimal transferAmount) {
        Account account = findAccountByNumber(accountNumber);
        checkAccountActiveAndNonBlocked(account);
        if (account.getAmount().compareTo(transferAmount) < 0) {
            throw new GeneralException(DEFAULT_FAIL_MESSAGE
                    .concat("your balance : ".concat(account.getAmount().toString())
                            .concat(" expected: ").concat(transferAmount.toString())));
        }
        return account;
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }


    public Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException(Account.class, accountNumber));
    }

    private void checkAccountActiveAndNonBlocked(Account account) {
        if (!account.getIsActive()) {
            throw new GeneralException(AccountService.DEFAULT_FAIL_MESSAGE.concat("deaktived"));
        }

        if (account.getIsBlock()) {
            throw new GeneralException(AccountService.DEFAULT_FAIL_MESSAGE.concat("blocked"));
        }
    }
}
