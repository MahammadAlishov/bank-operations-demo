package com.bank.operation.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/redis/test")
@RequiredArgsConstructor
public class RedisMockController {

    private final RedisTemplate<Object, Object> redisTemplate;


    @GetMapping
    public Integer sendData(@RequestParam String data) {
        Integer integer = redisTemplate.opsForValue().append("hello", data);
        redisTemplate.opsForList().leftPush("check", data);
        redisTemplate.expire("hello", Duration.ofMinutes(1));
        //redisTemplate.expire("check", Duration.ofMinutes(1));
        return integer;
    }
}
