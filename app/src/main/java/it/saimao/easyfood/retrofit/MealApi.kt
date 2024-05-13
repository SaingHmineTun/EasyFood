package it.saimao.easyfood.retrofit

import it.saimao.easyfood.model.CategoryList
import it.saimao.easyfood.model.CategoryMealList
import it.saimao.easyfood.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<CategoryMealList>

    @GET("categories.php")
    fun getAllCategories(): Call<CategoryList>

    @GET("search.php")
    fun getMealByName(@Query("s") maleName: String): Call<MealList>

}