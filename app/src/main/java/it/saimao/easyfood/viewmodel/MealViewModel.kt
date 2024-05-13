package it.saimao.easyfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.saimao.easyfood.model.Meal
import it.saimao.easyfood.model.MealList
import it.saimao.easyfood.retrofit.RetrofitInstance
import it.saimao.easyfood.room.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MealViewModel(val mealDatabase: MealDatabase) : ViewModel() {
    private var _mealDetailLiveData = MutableLiveData<Meal>()
    val mealDetailLiveData: LiveData<Meal>
        get() = _mealDetailLiveData

    fun getMealDetail(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    _mealDetailLiveData.value = response.body()!!.meals[0]
                } else {
                    return

                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Kham", t.toString())
            }

        })
    }

    fun addMealToFavourite(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    fun deleteMealFromFavourite(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

}