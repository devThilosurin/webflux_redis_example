package com.webfluxredis.example.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table
data class Product(
    @Id var id: Int?,
    val description: String,
    val price: Int
)