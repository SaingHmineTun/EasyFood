package it.saimao.easyfood.fragments.bottomsheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.saimao.easyfood.activities.MainActivity
import it.saimao.easyfood.activities.MealActivity
import it.saimao.easyfood.databinding.FragmentMealBottomSheetBinding
import it.saimao.easyfood.fragments.HomeFragment
import it.saimao.easyfood.model.Meal
import it.saimao.easyfood.viewmodel.HomeViewModel

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var meal: Meal
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        arguments?.let {
            mealId = it.getString(HomeFragment.MEAL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListener()
    }

    private fun initListener() {
        binding.root.setOnClickListener {
            if (::meal.isInitialized) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, meal.idMeal)
                    putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
                    putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
                }
                startActivity(intent)

            }
        }

    }

    private fun initData() {
        viewModel.bottomSheetMeal.observe(viewLifecycleOwner) {
            meal = it
            Glide.with(this).load(it.strMealThumb).into(binding.ivMealPhoto)
            binding.tvMealName.text = it.strMeal
            binding.tvArea.text = it.strArea
            binding.tvCategory.text = it.strCategory
        }
        mealId?.let {
            viewModel.setBottomSheetMeal(it)
        }
    }

    companion object {
        fun newInstance(param: String?): MealBottomSheetFragment =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(HomeFragment.MEAL_ID, param)
                }
            }
    }

}