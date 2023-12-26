package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.RBucketReactive
import org.redisson.api.TransactionOptions
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class Lec14TransactionTest : BaseTest(RedissonConfig()) {
    private lateinit var user1Balance: RBucketReactive<Long>
    private lateinit var user2Balance: RBucketReactive<Long>

    @BeforeAll
    fun accountSetup() {
        user1Balance = client.getBucket("user:1:balance", LongCodec.INSTANCE)
        user2Balance = client.getBucket("user:2:balance", LongCodec.INSTANCE)
        val mono = user1Balance.set(100L).then(user2Balance.set(0L)).then()
        StepVerifier.create(mono).verifyComplete()
    }

    @AfterAll
    fun accountBalanceStatus() {
        val mono = Flux.zip(user1Balance.get(), user2Balance.get())
            .doOnNext(::println)
            .then()
        StepVerifier.create(mono).verifyComplete()
    }

    @Test
    fun nonTransactionTest() {
        transfer(user1Balance, user2Balance, 50)
            .thenReturn(0)
            .map { i -> (5 / i) }
            .doOnError(::println)
            .subscribe()
        sleep(1_000)
    }

    @Test
    fun transactionTest() {
        val transaction = client.createTransaction(TransactionOptions.defaults())
        val user1Balance = transaction.getBucket<Long>("user:1:balance", LongCodec.INSTANCE)
        val user2Balance = transaction.getBucket<Long>("user:2:balance", LongCodec.INSTANCE)
        transfer(user1Balance, user2Balance, 50)
            .thenReturn(0)
            .map { i -> (5 / i) }
            .then(transaction.commit())
            .doOnError(::println)
            .doOnError { ex -> transaction.rollback() }
            .subscribe()
        sleep(1_000)
    }

    private fun transfer(
        fromAccount: RBucketReactive<Long>,
        toAccount: RBucketReactive<Long>,
        amount: Int
    ): Mono<Void> {
        return Flux.zip(fromAccount.get(), toAccount.get())
            .filter { it.t1 >= amount }
            .flatMap { t -> fromAccount.set(t.t1 - amount).thenReturn(t) }
            .flatMap { t -> toAccount.set(t.t2 + amount) }
            .then()
    }
}