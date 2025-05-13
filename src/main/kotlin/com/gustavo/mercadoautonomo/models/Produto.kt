package com.gustavo.mercadoautonomo.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
@Serializable
data class Produto(
    val codigo: String,
    val nome: String,
    val preco: Double,
    val imagem: String
)

object Produtos : Table("produtos") {
    val codigo = varchar("codigo", 50)
    val nome = varchar("nome", 255)
    val preco = double("preco")
    val imagem = varchar("imagem", 255)

    override val primaryKey = PrimaryKey(codigo)
}