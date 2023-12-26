package com.webfluxredis.example

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.webfluxredis.example.config.RedissonConfig
import com.webfluxredis.example.dto.Student
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier

class Lec02KeyValueObjectTest : BaseTest(RedissonConfig()) {

    @Test
    fun keyValueObjectWithMapperTest() {
        val student = Student("Mike", 10, "London", listOf(1, 2, 3))
        val bucket = client.getBucket<String>("student:1", StringCodec.INSTANCE)
        val set = bucket.set(student.toJson())
        val get = bucket.get().map(Student::fromJson).doOnNext(::println).then()
        StepVerifier.create(set.then(get)).verifyComplete()
    }

    @Test
    fun keyValueObjectTest() {
        val student = Student("Mike", 10, "London", listOf(1, 2, 3))
        val bucket = client.getBucket<Student>(
            "student:1",
            TypedJsonJacksonCodec(Student::class.java, Student::class.java, jsonMapper())
        )
        val set = bucket.set(student)
        val get = bucket.get().doOnNext(::println).then()
        StepVerifier.create(set.then(get)).verifyComplete()
    }
}