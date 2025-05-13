package com.gustavo.mercadoautonomo

import com.gustavo.mercadoautonomo.models.Produtos
import com.gustavo.mercadoautonomo.models.Vendas
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val url = System.getenv("DATABASE_URL")
        val driver = "org.postgresql.Driver"

        Database.connect(
            url = url,
            driver = driver
        )

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Produtos, Vendas)
        }
    }
}