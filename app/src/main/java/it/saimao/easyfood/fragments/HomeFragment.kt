package it.saimao.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import it.saimao.easyfood.R
import it.saimao.easyfood.activities.CategoryMealActivity
import it.saimao.easyfood.activities.MainActivity
import it.saimao.easyfood.activities.MealActivity
import it.saimao.easyfood.adapters.CategoryAdapter
import it.saimao.easyfood.adapters.PopularMealAdapter
import it.saimao.easyfood.databinding.FragmentHomeBinding
import it.saimao.easyfood.fragments.bottomsheets.MealBottomSheetFragment
import it.saimao.easyfood.model.Meal
import it.saimao.easyfood.viewmodel.HomeViewModel
import kotlinx.coroutines.Job

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealAdapter: PopularMealAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    companion object {
        const val MEAL_ID: String = "meal_id"
        const val MEAL_NAME: String = "meal_name"
        const val MEAL_THUMB: String = "meal_thump"
        const val CATEGORY_NAME: String = "category_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRandomMeal()
        initCategoryMeals() // This is used instead of popular meals because popular meals api is not free of charge
        initCategory()
        initListeners()
    }

    private fun initCategory() {
        categoryAdapter = CategoryAdapter()
        binding.rvCategory.apply {
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
        categoryAdapter.onItemClicked = {
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }

        viewModel.categoryList.observe(viewLifecycleOwner) {
            categoryAdapter.setCategoryList(it)
        }

    }

    private fun initCategoryMeals() {

        popularMealAdapter = PopularMealAdapter()
        binding.rvPopularMeals.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMealAdapter
        }
        viewModel.categoryMealList.observe(viewLifecycleOwner) {
            popularMealAdapter.setAdapterList(it)
        }
        viewModel.getCategoryMeals("Seafood")
        popularMealAdapter.onItemClicked = {
            gotoMealActivity(it.idMeal, it.strMeal, it.strMealThumb)
        }
        popularMealAdapter.onLongClicked = {
            val bottomSheet = MealBottomSheetFragment.newInstance(it.idMeal)
            bottomSheet.show(childFragmentManager, "Meal Information")
        }
    }

    private fun initListeners() {
        binding.cvRandomMeal.setOnClickListener {
            gotoMealActivity(randomMeal.idMeal, randomMeal.strMeal, randomMeal.strMealThumb)
        }
        binding.ibGotoSearchFragment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun gotoMealActivity(id: String, name: String, thumb: String) {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra(MEAL_ID, id)
        intent.putExtra(MEAL_NAME, name)
        intent.putExtra(MEAL_THUMB, thumb)
        startActivity(intent)
    }

    private fun initRandomMeal() {
        viewModel.getRandomMeal()
        viewModel.randomMealLiveData.observe(viewLifecycleOwner) {
            randomMeal = it
            Glide.with(this).load(it.strMealThumb).into(binding.imgRandomMeal)
        }
    }

}