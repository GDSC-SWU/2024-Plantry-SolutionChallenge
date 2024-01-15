package com.plantry.presentation.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.data.dto.ResponseHomeDto
import com.plantry.databinding.FragmentHomeBinding
import com.plantry.presentation.home.adapter.PantryAdapter
import com.plantry.presentation.home.bottomsheet.HomeElseBottomSheet
import com.plantry.presentation.home.popup.HomePlusPopUp

class FragmentHome : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun initView() {
        listSetting()
    }

    private fun listSetting() {
        val adapter = PantryAdapter()
        // 가짜 데이터
        val pantryList = listOf(
            ResponseHomeDto("pantry1", true, "y"),
            ResponseHomeDto("pantry2", false, "y"),
            ResponseHomeDto("pantry3", true, "r"),
            ResponseHomeDto("", false, "")
        )
        clickItem(adapter, pantryList)
        binding.rcvHomeRecyclerview.adapter = adapter
        adapter.submitList(pantryList)
    }

    private fun clickItem(adapter: PantryAdapter, list: List<ResponseHomeDto>) {
        adapter.itemClick = object : PantryAdapter.PantryItemClick {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.cl_home_item_layout -> {
                        if (position.equals(list.size - 1)) {
                            val pantryPlusPopUp = HomePlusPopUp()
                            pantryPlusPopUp.setStyle(
                                STYLE_NO_TITLE,
                                R.style.Theme_Plantry_AlertDialog
                            )
                            pantryPlusPopUp.show(parentFragmentManager, POP_UP)
                        } else {
                            arguments = Bundle().apply {
                                putString("pantry_name", list[position].name)
                            }
                            findNavController().navigate(R.id.action_home_to_home_pantry, arguments)
                        }
                    }

                    R.id.iv_home_item_else -> {
                        val editAndDeleteBottomsheet = HomeElseBottomSheet()
                        editAndDeleteBottomsheet.show(parentFragmentManager, BOTTOM_SHEET)
                    }

                    R.id.iv_home_item_heart -> {
                        // 하트 클릭 로직
                    }
                }
            }
        }
    }

    companion object {
        const val BOTTOM_SHEET = "home_else_bottom_sheet"
        const val POP_UP = "home_plus_pop_up"
    }
}


