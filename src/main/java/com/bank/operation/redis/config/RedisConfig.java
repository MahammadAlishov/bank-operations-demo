//package com.bank.operation.redis.redis;
//
//import io.lettuce.core.RedisClient;
//import io.lettuce.core.RedisURI;
//import io.lettuce.core.api.StatefulRedisConnection;
//import io.lettuce.core.api.async.RedisAsyncCommands;
//import io.lettuce.core.api.sync.RedisCommands;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RedisConfig {
//
//
//    @Bean
//    public RedisCommands<String, String> syncRedis() {
//        return redisConnection().sync();
//    }
//
//    @Bean
//    public RedisAsyncCommands<String, String> asyncRedis() {
//        return redisConnection().async();
//    }
//
//
//    private StatefulRedisConnection<String, String> redisConnection() {
//        RedisClient redisClient = RedisClient.create(RedisURI.builder()
//                .withHost("localhost")
//                .withPort(6379)
//                .build());
//        return redisClient.connect();
//    }
//}
