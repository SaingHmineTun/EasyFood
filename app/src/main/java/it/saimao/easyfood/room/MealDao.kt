package it.saimao.easyfood.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import it.saimao.easyfood.model.Meal

@Dao
interface MealDao {

    @Upsert
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM meals;")
    fun getAllMeals(): LiveData<List<Meal>>

}