package it.saimao.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.saimao.easyfood.R
import it.saimao.easyfood.activities.MainActivity
import it.saimao.easyfood.activities.MealActivity
import it.saimao.easyfood.adapters.FavouriteMealAdapter
import it.saimao.easyfood.databinding.FragmentFavouriteBinding
import it.saimao.easyfood.viewmodel.HomeViewModel


class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouriteMealAdapter: FavouriteMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initData()
        implSwipeToDelete()
    }

    private fun implSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentMeal =
                    favouriteMealAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.removeFromFavourite(meal = currentMeal)
                Snackbar.make(
                    requireView(),
                    "Remove from favourite success", Snackbar.LENGTH_LONG
                )
                    .setAction(
                        "Undo"
                    ) {
                        viewModel.addToFavourite(meal = currentMeal)
                    }.show()
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFavourites)
    }

    private fun initUi() {
        favouriteMealAdapter = FavouriteMealAdapter()
        favouriteMealAdapter.favouriteMealClicked = {
            val intent = Intent(requireContext(), MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favouriteMealAdapter
        }
    }

    private fun initData() {
        viewModel.favouriteMeals.observe(viewLifecycleOwner) { meals ->
            // TODO : use diffutils here!
            favouriteMealAdapter.differ.submitList(meals)
        }
    }

}