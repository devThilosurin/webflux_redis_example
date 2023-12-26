package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.stream.Collectors
import java.util.stream.LongStream

class Lec09ListQueueStackTest : BaseTest(RedissonConfig()) {

    @Test
    fun listTest() {
        val list = client.getList<Long>("number-input", LongCodec.INSTANCE)
//        val listAdd = Flux.range(1, 10)
//            .map(Int::toLong)
//            .flatMap(list::add)
//            .then()
//        StepVerifier.create(listAdd)
//            .verifyComplete()

        val longList = LongStream.rangeClosed(1, 10)
            .boxed()
            .collect(Collectors.toList())

        StepVerifier.create(list.addAll(longList).then())
            .verifyComplete()

        StepVerifier.create(list.size())
            .expectNext(10)
            .verifyComplete()
    }

    @Test
    fun queueTest() {
        val queue = client.getQueue<Long>("number-input", LongCodec.INSTANCE)
        val queuePoll = queue.poll().repeat(3).doOnNext(::println).then()

        StepVerifier.create(queuePoll)
            .verifyComplete()
        StepVerifier.create(queue.size())
            .expectNext(6)
            .verifyComplete()
    }

    @Test
    fun stackTest() {
        val deque = client.getDeque<Long>("number-input", LongCodec.INSTANCE)
        val stackPoll = deque.pollLast().repeat(3).doOnNext(::println).then()

        StepVerifier.create(stackPoll)
            .verifyComplete()
        StepVerifier.create(deque.size())
            .expectNext(2)
            .verifyComplete()
    }
}