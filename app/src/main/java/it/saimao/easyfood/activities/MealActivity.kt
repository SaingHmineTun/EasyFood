package it.saimao.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import it.saimao.easyfood.R
import it.saimao.easyfood.databinding.ActivityMealBinding
import it.saimao.easyfood.fragments.HomeFragment
import it.saimao.easyfood.model.Meal
import it.saimao.easyfood.room.MealDatabase
import it.saimao.easyfood.viewmodel.MealViewModel
import it.saimao.easyfood.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private val binding: ActivityMealBinding by lazy {
        ActivityMealBinding.inflate(layoutInflater)
    }

    private val viewModel: MealViewModel by lazy {
//        ViewModelProvider(this)[MealViewModel::class.java]
        val mealDatabase = MealDatabase.getInstance(context = applicationContext)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
    }

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var meal: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initUi()
        initData()
        initListeners()
    }

    private fun initListeners() {
        binding.ibYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube))
            startActivity(intent)
        }
        binding.fabAddToFavourite.setOnClickListener {
            viewModel.addMealToFavourite(meal)
            Toast.makeText(this, "Add to Favourite Success!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        showDetails(false)
        viewModel.mealDetailLiveData.observe(this) { meal ->
            this.meal = meal
            binding.tvCategory.text = resources.getString(R.string.category_, meal.strCategory)
            binding.tvLocation.text = resources.getString(R.string.area_, meal.strArea)
            binding.tvInstruction.text = meal.strInstructions
            showDetails(true)
        }
        viewModel.getMealDetail(mealId)
    }

    private fun initUi() {
        if (intent != null) {
            mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
            mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
            mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
            Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetail)
            binding.collapsedToolbar.title = mealName
        }
    }

    private fun showDetails(show: Boolean) {
        if (show) {

            binding.progressBar.visibility = View.INVISIBLE
            binding.fabAddToFavourite.visibility = View.VISIBLE
            binding.lyDetail.visibility = View.VISIBLE
            binding.ibYoutube.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.fabAddToFavourite.visibility = View.INVISIBLE
            binding.lyDetail.visibility = View.INVISIBLE
            binding.ibYoutube.visibility = View.INVISIBLE
        }
    }

}