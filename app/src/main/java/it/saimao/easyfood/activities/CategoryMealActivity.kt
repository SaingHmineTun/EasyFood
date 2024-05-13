package it.saimao.easyfood.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import it.saimao.easyfood.adapters.CategoryMealAdapter
import it.saimao.easyfood.databinding.ActivityCategoryMealBinding
import it.saimao.easyfood.fragments.HomeFragment
import it.saimao.easyfood.viewmodel.CategoryMealViewModel

class CategoryMealActivity : AppCompatActivity() {

    private val binding: ActivityCategoryMealBinding by lazy {
        ActivityCategoryMealBinding.inflate(layoutInflater)
    }

    private val viewModel: CategoryMealViewModel by lazy {
        ViewModelProvider(this)[CategoryMealViewModel::class.java]
    }

    private lateinit var categoryName: String
    private lateinit var categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initIntentData()
        initUi()
        initData()
    }

    private fun initData() {
        viewModel.categoryMealList.observe(this) { categoryMeals ->
            binding.tvCategoryCount.text = "$categoryName (${categoryMeals.size})"
            categoryMealAdapter.setCategoryMeals(categoryMeals)
        }
        viewModel.getMealsByCategory(categoryName)
    }

    private fun initUi() {
        categoryMealAdapter = CategoryMealAdapter()
        categoryMealAdapter.categoryMealItemClicked = {
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
        binding.rvCategoryMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }

    private fun initIntentData() {
        if (intent != null) {
            categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        }
    }
}