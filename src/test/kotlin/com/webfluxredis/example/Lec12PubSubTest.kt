package com.webfluxredis.example

import com.webfluxredis.example.config.RedissonConfig
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec

class Lec12PubSubTest : BaseTest(RedissonConfig()) {

    @Test
    fun subscriber1() {
        val topic = client.getTopic("slack-room1", StringCodec.INSTANCE)
        topic.getMessages(String::class.java)
            .doOnError(::println)
            .doOnNext(::println)
            .subscribe()
        sleep(600_000)
    }

    @Test
    fun subscriber2() {
        val patternTopic = client.getPatternTopic("slack-room*", StringCodec.INSTANCE)
        patternTopic.addListener(String::class.java) { channel, topic, msg ->
            println("channel: $channel, topic: $topic, msg: $msg")
        }.subscribe()
        sleep(600_000)
    }
}