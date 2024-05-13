package it.saimao.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.saimao.easyfood.databinding.ItemCategoryMealBinding
import it.saimao.easyfood.model.Meal

class FavouriteMealAdapter : RecyclerView.Adapter<FavouriteMealAdapter.FavouriteMealViewHolder>() {

    private var favouriteMeals: List<Meal> = ArrayList()
    var favouriteMealClicked: ((Meal) -> Unit)? = null
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    class FavouriteMealViewHolder(val binding: ItemCategoryMealBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setAdapterList(list: List<Meal>) {
        this.favouriteMeals = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealViewHolder {
        return FavouriteMealViewHolder(
            ItemCategoryMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: FavouriteMealViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.itemView.setOnClickListener {
            favouriteMealClicked?.invoke(meal)
        }
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.ivMeal)
        holder.binding.tvMeal.text = meal.strMeal
    }
}