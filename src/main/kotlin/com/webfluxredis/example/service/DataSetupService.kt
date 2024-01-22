package com.webfluxredis.example.service

import com.webfluxredis.example.entity.Product
import com.webfluxredis.example.repository.ProductRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.Resource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import reactor.core.publisher.Flux
import java.util.concurrent.ThreadLocalRandom

@Service
class DataSetupService(
    private val productRepo: ProductRepository,
    private val entityTemplate: R2dbcEntityTemplate
) : CommandLineRunner {

    @Value("classpath:schema.sql")
    private lateinit var resource: Resource

    override fun run(vararg args: String?) {
        val query = StreamUtils.copyToString(resource.inputStream, Charsets.UTF_8)
        println(query)

        val insert = Flux.range(1, 1000)
            .map { i ->
                Product(
                    id = null,
                    description = "product $i",
                    price = ThreadLocalRandom.current().nextInt(1, 100)
                )
            }
            .collectList()
            .flatMapMany(productRepo::saveAll)
            .then()

        entityTemplate.databaseClient
            .sql(query)
            .then()
            .then(insert)
            .doFinally { s -> println("Data setup completed $s") }
            .subscribe()
    }
}