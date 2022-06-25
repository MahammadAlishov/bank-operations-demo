package com.bank.operation.domain;

import com.bank.operation.constant.TransactionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String senderAccountNumber;
    String receiverAccountNumber;
    BigDecimal amount;
    String failDescription;

    @CreationTimestamp
    LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    TransactionStatus status;

    @PrePersist
    void setUp() {
        this.status = TransactionStatus.CREATED;
    }
}
