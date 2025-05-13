package com.gustavo.mercadoautonomo

import com.gustavo.mercadoautonomo.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "Erro interno: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Buscar produto por código
        get("/produtos/{codigo}") {
            val codigo = call.parameters["codigo"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Código ausente")

            val produto = transaction {
                Produtos.select { Produtos.codigo eq codigo }.singleOrNull()?.let {
                    Produto(
                        codigo = it[Produtos.codigo],
                        nome = it[Produtos.nome],
                        preco = it[Produtos.preco],
                        imagem = it[Produtos.imagem]
                    )
                }
            }

            produto?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
        }

        // Listar todos os produtos
        get("/produtos") {
            val produtos = transaction {
                Produtos.selectAll().map {
                    Produto(
                        codigo = it[Produtos.codigo],
                        nome = it[Produtos.nome],
                        preco = it[Produtos.preco],
                        imagem = it[Produtos.imagem]
                    )
                }
            }
            call.respond(produtos)
        }

        // Adicionar produto novo
        post("/produtos") {
            val novoProduto = call.receive<Produto>()

            val existe = transaction {
                Produtos.select { Produtos.codigo eq novoProduto.codigo }.count() > 0
            }

            if (existe) {
                call.respond(HttpStatusCode.Conflict, "Produto com este código já existe.")
                return@post
            }

            transaction {
                Produtos.insert {
                    it[codigo] = novoProduto.codigo
                    it[nome] = novoProduto.nome
                    it[preco] = novoProduto.preco
                    it[imagem] = novoProduto.imagem
                }
            }

            call.respond(HttpStatusCode.Created, novoProduto)
        }

        // Atualizar produto existente
        put("/produtos/{codigo}") {
            val codigo = call.parameters["codigo"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Código ausente")
            val produtoAtualizado = call.receive<Produto>()

            val linhasAtualizadas = transaction {
                Produtos.update({ Produtos.codigo eq codigo }) {
                    it[nome] = produtoAtualizado.nome
                    it[preco] = produtoAtualizado.preco
                    it[imagem] = produtoAtualizado.imagem
                }
            }

            if (linhasAtualizadas > 0) {
                call.respond(produtoAtualizado)
            } else {
                call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
            }
        }

        // Deletar produto
        delete("/produtos/{codigo}") {
            val codigo = call.parameters["codigo"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Código ausente")

            val removido = transaction {
                Produtos.deleteWhere { Produtos.codigo eq codigo }
            }

            if (removido > 0) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
            }
        }

        // Registrar nova venda
        post("/vendas") {
            val venda = call.receive<Venda>()

            val produtosStr = venda.produtos.joinToString(", ") { it.nome }

            transaction {
                Vendas.insert {
                    it[produtos] = produtosStr
                    it[total] = venda.total
                }
            }

            call.respondText("Venda registrada com sucesso!", status = HttpStatusCode.Created)
        }

        // Listar vendas
        get("/vendas") {
            val vendas = transaction {
                Vendas.selectAll().map {
                    VendaResponse(
                        id = it[Vendas.id],
                        produtos = it[Vendas.produtos],
                        total = it[Vendas.total]
                    )
                }
            }

            call.respond(vendas)
        }
    }
}