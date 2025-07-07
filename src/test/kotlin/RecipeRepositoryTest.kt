package com.ak1ena.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class RecipeRepositoryTest {

    @Test
    fun `test getAll returns prepopulated recipes`() {
        val allRecipes = RecipeRepository.getAll()
        assertEquals(2, allRecipes.size, "There should be 2 recipes initially")
    }

    @Test
    fun `test getById returns correct recipe`() {
        val recipe = RecipeRepository.getById(1)
        assertNotNull(recipe)
        assertEquals("Chicken Pad Thai", recipe.name)
    }

    @Test
    fun `test add new recipe`() {
        val request = RecipeRequest(
            name = "Banana Smoothie",
            instructions = "1. Blend banana and milk. 2. Add ice and blend again.",
            ingredients = listOf(
                IngredientRequest("Banana", 2f, "pieces"),
                IngredientRequest("Milk", 200f, "ml")
            )
        )

        val newRecipe = RecipeRepository.add(request)
        assertEquals("Banana Smoothie", newRecipe.name)
        assertEquals(2, newRecipe.ingredients.size)
        assertTrue(RecipeRepository.getAll().any { it.id == newRecipe.id })
    }

    @Test
    fun `test update existing recipe`() {
        val request = RecipeRequest(
            name = "Updated Omelette",
            instructions = "1. Beat eggs. 2. Cook slowly on low heat.",
            ingredients = listOf(
                IngredientRequest("Eggs", 3f, "large"),
                IngredientRequest("Cheese", 50f, "grams")
            )
        )

        val updated = RecipeRepository.update(2, request)
        assertTrue(updated, "Recipe should be updated")
        val updatedRecipe = RecipeRepository.getById(2)
        assertEquals("Updated Omelette", updatedRecipe?.name)
        assertEquals(2, updatedRecipe?.ingredients?.size)
    }

    @Test
    fun `test delete recipe`() {
        val allBefore = RecipeRepository.getAll().size
        val result = RecipeRepository.delete(1)
        val allAfter = RecipeRepository.getAll().size

        assertTrue(result, "Delete should return true")
        assertEquals(allBefore - 1, allAfter)
    }

    @Test
    fun `test searchByIngredient finds recipes`() {
        val results = RecipeRepository.searchByIngredient("Egg")
        assertTrue(results.isNotEmpty())
        assertTrue(results.any { it.name.contains("Omelette", ignoreCase = true) })
    }

    @Test
    fun `test searchByIngredient with non-existent ingredient`() {
        val results = RecipeRepository.searchByIngredient("Chocolate")
        assertTrue(results.isEmpty(), "No recipe should contain Chocolate")
    }
}
