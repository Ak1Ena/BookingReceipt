package com.ak1ena

import com.ak1ena.data.RecipeRepository
import com.ak1ena.data.RecipeRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        route("/recipes") {
            get {
                call.respond(RecipeRepository.getAll())
            }

            post {
                val request = call.receive<RecipeRequest>()
                val newRecipe = RecipeRepository.add(request)
                call.respond(HttpStatusCode.Created, newRecipe)
            }

            get("/search") {
                val ingredientName = call.request.queryParameters["ingredient"]
                if (ingredientName.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Missing 'ingredient' query parameter")
                    return@get
                }
                val results = RecipeRepository.searchByIngredient(ingredientName)
                call.respond(results)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val recipe = RecipeRepository.getById(id)
                if (recipe != null) {
                    call.respond(recipe)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val request = call.receive<RecipeRequest>()
                val updated = RecipeRepository.update(id, request)
                if (updated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }
                val deleted = RecipeRepository.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
