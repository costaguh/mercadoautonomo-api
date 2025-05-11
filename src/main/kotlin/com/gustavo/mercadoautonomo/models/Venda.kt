package com.gustavo.mercadoautonomo.models

import kotlinx.serialization.Serializable

@Serializable
data class Venda(
    val produtos: List<Produto>,
    val total: Double
)