package com.ak1ena.data

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    val quantity: Float,
    val unit: String
)

@Serializable
data class Recipe(
    val id: Int,
    val name: String,
    val instructions: String,
    val ingredients: List<Ingredient>
)

@Serializable
data class IngredientRequest(
    val name: String,
    val quantity: Float,
    val unit: String
)

@Serializable
data class RecipeRequest(
    val name: String,
    val instructions: String,
    val ingredients: List<IngredientRequest>
)

object RecipeRepository {
    private var nextRecipeId = 3
    private var nextIngredientId = 5

    // Pre-populated data for demonstration
    private val recipes = mutableListOf(
        Recipe(
            id = 1,
            name = "Chicken Pad Thai",
            instructions = "1. Cook noodles. 2. Stir-fry chicken. 3. Mix everything with sauce.",
            ingredients = listOf(
                Ingredient(1, "Rice Noodles", 200f, "grams"),
                Ingredient(2, "Chicken Breast", 150f, "grams"),
                Ingredient(3, "Tofu", 100f, "grams")
            )
        ),
        Recipe(
            id = 2,
            name = "Simple Omelette",
            instructions = "1. Whisk eggs. 2. Pour into hot pan. 3. Cook until firm.",
            ingredients = listOf(
                Ingredient(4, "Eggs", 2f, "large")
            )
        )
    )

    fun getAll(): List<Recipe> = recipes

    fun getById(id: Int?): Recipe? = recipes.find { it.id == id }

    fun add(request: RecipeRequest): Recipe {
        val newIngredients = request.ingredients.map {
            Ingredient(
                id = nextIngredientId++,
                name = it.name,
                quantity = it.quantity,
                unit = it.unit
            )
        }
        val recipe = Recipe(
            id = nextRecipeId++,
            name = request.name,
            instructions = request.instructions,
            ingredients = newIngredients
        )
        recipes.add(recipe)
        return recipe
    }

    fun update(id: Int?, request: RecipeRequest): Boolean {
        val recipeIndex = recipes.indexOfFirst { it.id == id }
        if (recipeIndex == -1) return false

        val updatedIngredients = request.ingredients.map {
            Ingredient(
                id = nextIngredientId++,
                name = it.name,
                quantity = it.quantity,
                unit = it.unit
            )
        }
        recipes[recipeIndex] = Recipe(
            id = id!!,
            name = request.name,
            instructions = request.instructions,
            ingredients = updatedIngredients
        )
        return true
    }

    fun delete(id: Int): Boolean {
        return recipes.removeIf { it.id == id }
    }

    fun searchByIngredient(ingredientName: String): List<Recipe> {
        return recipes.filter { recipe ->
            recipe.ingredients.any { ingredient ->
                ingredient.name.contains(ingredientName, ignoreCase = true)
            }
        }
    }
}