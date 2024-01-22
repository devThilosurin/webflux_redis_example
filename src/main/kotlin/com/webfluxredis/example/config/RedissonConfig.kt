package com.webfluxredis.example.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {

    @Bean
    fun getClient(): RedissonClient =
        Config().apply {
            useSingleServer().address = "redis://localhost:6379"
        }.let(Redisson::create)

    @Bean
    fun getRedissonClient(): RedissonReactiveClient = getClient().reactive()
}