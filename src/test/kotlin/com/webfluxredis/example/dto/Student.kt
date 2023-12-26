package com.webfluxredis.example.dto

import com.fasterxml.jackson.databind.ObjectMapper

data class Student(
    val name: String,
    val age: Int,
    val city: String,
    val marks: List<Int>
) {
    companion object {
        private val mapper = ObjectMapper()

        fun fromJson(json: String): Student =
            mapper.readValue(json, Student::class.java)
    }

    fun toJson(): String = mapper.writeValueAsString(this)
}
