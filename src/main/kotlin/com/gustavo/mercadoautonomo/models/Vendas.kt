package com.gustavo.mercadoautonomo.models

import org.jetbrains.exposed.sql.Table

object Vendas : Table("vendas") {
    val id = integer("id").autoIncrement()
    val produtos = text("produtos") // JSON ou string com produtos comprados
    val total = double("total")

    override val primaryKey = PrimaryKey(id)
}