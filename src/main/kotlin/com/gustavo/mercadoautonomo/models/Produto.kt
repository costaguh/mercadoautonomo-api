package com.gustavo.mercadoautonomo.models

import kotlinx.serialization.Serializable

@Serializable
data class Produto(
    val codigo: String,
    val nome: String,
    val preco: Double,
    val imagem: String
)