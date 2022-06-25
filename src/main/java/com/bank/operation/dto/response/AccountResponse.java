package com.bank.operation.dto.response;

import com.bank.operation.constant.Currency;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Long id;
    String number;
    String displayName;
    BigDecimal amount;
    LocalDateTime createTime;
    Boolean isBlock;
    Boolean isActive;
    Currency currency;
}
