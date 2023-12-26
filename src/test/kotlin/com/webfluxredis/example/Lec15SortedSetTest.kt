package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier

class Lec15SortedSetTest : BaseTest(RedissonConfig()) {

    @Test
    fun sortedSet() {
        val sortedSet = client.getScoredSortedSet<String>("student:score", StringCodec.INSTANCE)
        val mono = sortedSet.addScore("sam", 12.25)
            .then(sortedSet.add(23.25, "mike"))
            .then(sortedSet.addScore("jake", 7))
            .then()
        StepVerifier.create(mono).verifyComplete()
        sortedSet.entryRange(0, 1)
            .flatMapIterable { it }
            .map { se -> "${se.score} : ${se.value}" }
            .doOnNext(::println)
        sleep(1_000)
    }
}