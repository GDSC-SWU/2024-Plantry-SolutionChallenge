package com.plantry.presentation.addfood.popup.ocr

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.plantry.R
import com.plantry.coreui.adapter.ItemClick
import com.plantry.coreui.base.BindingDialogFragment
import com.plantry.coreui.fragment.longToast
import com.plantry.coreui.view.UiState
import com.plantry.data.dto.request.product.RequestProductAddSingle
import com.plantry.data.dto.response.ocr.ResponseOcrSubmit
import com.plantry.data.dto.response.pantry.ResponsePantryDto
import com.plantry.databinding.PopupAddFoodBinding
import com.plantry.presentation.addfood.adapter.PantryNameListAdapter
import com.plantry.presentation.addfood.bottomsheet.AddFoodIconSelectBottomSheet
import com.plantry.presentation.addfood.popup.addfood.AddFoodDatePopUp
import com.plantry.presentation.addfood.popup.addfood.AddFoodPopUp
import com.plantry.presentation.addfood.viewmodel.ocr.OcrSubmitViewModel
import com.plantry.presentation.addfood.viewmodel.product.FoodViewModel
import com.plantry.presentation.addfood.viewmodel.product.ProductAddSingleViewModel
import com.plantry.presentation.home.ui.home.FragmentHomePantry.Companion.COLD
import com.plantry.presentation.home.ui.home.FragmentHomePantry.Companion.ETC
import com.plantry.presentation.home.ui.home.FragmentHomePantry.Companion.FREEZE
import com.plantry.presentation.home.viewmodel.pantry.PantryListViewModel
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class MultiAddFoodPopUp : BindingDialogFragment<PopupAddFoodBinding>(R.layout.popup_add_food) {
    private val viewModelPantryLlist by viewModels<PantryListViewModel>()
    private val viewModelProuductAdd by viewModels<ProductAddSingleViewModel>()
    private val viewModelFood by viewModels<FoodViewModel>({ requireActivity() })
    private val viewModelOcrSubmit by viewModels<OcrSubmitViewModel>({ requireActivity() })

    var changedPantryId: Int = 0
    var storage: String = COLD
    var halfUnitCheck: Boolean = false
    var ItemCount = 1
    var ocrResult: List<ResponseOcrSubmit.Product?>? = null

    override fun initView() {
        // multi 에서 나온 작업
        setItemNum()
        setConfirmButtonText()
        observeOcr()
        observeProductAdd()
        clickConfirmButton()

        // 기본 설정 작업
        clickIconSelected()
        iconObserver()
        clickPantryName()
        clickDatePopup()
        clickPantryDate()
        getStorageSelected()
        selecteHalfUnit()
        clickCancleButton()
        observeList()
        checkDateValidate()
        plusCount(halfUnitCheck)
        minusCount(halfUnitCheck)

    }

    private fun setItemNum() {
        binding.tvAddFoodPopupCountTitle.visibility = View.VISIBLE
        binding.tvAddFoodPopupCountTitle.text =
            "${ItemCount}/${arguments?.getInt("itemCount").toString()} Food"
    }

    private fun setConfirmButtonText() {
        Log.d("aaa_connect1", ItemCount.toString())
        Log.d("aaa_connect1", (ItemCount - 1).toString())
        val itemTotalCount = arguments?.getInt("itemCount")
        Log.d("aaa_connect1", itemTotalCount.toString())
        if ((ItemCount) != itemTotalCount) {
            binding.tvAddFoodPopupConfirm.visibility = View.INVISIBLE
            binding.tvAddFoodPopupNext.visibility = View.VISIBLE
            Log.d("aaa_connect1", binding.tvAddFoodPopupConfirm.visibility.toString())
        } else {
            binding.tvAddFoodPopupConfirm.visibility = View.VISIBLE
            binding.tvAddFoodPopupNext.visibility = View.GONE
            Log.d("aaa_connect2", binding.tvAddFoodPopupConfirm.visibility.toString())
        }
    }


    private fun clickConfirmButton() {
        binding.tvAddFoodPopupConfirm.setOnClickListener {
            sendFoodInfo()
        }
        binding.tvAddFoodPopupNext.setOnClickListener {
            sendFoodInfo()
        }
    }

    private fun sendFoodInfo() {
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
                pantry = changedPantryId,
                storage = storage
            )
            Log.d("aaa1", requestAddProduct.toString())

            viewModelProuductAdd.postAddSingleProduct(requestAddProduct)
            Log.d("Aaa", " mlti")
        }
    }

    private fun observeProductAdd() {
        viewModelProuductAdd.productAddSingle.observe(this) {
            when (it) {
                is UiState.Success -> {
                    changedPantryId = 0
                    ItemCount += 1

                    setConfirmButtonText()
                    val itemTotalCount = arguments?.getInt("itemCount")
                    Log.d("aaa_connect", binding.tvAddFoodPopupConfirm.visibility.toString())
                    Log.d("aaa_connect3", itemTotalCount.toString())
                    if ((ItemCount - 1) == itemTotalCount) {
                        viewModelOcrSubmit.setFaliureResult()
                        Log.d("aaa_c", viewModelOcrSubmit.ocrResult.toString())
                        dismiss()
                    } else {
                        Log.d("aaa_connect", ItemCount.toString())
                        setFieldEmpty(ItemCount - 1)
                    }
                }

                else -> Unit
            }
        }
    }

    fun observeOcr() {
        viewModelOcrSubmit.ocrResult.observe(this) {
            when (it) {
                is UiState.Success -> {
                    ocrResult = it.data.data
                    setFieldEmpty(0)
                }

                else -> Unit
            }
        }
    }

    private fun setFieldEmpty(position: Int) {
        val totalCount = arguments?.getInt("itemCount")

        if (position != totalCount) {
            binding.tvAddFoodPopupCountTitle.text = "${position + 1}/${totalCount} Food"
            binding.etAddFoodPopupFoodNameInput.setText(ocrResult?.get(position)?.food_name ?: "")
            binding.tvHomePantryItemContentCount.text = (ocrResult?.get(position)?.quantity ?: 1).toString()
            viewModelFood.setFaliureIcon()
            binding.tvAddFoodPopupFoodIconRealImg.visibility = View.GONE
            binding.tvAddFoodPopupFoodIconTestImg.visibility = View.VISIBLE
            binding.etAddFoodPopupDateInput.setText(null)
            binding.tvAddFoodPopupSelectPantryContent.text = ""
            binding.rbAddFoodStorageCold.isChecked = true
            storage = COLD
            binding.tvAddFoodHalfCheck.isSelected = false
            halfUnitCheck = false
            binding.svAddFoodScrollview.smoothScrollTo(0, 0)
        }

        Log.d("aaa", binding.etAddFoodPopupFoodNameInput.text.toString())


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

    private fun clickItem(
        adapter: PantryNameListAdapter,
        list: List<ResponsePantryDto.Result>
    ) {
        adapter.nameClick = object : ItemClick {
            override fun onClick(view: View, position: Int) {
                when (view.id) {
                    R.id.cl_add_food_pantry_name_item -> {
                        binding.tvAddFoodPopupSelectPantryContent.text =
                            list[position].title
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
                binding.layoutPopupAddFoodDayList.clAddFoodPopupDate.visibility =
                    View.VISIBLE
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
                binding.tvHomePantryItemContentCount.text =
                    (count.toDouble() + 0.5).toString()
            } else {
                val count = binding.tvHomePantryItemContentCount.text.toString()
                binding.tvHomePantryItemContentCount.text =
                    (count.toDouble() + 1).toString()
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
        binding.tvAddFoodPopupCancle.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun dismiss() {
        viewModelFood.setFaliureIcon()
        viewModelFood.setFaliureName()
        ItemCount = 1
        super.dismiss()
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


    companion object {
        const val POP_UP_DAY = "add_food_popup_to_day_detail_popup"
        const val POP_UP_ICON = "add_food_popup_to_icon_select_bottom_sheet"
        const val DATE_OK = "ok"
        const val DATE_NOT_VALIDATE = "Please enter a valid date."
        const val DATE_UN_CHEKED =
            "Please write it in the order of \n" + "year, month, and day."
        const val DATE_EMPTY = "Please enter the date."
    }
}
