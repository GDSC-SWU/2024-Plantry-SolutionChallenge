package com.plantry.presentation.addfood.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.plantry.R
import com.plantry.coreui.base.BindingBottomSheetFragment
import com.plantry.coreui.fragment.longToast
import com.plantry.coreui.fragment.toast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.FragmentAddFoodBinding
import com.plantry.presentation.addfood.popup.addfood.AddFoodPopUp
import com.plantry.presentation.addfood.viewmodel.ocr.OcrSubmitViewModel
import com.plantry.presentation.home.ui.home.FragmentHomePantry.Companion.FOR_ADD_FROM_NO_BASE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class FragmentAddFood :
    BindingBottomSheetFragment<FragmentAddFoodBinding>(R.layout.fragment_add_food) {

    private val viewModelOcrSubmit by viewModels<OcrSubmitViewModel>({ requireActivity() })

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
            if (checkPermission(CAMERA)) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchCameraIntent()
            } else {
                longToast("Please approve the camera permission.")
            }
        }

    private fun launchCameraIntent() {
        val storageDir: File? =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(
            "JPEG_img_",
            ".jpeg",
            storageDir
        )

        filepath = imageFile.absolutePath

        val photoURI: Uri = FileProvider.getUriForFile(
            requireContext(),
            "com.plantry.fileprovider",
            imageFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        longToast("Please put the receipt in the center of the screen.")
        requestCameraFileLauncher.launch(intent)
    }

    private val requestCameraFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(filepath)
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()

            val requestFile =
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)

            val imagePart =
                MultipartBody.Part.createFormData("image", "image.jpeg", requestFile)

            if (imageBitmap != null) {
                viewModelOcrSubmit.postOcrSubmit(imagePart)
                findNavController().navigate(R.id.action_add_food_to_ocr_loading)
                dismiss()
            } else {
                toast("Failed to get image.")
            }
        } else {
            toast("The camera has cancelled importing images.")
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
                        longToast("Please approve the camera permission.")
                    }
                }
            }
        }
    }


    companion object {
        const val POP_UP = "add_food_popup"
    }
}
