package com.bank.operation.constant;

public final class Constant {

    private Constant() {

    }

    public static final class Kafka {
        public static final String TRANSACTION_KAFKA_TOPIC = "tr_topic";
        public static final String MAIL_KAFKA_TOPIC = "mail_topic";
        public static final String TRANSACTION_KAFKA_GROUP_ID = "transaction_group_id";
        public static final String MAIL_KAFKA_GROUP_ID = "mail_group_id";
    }

    public static final class Redis {
        public static final String TRANSACTION_OTP_KEY = "tr_otp-";
        public static final String SENDER_ACCOUNT_TRANSACTION_KEY = "tr_sender-";
        public static final String RECEIVER_ACCOUNT_TRANSACTION_KEY = "tr_receiver";
    }

    public static final class Message {
        public static final String OTP_MAIL_MESSAGE_SUBJECT = "One Time Password";
        public static final String WRONG_OTP_MESSAGE = "Wrong otp";
        public static final String EXPIRE_OTP_MESSAGE = "Expire otp";
    }

}
