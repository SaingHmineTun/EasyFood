package it.saimao.easyfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.saimao.easyfood.model.Category
import it.saimao.easyfood.model.CategoryList
import it.saimao.easyfood.model.CategoryMeal
import it.saimao.easyfood.model.CategoryMealList
import it.saimao.easyfood.model.Meal
import it.saimao.easyfood.model.MealList
import it.saimao.easyfood.retrofit.RetrofitInstance
import it.saimao.easyfood.room.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
    private var _randomMealLiveData = MutableLiveData<Meal>()
    val randomMealLiveData: LiveData<Meal> = _randomMealLiveData
    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    // Get random meal
                    _randomMealLiveData.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Kham", t.message.toString())
            }

        })
    }

    private var _categoryMealsList = MutableLiveData<List<CategoryMeal>>()
    val categoryMealList: LiveData<List<CategoryMeal>>
        get() = _categoryMealsList

    fun getCategoryMeals(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName)
            .enqueue(object : Callback<CategoryMealList> {
                override fun onResponse(
                    call: Call<CategoryMealList>,
                    response: Response<CategoryMealList>
                ) {
                    if (response.body() != null) {
                        _categoryMealsList.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<CategoryMealList>, t: Throwable) {
                    Log.d("Kham", t.message.toString())
                }

            })

    }

    private var _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>>
        get() = _categoryList

    private fun getAllCategory() {
        RetrofitInstance.api.getAllCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    _categoryList.value = response.body()!!.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Kham", t.message.toString())
            }

        })
    }

    init {
        getAllCategory()
    }

    val favouriteMeals = mealDatabase.mealDao().getAllMeals()
    fun removeFromFavourite(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun addToFavourite(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    private var _bottomSheetMeal = MutableLiveData<Meal>()
    val bottomSheetMeal: LiveData<Meal>
        get() = _bottomSheetMeal

    fun setBottomSheetMeal(mealId: String) {
        RetrofitInstance.api.getMealDetails(mealId).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    _bottomSheetMeal.postValue(response.body()!!.meals[0])
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("Kham", "Error on call setBottmSheetMeal\n${t.message.toString()}")
            }

        })
    }


    private var _searchMeal = MutableLiveData<List<Meal>>()
    val searchMeal: LiveData<List<Meal>>
        get() = _searchMeal

    fun searchMealByName(mealName: String) {
        RetrofitInstance.api.getMealByName(mealName).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.let {
                    _searchMeal.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("Kham", t.message.toString())
            }

        })
    }

}