package com.bank.operation.domain;

import com.bank.operation.constant.Currency;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String number;
    String displayName;
    BigDecimal amount;
    String mail;
    LocalDateTime createTime;
    Boolean isBlock;
    Boolean isActive;

    @Enumerated(EnumType.STRING)
    Currency currency;


    @PrePersist
    void setUp() {
        number = UUID.randomUUID().toString();
        createTime = LocalDateTime.now();
        isActive = Boolean.TRUE;
        isBlock = Boolean.FALSE;
        amount = BigDecimal.TEN;
    }
}
