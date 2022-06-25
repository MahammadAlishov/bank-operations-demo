package com.bank.operation.dto.request;


import com.bank.operation.constant.Currency;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String displayName;
    Currency currency;
    String mail;
}
