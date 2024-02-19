package com.plantry.presentation.profile.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingFragment
import com.plantry.databinding.FragmentProfileDeleteBinding
import com.plantry.presentation.profile.popup.DeleteIdPopUp

class FragmentProfileDelete :
    BindingFragment<FragmentProfileDeleteBinding>(R.layout.fragment_profile_delete){
    override var bnvVisibility = View.GONE
    var deleteOption = 1

    override fun initView() {
        clickBackStack()
        checkDeleteOption()
        clickDeleteSure()
        deleteOptionListVisibility()
    }


    private fun clickBackStack(){
        binding.ivProfileDeleteToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun deleteOptionListVisibility(){
        binding.tvProfileDeleteReasonContent.setOnClickListener {
            if(binding.layoutDeleteReaonList.clDeletePopupPantryList.visibility == View.GONE){
                binding.layoutDeleteReaonList.clDeletePopupPantryList.visibility = View.VISIBLE
            }
            else{
                binding.layoutDeleteReaonList.clDeletePopupPantryList.visibility = View.GONE
            }
        }
    }

    private fun checkDeleteOption(){
        var radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent1.text.toString()
        binding.layoutDeleteReaonList.rgDeleteOptionGroup.setOnCheckedChangeListener {group, checkedId ->
            binding.layoutDeleteReaonList.clDeletePopupPantryList.visibility = View.GONE
            when(checkedId){
                R.id.rb_profile_delete_reason_content1 -> {
                    deleteOption = 1
                    radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent1.text.toString()

                }
                R.id.rb_profile_delete_reason_content2 -> {
                    deleteOption = 2
                    radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent2.text.toString()

                }
                R.id.rb_profile_delete_reason_content3 -> {
                    deleteOption = 3
                    radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent3.text.toString()

                }
                R.id.rb_profile_delete_reason_content4 -> {
                    deleteOption = 4
                    radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent4.text.toString()

                }
                R.id.rb_profile_delete_reason_content5 -> {
                    deleteOption = 5
                    radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent5.text.toString()

                }
                R.id.rb_profile_delete_reason_content6 -> {
                    deleteOption = 6
                    radioButtonClickedText = binding.layoutDeleteReaonList.rbProfileDeleteReasonContent6.text.toString()

                }
            }
            binding.tvProfileDeleteReasonContent.setText(radioButtonClickedText)
        }
    }
    private fun clickDeleteSure(){
        binding.tvProfileDeleteButton.setOnClickListener {
            val signoutPopUp = DeleteIdPopUp()
            signoutPopUp.setStyle(
                STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            signoutPopUp.arguments =  Bundle().apply {
                putInt("deleteOption", deleteOption)
                Log.d("aaa", deleteOption.toString())
            }
            signoutPopUp.show(parentFragmentManager, POP_UP_SIGNGOUT)
        }
    }
    companion object{
        const val POP_UP_SIGNGOUT = "delete_popup_to_delete_sure_popup"
    }
}