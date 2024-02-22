package com.plantry.presentation.addfood.popup.addfood

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.view.UiState
import com.plantry.databinding.PopupAddFoodDeleteOptionBinding
import com.plantry.presentation.home.ui.home.FragmentHomePantry.Companion.ALL
import com.plantry.presentation.home.viewmodel.product.ProductDeleteViewModel
import com.plantry.presentation.home.viewmodel.product.ProductListSearchViewModel
import java.text.NumberFormat

class AddFoodDeleteOptionPopUp :
    BindingDialogFragment<PopupAddFoodDeleteOptionBinding>(R.layout.popup_add_food_delete_option) {
    private val viewModelProuductDelete by viewModels<ProductDeleteViewModel>({ requireActivity() })
    private val viewModelProductList by viewModels<ProductListSearchViewModel>({ requireParentFragment() })

    var halfUnitCheck: Boolean = false
    var option: Int = 1
    var itemCountEnd: Double = 0.5

    override fun initView() {
        Log.d("bbb", "optionpopup")
        clickCancleButton()
        checkedOption()
        clickDeleteButton()
        plusCount(halfUnitCheck)
        minusCount(halfUnitCheck)
        observe()
        clickHalfUnit()
    }

    private fun clickCancleButton() {
        dialog?.dismiss()
    }


    private fun clickDeleteButton() {
        val productId: Int = arguments?.getInt("productId", 0) ?: -1
        binding.tvAddFoodDeletePopupConfirm.setOnClickListener {
            viewModelProuductDelete.deleteProduct(productId, option, itemCountEnd)
        }
    }

    private fun clickHalfUnit() {
        binding.tvAddFoodDeleteHalfCheck.setOnClickListener {
            halfUnitCheck = !halfUnitCheck
            binding.tvAddFoodDeleteHalfCheck.isSelected = halfUnitCheck
            plusCount(halfUnitCheck)
            minusCount(halfUnitCheck)
        }
    }

    private fun checkedOption() {
        binding.rgAddFoodDeleteOption.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_add_food_delete_ingestion -> {
                    Log.d("aaa", INGESTION.toString())
                    option = INGESTION
                }

                R.id.rb_add_food_delete_disposal -> {
                    Log.d("aaa", DISPOSAL.toString())
                    option = DISPOSAL
                }

                R.id.rb_add_food_delete_share -> {
                    Log.d("aaa", SHARING.toString())
                    option = SHARING
                }

                R.id.rb_add_food_delete_mistake -> {
                    Log.d("aaa", MISTAKE.toString())
                    option = MISTAKE
                }

                else -> {
                    option = INGESTION
                }
            }
        }
    }

    private fun plusCount(halUnitCheck: Boolean) {
        binding.ivAddFoodPopupDeleteCountPlus.setOnClickListener {
            binding.tvAddFoodPopupDeleteCountErrorMessage.visibility = View.GONE
            if (halUnitCheck) {
                checkPlusCountIsLessThanItemCount(0.5)
                Log.d("Aaa+", halUnitCheck.toString())
            } else {
                checkPlusCountIsLessThanItemCount(1.0)
                Log.d("Aaa+", halUnitCheck.toString())
            }
            tryNotShowDouble()
        }
    }

    private fun checkPlusCountIsLessThanItemCount(count: Double) {
        val itemCount: Double = arguments?.getDouble("count", 0.0) ?: -1.0

        val nowCount = binding.tvAddFoodPopupDeleteCount.text.toString()
        val plusCount = nowCount.toDouble() + count
        if (plusCount > itemCount) {
            binding.tvAddFoodPopupDeleteCountErrorMessage.visibility = View.VISIBLE
            binding.tvAddFoodPopupDeleteCountErrorMessage.text =
                getText(R.string.popup_add_food_error_message5)
        } else {
            binding.tvAddFoodPopupDeleteCount.text = (plusCount).toString()
            itemCountEnd = plusCount
            Log.d("Aaa", itemCountEnd.toString())
        }
    }

    private fun minusCount(halUnitCheck: Boolean) {
        binding.ivAddFoodPopupDeleteCountMinus.setOnClickListener {
            binding.tvAddFoodPopupDeleteCountErrorMessage.visibility = View.GONE
            if (halUnitCheck) {
                checkMinusCountIsMoreThanZero(0.5)
                Log.d("Aaa-", halUnitCheck.toString())
            } else {
                checkMinusCountIsMoreThanZero(1.0)
                Log.d("Aaa-", halUnitCheck.toString())
            }
            tryNotShowDouble()
        }
    }


    private fun checkMinusCountIsMoreThanZero(count: Double) {
        val nowCount = binding.tvAddFoodPopupDeleteCount.text.toString()
        val minusCount = nowCount.toDouble() - count
        if (minusCount <= 0.0) {
            binding.tvAddFoodPopupDeleteCountErrorMessage.visibility = View.VISIBLE
            binding.tvAddFoodPopupDeleteCountErrorMessage.text =
                getText(R.string.popup_add_food_error_message4)
        } else {
            binding.tvAddFoodPopupDeleteCount.text = minusCount.toString()
            itemCountEnd = minusCount
            Log.d("Aaa", itemCountEnd.toString())
        }
    }
    private fun tryNotShowDouble() {
        val nowCount = binding.tvAddFoodPopupDeleteCount.text.toString().toDouble()
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = if (nowCount % 1 == 0.0) 0 else 1
        binding.tvAddFoodPopupDeleteCount.text = numberFormat.format(nowCount)

    }

    private fun observe() {
        viewModelProuductDelete.productDelete.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val pantryFilter: String = arguments?.getString("pantryFilter", ALL) ?:ALL
                    val pantryId: Int = arguments?.getInt("pantryId", 0) ?: -1
                    viewModelProductList.getListSearchProduct(pantry = pantryId, pantryFilter)
                    dismiss()
                    viewModelProuductDelete.setProductDeleteEmpty()

                }

                else -> Unit
            }
        }
    }

    companion object {
        const val INGESTION = 1
        const val DISPOSAL = 2
        const val SHARING = 3
        const val MISTAKE = 4
    }
}
