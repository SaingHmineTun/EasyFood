package it.saimao.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import it.saimao.easyfood.R
import it.saimao.easyfood.activities.CategoryMealActivity
import it.saimao.easyfood.activities.MainActivity
import it.saimao.easyfood.adapters.CategoryAdapter
import it.saimao.easyfood.databinding.FragmentCategoryBinding
import it.saimao.easyfood.viewmodel.HomeViewModel

class CategoryFragment : Fragment() {

    private val binding: FragmentCategoryBinding by lazy {
        FragmentCategoryBinding.inflate(layoutInflater)
    }

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ((activity) as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initData()
    }

    private fun initUi() {
        categoryAdapter = CategoryAdapter()
        categoryAdapter.onItemClicked = {
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
        binding.rvCategoryList.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoryAdapter
        }
    }

    private fun initData() {
        viewModel.categoryList.observe(viewLifecycleOwner) {
            categoryAdapter.setCategoryList(it)
        }
    }

}