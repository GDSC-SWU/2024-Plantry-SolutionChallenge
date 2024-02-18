package com.plantry.presentation.addfood.popup

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.fragment.longToast
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.request.product.RequestProductAddSingle
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import com.plantry.databinding.PopupAddFoodBinding
import com.plantry.databinding.PopupAddFoodDeleteOptionBindingImpl
import com.plantry.presentation.addfood.adapter.FoodIconListAdapter
import com.plantry.presentation.addfood.adapter.PantryNameListAdapter
import com.plantry.presentation.addfood.bottomsheet.AddFoodIconSelectBottomSheet
import com.plantry.presentation.addfood.viewmodel.FoodViewModel
import com.plantry.presentation.addfood.viewmodel.ProductAddSingleViewModel
import com.plantry.presentation.home.adapter.PantryAdapter
import com.plantry.presentation.home.bottomsheet.HomeElseBottomSheet
import com.plantry.presentation.home.popup.HomePlusPopUp
import com.plantry.presentation.home.ui.FragmentHome
import com.plantry.presentation.home.ui.FragmentHomePantry
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.ALL
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.COLD
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.ETC
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.FOR_ADD_FROM_BASE
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.FOR_ADD_FROM_NO_BASE
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.FOR_EDIT
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.FREEZE
import com.plantry.presentation.home.viewmodel.pantry.PantryListViewModel
import com.plantry.presentation.home.viewmodel.product.ProductDeleteViewModel
import com.plantry.presentation.home.viewmodel.product.ProductEditViewModel
import com.plantry.presentation.home.viewmodel.product.ProductListSearchViewModel
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class AddFoodPopUp : BindingDialogFragment<PopupAddFoodBinding>(R.layout.popup_add_food) {
    private val viewModelPantryLlist by viewModels<PantryListViewModel>()
    private val viewModelProductList by viewModels<ProductListSearchViewModel>({ requireParentFragment() })
    private val viewModelProuductAdd by viewModels<ProductAddSingleViewModel>()
    private val viewModelProuductEdit by viewModels<ProductEditViewModel>()
    private val viewModelFood by viewModels<FoodViewModel>({ requireActivity() })
    private val viewModelProuductDelete by viewModels<ProductDeleteViewModel>({ requireActivity() })

    var changedPantryId: Int = 0
    var storage: String = COLD
    var halfUnitCheck: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        iconObserver()
        checkMode()
        clickCancleButton()
        clickPantryName()
        clickDatePopup()
        clickPantryDate()
        observeList()
        observeEdit()
        observeDelete()
        observeProductAdd()
        clickIconSelected()
        getStorageSelected()
        selecteHalfUnit()
        plusCount(halfUnitCheck)
        minusCount(halfUnitCheck)
        checkDateValidate()
        clickConfirmButton()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkMode() {
        val getMode = arguments?.getInt("mode")
        when (getMode) {
            FOR_EDIT -> {
                setDeleteButton()
                setPantryNameAndPantryFilter()
                setContAndIconAndDateModeProduct()
                setDateProduct()
            }

            FOR_ADD_FROM_NO_BASE -> {
            }

            FOR_ADD_FROM_BASE -> {
                setPantryNameAndPantryFilter()
            }
        }
    }

    private fun clickIconSelected() {
        binding.tvAddFoodPopupFoodIconTestImg.setOnClickListener {
            openFoodIconSelect()
        }
        binding.tvAddFoodPopupFoodIconRealImg.setOnClickListener {
            openFoodIconSelect()
        }
    }

    private fun openFoodIconSelect() {
        val productIconSelectedBottomSheet = AddFoodIconSelectBottomSheet()
        productIconSelectedBottomSheet.setStyle(
            STYLE_NO_TITLE, R.style.Theme_Plantry_AlertDialog
        )
        productIconSelectedBottomSheet.show(parentFragmentManager, POP_UP_ICON)

    }


    private fun iconObserver() {
        viewModelFood.iconLiveData.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.tvAddFoodPopupFoodIconTestImg.visibility = View.INVISIBLE
                    binding.tvAddFoodPopupFoodIconRealImg.visibility = View.VISIBLE
                    binding.tvAddFoodPopupFoodIconRealImg.text = it.data
                }

                else -> Unit
            }
        }
        viewModelFood.nameLiveData.observe(this) {
            when (it) {
                is UiState.Success -> {
                    binding.etAddFoodPopupFoodNameInput.setText(it.data)
                }

                else -> Unit
            }
        }
    }


    private fun setPantryNameAndPantryFilter() {
        val pantryName = arguments?.getString("pantry_name")
        val pantryFilter = arguments?.getString("pantry_filter")
        if (!(pantryName.isNullOrEmpty())) {
            binding.tvAddFoodPopupSelectPantryContent.text = pantryName
        }
        when (pantryFilter) {
            COLD -> {
                binding.rbAddFoodStorageCold.isChecked = true
            }

            FREEZE -> {
                binding.rbAddFoodStorageFreeze.isChecked = true
            }

            ETC -> {
                binding.rbAddFoodStorageEtc.isChecked = true
            }
        }
    }

    private fun setContAndIconAndDateModeProduct() {
        val count: Double = arguments?.getDouble("count", 0.0) ?: 1.0
        val icon: String = arguments?.getString("icon", "") ?: ""
        val isUseByDate: Int = arguments?.getInt("isUseByDate", 0) ?: 0
        val name: String = arguments?.getString("products_name", "") ?: ""

        // count
        binding.tvHomePantryItemContentCount.text = count.toString()
        tryNotShowDouble()

        // icon
        binding.tvAddFoodPopupFoodIconRealImg.visibility = View.VISIBLE
        binding.tvAddFoodPopupFoodIconTestImg.visibility = View.INVISIBLE
        binding.tvAddFoodPopupFoodIconRealImg.text = icon

        // isUseByDate
        if (isUseByDate == 1) {
            binding.tvAddFoodPopupDateCheck.text =
                getText(R.string.home_pantry_item_content_use_by_date).toString()
        } else if (isUseByDate == 2) {
            binding.tvAddFoodPopupDateCheck.text =
                getText(R.string.home_pantry_item_content_sell_by_date).toString()
        }

        // name
        binding.etAddFoodPopupFoodNameInput.setText(name)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateProduct() {
        // arguments로부터 날짜 데이터 가져오기
        val days: Int = arguments?.getInt("days", 0) ?: 0

        // 현재 날짜 가져오기
        val currentDate: LocalDate = LocalDate.now()

        // 해당 날짜 계산
        val targetDate: LocalDate = currentDate.plusDays((days).toLong() + 1)

        // 날짜를 yy.MM.dd 형식으로 포맷팅
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
        val formattedDate: String = targetDate.format(dateFormatter)

        binding.etAddFoodPopupDateInput.setText(formattedDate)

    }

    private fun setDeleteButton() {
        binding.tvAddFoodPopupCancle.text = getText(R.string.popup_button_delete)
        binding.tvAddFoodPopupCancle.setTextColor(resources.getColor(R.color.white1))
        binding.tvAddFoodPopupCancle.setBackgroundResource(R.drawable.shape_fill_error_color_8_rec)
    }

    private fun clickPantryName() {
        var pantryNameClick: Boolean = false

        binding.tvAddFoodPopupSelectPantryContent.setOnClickListener {
            pantryNameClick = !pantryNameClick
            viewModelPantryLlist.getPantryList()
            if (pantryNameClick) {
                binding.layoutPopupAddFoodPantryNameList.clAddFoodPopupPantryList.visibility =
                    View.VISIBLE
                binding.layoutPopupAddFoodDayList.clAddFoodPopupDate.visibility =
                    View.GONE
            } else {
                binding.layoutPopupAddFoodPantryNameList.clAddFoodPopupPantryList.visibility =
                    View.GONE
            }
        }
    }

    private fun clickItem(adapter: PantryNameListAdapter, list: List<ResponsePantryDto.Result>) {
        adapter.nameClick = object : ItemClick {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.cl_add_food_pantry_name_item -> {
                        binding.tvAddFoodPopupSelectPantryContent.text = list[position].title
                        changedPantryId = list[position].id
                        binding.layoutPopupAddFoodPantryNameList.clAddFoodPopupPantryList.visibility =
                            View.GONE
                    }
                }
            }
        }
    }

    private fun clickPantryDate() {
        var pantryDateClick: Boolean = false

        binding.tvAddFoodPopupDateCheck.setOnClickListener {
            pantryDateClick = !pantryDateClick
            if (pantryDateClick) {
                binding.layoutPopupAddFoodDayList.clAddFoodPopupDate.visibility = View.VISIBLE
                binding.layoutPopupAddFoodPantryNameList.clAddFoodPopupPantryList.visibility =
                    View.GONE
                clickedDateItem()
            } else {
                binding.layoutPopupAddFoodDayList.clAddFoodPopupDate.visibility = View.GONE
            }
        }
    }

    private fun clickedDateItem() {
        binding.layoutPopupAddFoodDayList.tvAddFoodUseDate.setOnClickListener {
            binding.tvAddFoodPopupDateCheck.setText(R.string.home_pantry_item_content_use_by_date)
            binding.layoutPopupAddFoodDayList.clAddFoodPopupDate.visibility = View.GONE
        }
        binding.layoutPopupAddFoodDayList.tvAddFoodSellDate.setOnClickListener {
            binding.tvAddFoodPopupDateCheck.setText(R.string.home_pantry_item_content_sell_by_date)
            binding.layoutPopupAddFoodDayList.clAddFoodPopupDate.visibility = View.GONE
        }
    }

    private fun clickDatePopup() {
        binding.tvAddFoodPopupDayTitle.setOnClickListener {
            val pantryPlusPopUp = AddFoodDatePopUp()
            pantryPlusPopUp.setStyle(
                STYLE_NO_TITLE, R.style.Theme_Plantry_AlertDialog
            )
            pantryPlusPopUp.show(parentFragmentManager, POP_UP_DAY)
        }
    }

    private fun getStorageSelected() {
        binding.rgAddFoodStorageRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_add_food_storage_cold -> {
                    storage = COLD
                }

                R.id.rb_add_food_storage_etc -> {
                    storage = ETC
                }

                R.id.rb_add_food_storage_freeze -> {
                    storage = FREEZE
                }
            }
        }
    }

    private fun selecteHalfUnit() {
        binding.tvAddFoodHalfCheck.setOnClickListener {
            halfUnitCheck = !halfUnitCheck
            binding.tvAddFoodHalfCheck.isSelected = halfUnitCheck
            plusCount(halfUnitCheck)
            minusCount(halfUnitCheck)
        }
    }

    private fun plusCount(halUnitCheck: Boolean) {
        binding.ivAddFoodPopupCountPlus.setOnClickListener {
            binding.tvAddFoodPopupCountErrorMessage.visibility = View.GONE
            if (halUnitCheck) {
                val count = binding.tvHomePantryItemContentCount.text.toString()
                binding.tvHomePantryItemContentCount.text = (count.toDouble() + 0.5).toString()
            } else {
                val count = binding.tvHomePantryItemContentCount.text.toString()
                binding.tvHomePantryItemContentCount.text = (count.toDouble() + 1).toString()
            }
            tryNotShowDouble()
        }
    }

    private fun tryNotShowDouble() {
        val nowCount = binding.tvHomePantryItemContentCount.text.toString().toDouble()
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = if (nowCount % 1 == 0.0) 0 else 1
        binding.tvHomePantryItemContentCount.text = numberFormat.format(nowCount)

    }

    private fun minusCount(halUnitCheck: Boolean) {
        binding.ivAddFoodPopupCountMinus.setOnClickListener {
            binding.tvAddFoodPopupCountErrorMessage.visibility = View.GONE
            if (halUnitCheck) {
                val nowCount = binding.tvHomePantryItemContentCount.text.toString()
                val minusCount = nowCount.toDouble() - 0.5
                if (minusCount <= 0.0) {
                    binding.tvAddFoodPopupCountErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.tvHomePantryItemContentCount.text = minusCount.toString()
                }
            } else {
                val nowCount = binding.tvHomePantryItemContentCount.text.toString()
                val minusCount = nowCount.toDouble() - 1
                if (minusCount <= 0.0) {
                    binding.tvAddFoodPopupCountErrorMessage.visibility = View.VISIBLE
                } else {
                    binding.tvHomePantryItemContentCount.text = minusCount.toString()
                }
            }
            tryNotShowDouble()
        }
    }

    private fun clickCancleButton() {
        val productId: Int = arguments?.getInt("productId", 0) ?: 0
        if (binding.tvAddFoodPopupCancle.text.toString().equals("Delete")) {
            binding.tvAddFoodPopupCancle.setOnClickListener {
                val pantryFilter = arguments?.getString("pantry_filter")
                val productDeletePopUp = AddFoodDeleteOptionPopUp()
                productDeletePopUp.setStyle(
                    STYLE_NO_TITLE,
                    R.style.Theme_Plantry_AlertDialog
                )
                productDeletePopUp.arguments = Bundle().apply {
                    putInt("productId", productId)
                    putDouble(
                        "count",
                        binding.tvHomePantryItemContentCount.text.toString().toDouble()
                    )
                    putString("pantryFilter", pantryFilter)
                }
                productDeletePopUp.show(childFragmentManager, POP_UP_DELETE)
            }
        } else {
            binding.tvAddFoodPopupCancle.setOnClickListener {
                dialog?.dismiss()
            }
        }

        viewModelFood.setFaliureIcon()
        viewModelFood.setFaliureName()
    }

    private fun clickConfirmButton() {
        binding.tvAddFoodPopupConfirm.setOnClickListener {
            val pantryId = arguments?.getInt("pantry_id")
            val pantryName = binding.tvAddFoodPopupSelectPantryContent.text.toString()
            val productIcon = binding.tvAddFoodPopupFoodIconRealImg.text.toString()
            val productName = binding.etAddFoodPopupFoodNameInput.text.toString()
            val isUseByDate = when (binding.tvAddFoodPopupDateCheck.text.toString()) {
                "Use-by Date" -> {
                    true
                }

                "Sell-by Date" -> {
                    false
                }

                else -> {
                    true
                }
            }
            val sdfInput = SimpleDateFormat("yy.MM.dd")
            val sdfOutput = SimpleDateFormat("yyyy-MM-dd")

            val dateString = "20" + binding.etAddFoodPopupDateInput.text.toString()
            var productDate = ""
            try {
                val date = sdfInput.parse(dateString)
                productDate = sdfOutput.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val productCount = binding.tvHomePantryItemContentCount.text.toString()

            if (pantryName.isNullOrEmpty()) {
                longToast("please select pantry!")
            } else if (productIcon.isNullOrEmpty()) {
                longToast("please select product icon!")
            } else if (productName.isNullOrEmpty()) {
                longToast("Please enter the name of the product!")
            } else if (productDate.isNullOrEmpty()) {
                binding.tvAddFoodPopupDateErrorMessage.visibility = View.VISIBLE
                binding.tvAddFoodPopupDateErrorMessage.text = DATE_EMPTY
            } else if (binding.tvAddFoodPopupDateErrorMessage.visibility == View.VISIBLE ||
                binding.tvAddFoodPopupCountErrorMessage.visibility == View.VISIBLE ||
                binding.tvAddFoodPopupFoodErrorMessage.visibility == View.VISIBLE
            ) {
                longToast("please check error Message!")
            } else {
                val requestAddProduct = RequestProductAddSingle(
                    count = productCount.toDouble(),
                    date = productDate,
                    icon = productIcon,
                    isUseByDate = isUseByDate,
                    name = productName,
                    pantry = if (changedPantryId != 0) {
                        changedPantryId
                    } else {
                        pantryId
                    },
                    storage = storage
                )

                Log.d("aaa", requestAddProduct.toString())
                Log.d("aaa", pantryId.toString())
                val productId: Int = arguments?.getInt("productId", 0) ?: 0

                if (productId > 0) {
                    viewModelProuductEdit.patchEditProduct(productId, requestAddProduct)
                } else {
                    viewModelProuductAdd.postAddSingleProduct(requestAddProduct)
                }

                changedPantryId = 0
                viewModelFood.setFaliureIcon()
                viewModelFood.setFaliureName()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        viewModelFood.setFaliureIcon()
        viewModelFood.setFaliureName()
    }

    private fun observeList() {
        viewModelPantryLlist.pantryList.observe(this) {
            when (it) {
                is UiState.Success -> {
                    val adapter = PantryNameListAdapter()
                    binding.layoutPopupAddFoodPantryNameList.rcvAddFoodPopupPantryList.adapter =
                        adapter
                    adapter.submitList(it.data.result)
                    clickItem(adapter, it.data.result)
                }

                else -> Unit
            }
        }
    }

    private fun observeEdit() {
        viewModelProuductEdit.productEdit.observe(this) {
            val pantryId = arguments?.getInt("pantry_id")
            val pantryFilter = arguments?.getString("pantry_filter")
            when (it) {
                is UiState.Success -> {
                    if (pantryId != null && pantryFilter != null) {
                        viewModelProductList.getListSearchProduct(pantryId, pantryFilter)
                    }
                    dismiss()
                }

                else -> Unit
            }
        }
    }

    private fun observeProductAdd() {
        viewModelProuductAdd.productAddSingle.observe(this) {
            val pantryId = arguments?.getInt("pantry_id")
            val pantryFilter = arguments?.getString("pantry_filter")
            when (it) {
                is UiState.Success -> {
                    if (pantryId != null && pantryFilter != null) {
                        viewModelProductList.getListSearchProduct(pantryId, pantryFilter)
                    }
                    dismiss()
                }

                else -> Unit
            }
        }
    }

    private fun checkDateValidate() {
        binding.etAddFoodPopupDateInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isEmpty()) {
                    binding.tvAddFoodPopupDateErrorMessage.visibility = View.VISIBLE
                    binding.tvAddFoodPopupDateErrorMessage.text = DATE_EMPTY
                } else {
                    val checkedResult = isValidDateFormat(text.toString())
                    if (!checkedResult.equals(DATE_OK)) {
                        binding.tvAddFoodPopupDateErrorMessage.visibility = View.VISIBLE
                        binding.tvAddFoodPopupDateErrorMessage.text = checkedResult
                    } else {
                        binding.tvAddFoodPopupDateErrorMessage.visibility = View.GONE
                    }
                }
            }

        }

        )
    }

    private fun isValidDateFormat(input: String): String {
        val dateFormatPattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{2}\$")
        val matcher = dateFormatPattern.matcher(input)

        // 정규표현식에 일치하는지 확인
        if (matcher.matches()) {
            val sdf = SimpleDateFormat("yy.MM.dd")
            sdf.isLenient = false // 엄격한 모드로 설정하여 유효한 날짜만 허용
            try {
                sdf.parse(input)
                return DATE_OK // 유효한 날짜 형식인 경우
            } catch (e: Exception) {
                return DATE_NOT_VALIDATE // 날짜 형식이지만 유효하지 않은 경우
            }
        } else {
            return DATE_UN_CHEKED// 정규표현식과 일치하지 않는 경우
        }
    }

    private fun observeDelete() {
        viewModelProuductDelete.productDelete.observe(this) {
            val pantryId = arguments?.getInt("pantry_id")
            val pantryFilter = arguments?.getString("pantry_filter")
            when (it) {
                is UiState.Success -> {
                    Log.d("aaa", it.data.toString() + "딜리트")
                    if (pantryId != null && pantryFilter != null) {
                        viewModelProductList.getListSearchProduct(pantryId, pantryFilter)
                    }
                    dismiss()
                }

                else -> Unit
            }
        }
    }

    companion object {
        const val POP_UP_DAY = "add_food_popup_to_day_detail_popup"
        const val POP_UP_ICON = "add_food_popup_to_icon_select_bottom_sheet"
        const val POP_UP_DELETE = "add_food_popup_to_delet_products"
        const val DATE_OK = "ok"
        const val DATE_NOT_VALIDATE = "Please enter a valid date."
        const val DATE_UN_CHEKED = "Please write it in the order of \n" + "year, month, and day."
        const val DATE_EMPTY = "Please enter the date."
    }
}
