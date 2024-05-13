package it.saimao.easyfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.saimao.easyfood.model.CategoryMeal
import it.saimao.easyfood.model.CategoryMealList
import it.saimao.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel : ViewModel() {

    private var _categoryMealList = MutableLiveData<List<CategoryMeal>>()
    val categoryMealList: LiveData<List<CategoryMeal>>
        get() = _categoryMealList

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName)
            .enqueue(object : Callback<CategoryMealList> {
                override fun onResponse(
                    call: Call<CategoryMealList>,
                    response: Response<CategoryMealList>
                ) {
                    if (response.body() != null) {
                        _categoryMealList.postValue(response.body()!!.meals)
                    }
                }

                override fun onFailure(call: Call<CategoryMealList>, t: Throwable) {
                    Log.e("Category Meal View Model", t.message.toString())
                }

            })
    }

}