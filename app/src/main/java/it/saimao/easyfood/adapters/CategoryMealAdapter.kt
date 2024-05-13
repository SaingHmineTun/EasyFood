package it.saimao.easyfood.adapters

import android.media.RouteListingPreference.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.saimao.easyfood.databinding.ItemCategoryMealBinding
import it.saimao.easyfood.model.CategoryMeal

class CategoryMealAdapter : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {

    private var categoryMeals: List<CategoryMeal> = ArrayList()
    var categoryMealItemClicked: ((CategoryMeal) -> Unit)? = null
    var categoryMealOnLongClicked: ((CategoryMeal) -> Unit)? = null

    class CategoryMealViewHolder(val binding: ItemCategoryMealBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setCategoryMeals(list: List<CategoryMeal>) {
        this.categoryMeals = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(
            ItemCategoryMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryMeals.size
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        val categoryMeal = categoryMeals[position]
        holder.binding.tvMeal.text = categoryMeal.strMeal
        holder.itemView.setOnClickListener {
            categoryMealItemClicked?.invoke(categoryMeal)
        }
        holder.itemView.setOnLongClickListener {
            categoryMealOnLongClicked?.invoke(categoryMeal)
            true
        }
        Glide.with(holder.itemView).load(categoryMeal.strMealThumb).into(holder.binding.ivMeal)
    }


}