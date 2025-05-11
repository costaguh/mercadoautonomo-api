package com.gustavo.mercadoautonomo

import com.gustavo.mercadoautonomo.models.Venda
import com.gustavo.mercadoautonomo.models.Produto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/produtos/{codigo}") {
            val codigo = call.parameters["codigo"]

            val produtos = listOf(
                Produto("7891000055123", "Pipoca Doce Nhac", 1.00, "pipoca.png"),
                Produto("7896004007010", "Amendoim Japonês", 0.50, "amendoim.png")
            )

            val produto = produtos.find { it.codigo == codigo }

            if (produto != null) {
                call.respond(produto)
            } else {
                call.respondText("Produto não encontrado", status = HttpStatusCode.NotFound)
            }
        }

        post("/vendas") {
            val venda = call.receive<Venda>()

            println("Venda recebida:")
            println("Produtos: ${venda.produtos.map { it.nome }}")
            println("Total: R$ ${venda.total}")

            call.respondText("Venda registrada com sucesso!", status = HttpStatusCode.Created)
        }
    }
}
