package com.webfluxredis.example.repository

import com.webfluxredis.example.entity.Product
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : R2dbcRepository<Product, Int>