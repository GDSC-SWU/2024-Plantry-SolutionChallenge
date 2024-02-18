package com.plantry.presentation.addfood.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.plantry.R
import com.plantry.coreui.base.BindingBottomSheetFragment
import com.plantry.coreui.fragment.longToast
import com.plantry.coreui.fragment.toast
import com.plantry.databinding.FragmentAddFoodBinding
import com.plantry.presentation.addfood.popup.AddFoodPopUp
import com.plantry.presentation.home.ui.FragmentHomePantry.Companion.FOR_ADD_FROM_NO_BASE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class FragmentAddFood :
    BindingBottomSheetFragment<FragmentAddFoodBinding>(R.layout.fragment_add_food) {

    lateinit var filepath: String
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_CODE = 98

    override fun initView() {
        clickDirectInput()
        clickRecipt()
    }

    private fun clickDirectInput() {
        binding.tvAddFoodDirect.setOnClickListener {
            val addFoodPopUp = AddFoodPopUp()
            addFoodPopUp.setStyle(
                STYLE_NO_TITLE,
                R.style.Theme_Plantry_AlertDialog
            )
            addFoodPopUp.arguments = Bundle().apply {
                putInt("mode", FOR_ADD_FROM_NO_BASE)
            }
            addFoodPopUp.show(parentFragmentManager, POP_UP)
        }
    }

    private fun clickRecipt() {
        binding.tvAddFoodRecipt.setOnClickListener {
            CallCamera()
        }
    }

    fun CallCamera() {
        if (checkPermission(CAMERA)) {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filepath = file.absolutePath
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.plantry.fileprovider",
                file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            longToast("Please put the receipt in the center of the screen.")
            requestCameraFileLauncher.launch(intent)
        }
    }

    val requestCameraFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val option = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(filepath, option)
        bitmap?.let {
            binding.ivAddFoodResultImg.setImageBitmap(bitmap)
            binding.ivAddFoodResultImg.visibility = View.VISIBLE
        }
    }

    fun checkPermission(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        permissions,
                        CAMERA_CODE
                    )
                    return false;
                }
            }
        }
        return true;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), "카메라 권한을 승인해 주세요.", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }


    companion object {
        const val POP_UP = "add_food_popup"
    }
}
