package it.saimao.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.saimao.easyfood.databinding.ItemPopularMealBinding
import it.saimao.easyfood.model.CategoryMeal

class PopularMealAdapter :
    RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>() {

    private var categoryMealList: List<CategoryMeal> = ArrayList()
    var onItemClicked: ((CategoryMeal) -> Unit)? = null
    var onLongClicked: ((CategoryMeal) -> Unit)? = null

    class PopularMealViewHolder(val binding: ItemPopularMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: CategoryMeal) {
            Glide.with(binding.root).load(meal.strMealThumb).into(binding.ivPopularMeal)
        }

    }

    fun setAdapterList(list: List<CategoryMeal>) {
        this.categoryMealList = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            ItemPopularMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryMealList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        val meal = categoryMealList[position]
        holder.binding.ivPopularMeal.setOnClickListener {
            onItemClicked?.let { it1 -> it1(meal) }
        }
        holder.binding.ivPopularMeal.setOnLongClickListener {
            onLongClicked?.let { it1 ->
                it1(meal)
            }
            true
        }
        holder.bind(meal)
    }
}