package it.saimao.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.saimao.easyfood.databinding.ItemCategoryBinding
import it.saimao.easyfood.model.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoryList: List<Category> = ArrayList()
    var onItemClicked: ((Category) -> Unit)? = null

    class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setCategoryList(list: List<Category>) {
        this.categoryList = list
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding.tvCategory.text = category.strCategory
        Glide.with(holder.binding.root).load(category.strCategoryThumb)
            .into(holder.binding.ivCategory)
        holder.itemView.setOnClickListener { view ->
            onItemClicked?.let { it -> it(category) }
        }
    }

}