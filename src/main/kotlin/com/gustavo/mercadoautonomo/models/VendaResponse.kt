package com.gustavo.mercadoautonomo.models

import kotlinx.serialization.Serializable

@Serializable
data class VendaResponse(
    val id: Int,
    val produtos: String,
    val total: Double
)