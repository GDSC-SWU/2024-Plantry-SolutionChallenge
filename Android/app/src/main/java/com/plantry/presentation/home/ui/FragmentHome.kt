package com.plantry.presentation.home.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingFragment
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import com.plantry.databinding.FragmentHomeBinding
import com.plantry.presentation.home.adapter.pantry.PantryAdapter
import com.plantry.presentation.home.bottomsheet.HomeElseBottomSheet
import com.plantry.presentation.home.popup.HomePlusPopUp
import com.plantry.presentation.home.viewmodel.pantry.PantryListViewModel
import com.plantry.presentation.home.viewmodel.pantry.PantryStarViewModel

class FragmentHome : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel_list by viewModels<PantryListViewModel>({ requireParentFragment() })
    private val viewModel_star by viewModels<PantryStarViewModel>()
    override fun initView() {
        resetScrollPosition()
        viewModel_list.getPantryList()
        observe_list()
        observe_star()
        clickNotification()
    }


    private fun clickNotification() {
        binding.ivHomeNotification.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_home_notification)
        }
    }

    private fun resetScrollPosition() {
        binding.svHomeScrollview.smoothScrollTo(0, 0)
    }

    private fun clickItem(adapter: PantryAdapter, list: List<ResponsePantryDto.Result>) {
        adapter.pantryItemClick = object : ItemClick {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.cl_home_item_layout -> { // position.equals(list.size - 1)
                        if (list[position].title.equals("")) { // add pantry 아이템
                            val pantryPlusPopUp = HomePlusPopUp()
                            pantryPlusPopUp.setStyle(
                                STYLE_NO_TITLE,
                                R.style.Theme_Plantry_AlertDialog
                            )
                            pantryPlusPopUp.show(parentFragmentManager, POP_UP)
                        } else { // 세부 pantry로 넘어가는 것
                            arguments = Bundle().apply {
                                putString("pantry_name", list[position].title)
                                putInt("pantry_id", list[position].id)
                            }
                            findNavController().navigate(R.id.action_home_to_home_pantry, arguments)
                        }
                    }

                    R.id.iv_home_item_else -> {
                        val editAndDeleteBottomsheet = HomeElseBottomSheet()
                        editAndDeleteBottomsheet.arguments = Bundle().apply {
                            putInt("pantry_id", list[position].id)
                            putString("pantry_title", list[position].title)
                            putString("pantry_color", list[position].color)
                        }
                        editAndDeleteBottomsheet.show(parentFragmentManager, BOTTOM_SHEET)
                    }

                    R.id.iv_home_item_heart -> {
                        view.isSelected = !view.isSelected
                        viewModel_star.patchSetStar(list[position].id)
                    }
                }
            }
        }
    }

    private fun observe_list() {
        viewModel_list.pantryList.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val adapter = PantryAdapter()
                    binding.rcvHomeRecyclerview.adapter = adapter
                    var list = it.data.result
                    list += arrayListOf(ResponsePantryDto.Result("", -1, false, ""))
                    adapter.submitList(list)
                    clickItem(adapter, list)
                    Log.d("aaa", list.toString())
                }

                else -> Unit
            }
        }
    }

    private fun observe_star() {
        viewModel_star.pantryStar.observe(this) {
            when (it) {
                is UiState.Success -> {
                    viewModel_list.getPantryList()
                }

                else -> Unit
            }
        }
    }

    companion object {
        const val BOTTOM_SHEET = "home_else_bottom_sheet"
        const val POP_UP = "home_plus_pop_up"
    }
}


